package org.shtiroy_ap.telegram.bot.callback;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy_ap.telegram.service.PreferenceService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.shtiroy_ap.telegram.util.StringConstants.BOT_ERROR;

/**
 * Сервис обработки комадны myCategoryPage.
 * Поресылает следующие 5 категорий из избранных.
 */
@CallbackMapping("myCategoryPage")
@Component
public class MyCategoryPageCommand implements CallbackCommand{

    private final PreferenceService preferenceService;
    private final Logger log = LogManager.getLogger(MyCategoryPageCommand.class.getName());

    public MyCategoryPageCommand(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    /**
     * Класс CallbackCommandManager пересылает выполнения метода если поступила команда myCategoryPage.
     *
     * @param callbackQuery - callback от пользователя
     * @param sender - интерфейс для отправки сообщений
     * @param data - данные от пользователя
     */
    @Override
    public void execute(CallbackQuery callbackQuery, AbsSender sender, String data) {
        Long chatId = callbackQuery.getMessage().getChatId();
        int page = Integer.parseInt(data);
        SendMessage message = preferenceService.buildMyAlertsMessage(chatId, page);
        try {
            sender.execute(message);
        } catch (TelegramApiException e) {
            log.error(BOT_ERROR, e.getMessage());
        }
    }
}
