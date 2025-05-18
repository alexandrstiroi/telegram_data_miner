package org.shtiroy_ap.telegram.bot.command;

import org.shtiroy_ap.telegram.service.PreferenceService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class PreferenceCommand implements BotCommand{

    private final PreferenceService preferenceService;

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
            e.printStackTrace();
        }
    }
}
