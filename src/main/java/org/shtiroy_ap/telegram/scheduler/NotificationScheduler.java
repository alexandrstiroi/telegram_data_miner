package org.shtiroy_ap.telegram.scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy_ap.telegram.entity.TenderPreference;
import org.shtiroy_ap.telegram.entity.User;
import org.shtiroy_ap.telegram.model.TenderDto;
import org.shtiroy_ap.telegram.repository.TenderPreferenceRepository;
import org.shtiroy_ap.telegram.repository.UserRepository;
import org.shtiroy_ap.telegram.service.FavoriteTenderService;
import org.shtiroy_ap.telegram.service.MessageService;
import org.shtiroy_ap.telegram.service.TenderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

/**
 * Сервис отправки уведомлений по тендерам из избранных кодов.
 */
@Component
public class NotificationScheduler {
    private final TenderService tenderService;
    private final UserRepository userRepository;
    private final TenderPreferenceRepository tenderPreferenceRepository;
    private final MessageService messageService;
    private final AbsSender absSender;
    private final FavoriteTenderService favoriteTenderService;
    private final Logger log = LogManager.getLogger(NotificationScheduler.class.getName());

    public NotificationScheduler(TenderService tenderService, UserRepository userRepository, TenderPreferenceRepository tenderPreferenceRepository,
                                 MessageService messageService, AbsSender absSender, FavoriteTenderService favoriteTenderService){
        this.tenderService = tenderService;
        this.userRepository = userRepository;
        this.tenderPreferenceRepository = tenderPreferenceRepository;
        this.messageService = messageService;
        this.absSender = absSender;
        this.favoriteTenderService = favoriteTenderService;
    }

    /**
     * Запуск по расписанию уведомление по новым тендерам.
     */
    @Scheduled(cron = "${app.crone.notification}")
    public void sendTenderNotifications() {
        log.info("Начинаем запрос! Ищем новые тендеры!");
        List<TenderDto> tenders = tenderService.fetchTenders();
        log.info("Количество новых тендеров {}", tenders.size());
        List<User> users = userRepository.findAll();
        log.info("Количество пользователей {}", users.size());

        for (User user : users){
            if (!user.isAuthorized()) continue;
            List<TenderPreference> prefs = tenderPreferenceRepository.findByUser(user);
            List<TenderDto> matched = tenders.stream()
                    .filter(t -> prefs.stream().anyMatch(p -> t.getCategory().equalsIgnoreCase(p.getCategoryId())))
                    .toList();
            if (!matched.isEmpty()){

                for (TenderDto tenderDto : matched){
                    StringBuilder message = new StringBuilder();
                    message.append("<b>Наименование:</b>\n").append("<i>").append(tenderDto.getName()).append("</i>").append("\n")
                            .append("<b>Заказчик:</b>\n").append("<i>").append(tenderDto.getCustomerName()).append("</i>").append("\n")
                            .append("<b>IDNO:</b>\n").append("<i>").append(tenderDto.getCustomerId()).append("</i>").append("\n\n")
                            .append("<b>").append(tenderDto.getDate()).append("</b>").append("\n")
                            .append("<b>Ссылка на тендер:</b>\n").append("<a href=\"").append(tenderDto.getUrl()).append("\">Тендер</a>");
                    messageService.sendTextMessage(absSender, user.getChatId(), message.toString(), tenderDto.getId(), tenderDto.getCustomerId());
                }
            }
        }
    }

    /**
     * Уведомления по избранным тендерам. Запуск по расписанию.
     */
    @Scheduled(cron = "${app.crone.verify}")
    public void verifyTenderFavorites() {
        favoriteTenderService.checkForUpdatesAndNotify(absSender);
    }
}
