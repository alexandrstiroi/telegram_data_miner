package org.shtiroy_ap.telegram.service;

import org.shtiroy_ap.telegram.entity.TenderPreference;
import org.shtiroy_ap.telegram.entity.User;
import org.shtiroy_ap.telegram.repository.TenderPreferenceRepository;
import org.shtiroy_ap.telegram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Service
public class AuthService {
    @Value("${telegram.bot.pin-code}")
    private String pinCode;

    private final UserRepository userRepository;
    private final TenderPreferenceRepository tenderPreferenceRepository;

    public AuthService(UserRepository userRepository, TenderPreferenceRepository tenderPreferenceRepository) {
        this.userRepository = userRepository;
        this.tenderPreferenceRepository = tenderPreferenceRepository;
    }

    public boolean isAuthorized(Long chatId) {
        return userRepository.findById(chatId)
                .map(User::isAuthorized)
                .orElse(false);
    }

    public void handleAuthorization(Update update, AbsSender sender) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();

        Optional<User> existing = userRepository.findById(chatId);
        if (existing.isEmpty()) {
            if (text.equals(pinCode)) {
                User user = new User(chatId, update.getMessage().getFrom().getUserName(), true);
                user = userRepository.save(user);
                //todo убрать после возможности настройки прдпочтений
                TenderPreference tenderPreference = new TenderPreference();
                tenderPreference.setCategoryId("30100000-0");
                tenderPreference.setUser(user);
                tenderPreferenceRepository.save(tenderPreference);
                tenderPreference.setCategoryId("30200000-0");
                tenderPreference.setUser(user);
                tenderPreferenceRepository.save(tenderPreference);
                //todo убрать
                sendMessage(sender, chatId, "✅ Авторизация успешна!");
            } else {
                sendMessage(sender, chatId, "🔐 Введите PIN-код для доступа:");
            }
        } else {
            sendMessage(sender, chatId, "🔐 Ожидаем правильный PIN-код:");
        }
    }

    private void sendMessage(AbsSender sender, Long chatId, String text) {
        try {
            sender.execute(new SendMessage(chatId.toString(), text));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
