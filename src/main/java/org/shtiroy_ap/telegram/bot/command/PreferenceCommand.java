package org.shtiroy_ap.telegram.bot.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy_ap.telegram.service.PreferenceService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.shtiroy_ap.telegram.util.StringConstants.BOT_ERROR;

/**
 * Обработка команды preference. Возвращает весь список кодов.
 */
@Component
public class PreferenceCommand implements BotCommand{

    private final PreferenceService preferenceService;
    private final Logger log = LogManager.getLogger(PreferenceCommand.class.getName());

    public PreferenceCommand(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @Override
    public String getName() {
        return "/preference";
    }

    @Override
    public String getDescription() {
        return "Настройка категорий уведомлений";
    }

    @Override
    public void execute(Update update, AbsSender sender) {
        Long chatId = update.getMessage().getChatId();
        SendMessage message = preferenceService.buildCategorySelectionMessage(chatId, 0, 0);
        try {
            sender.execute(message);
        } catch (TelegramApiException e) {
            log.error(BOT_ERROR, e.getMessage());
        }
    }
}
