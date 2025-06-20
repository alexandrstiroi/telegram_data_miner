package org.shtiroy_ap.telegram.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy_ap.telegram.entity.UserFavorite;
import org.shtiroy_ap.telegram.model.TenderDetailDto;
import org.shtiroy_ap.telegram.repository.UserFavoriteRepository;
import org.shtiroy_ap.telegram.util.TenderDetailComparator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.List;

@Service
public class FavoriteTenderService {
    private final UserFavoriteRepository favoriteRepository;
    private final TenderService tenderService;
    private final ObjectMapper objectMapper;
    private final Logger log = LogManager.getLogger(FavoriteTenderService.class.getName());

    public FavoriteTenderService(UserFavoriteRepository favoriteRepository, TenderService tenderService, ObjectMapper objectMapper) {
        this.favoriteRepository = favoriteRepository;
        this.tenderService = tenderService;
        this.objectMapper = objectMapper;
    }

    public boolean addFavorite(Long chatId, String tenderId){
        if (favoriteRepository.existsByChatIdAndTenderId(chatId, tenderId)){
            return false;
        }
        String snapshot = null;
        try {
            snapshot = objectMapper.writeValueAsString(tenderService.fetchTenderDetail(tenderId));
        } catch (JsonProcessingException ex){
            log.error("Ошибка парсинга {}", ex.getMessage());
        }
        UserFavorite fav = new UserFavorite();
        fav.setChatId(chatId);
        fav.setTenderId(tenderId);
        fav.setLastSnapshot(snapshot);

        favoriteRepository.save(fav);
        return true;
    }

    public void checkForUpdatesAndNotify(AbsSender absSender){
        List<UserFavorite> favorites = favoriteRepository.findAll();

        for (UserFavorite fav : favorites){
            TenderDetailDto previous;
            try {
                previous = objectMapper.readValue(fav.getLastSnapshot(), TenderDetailDto.class);
            } catch (JsonProcessingException exception){
                continue; // неможем распарсить json
            }
            if (!previous.getStatus().equals("cancelled")) {
                TenderDetailDto current = tenderService.verifyTenderDetail(fav.getTenderId());
                if (current != null) {
                    //Есть изменения
                    try {
                        String currentJson = objectMapper.writeValueAsString(current);
                        fav.setLastSnapshot(currentJson);
                    } catch (JsonProcessingException exception) {
                        exception.getMessage();
                    }
                    favoriteRepository.save(fav);
                    List<String> diff = TenderDetailComparator.compareTenderDetails(previous, current);
                    if (!diff.isEmpty()) {
                        String diffSummary = String.join("\n", diff);
                        ClassPathResource imgResource = new ClassPathResource("images/tender_alert.png");
                        if (diffSummary.length() < 1024) {
                            SendPhoto message = new SendPhoto();
                            message.setChatId(fav.getChatId());
                            message.setCaption("<b>📢 Обновление в тендере:</b> <i>" + current.getName() + ":</i>\n\n" + diffSummary);
                            message.setParseMode("HTML");
                            try {
                                message.setPhoto(new InputFile(imgResource.getInputStream(), "tender_alert.png"));
                                absSender.execute(message);
                            } catch (TelegramApiException | IOException exception) {
                                exception.printStackTrace();
                            }
                        } else {

                            try {
                                List<String> parts = TenderMessageBuilderService.splitMessageByLines("<b>📢 Обновление в тендере:</b> <i>" + current.getName() + ":</i>\n\n" + diffSummary);

                                // Отправка первой части с картинкой
                                SendPhoto photo = new SendPhoto();
                                photo.setChatId(fav.getChatId());
                                photo.setPhoto(new InputFile(imgResource.getInputStream(), "tender_alert.png"));
                                photo.setCaption(parts.get(0)); // caption до 1024 символов
                                photo.setParseMode("HTML");
                                absSender.execute(photo);

                                // Остальные части — SendMessage
                                for (int i = 1; i < parts.size(); i++) {
                                    SendMessage msg = new SendMessage();
                                    msg.setChatId(fav.getChatId());
                                    msg.setText(parts.get(i));
                                    msg.setParseMode("HTML");
                                    absSender.execute(msg);
                                }
                            } catch (TelegramApiException | IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
