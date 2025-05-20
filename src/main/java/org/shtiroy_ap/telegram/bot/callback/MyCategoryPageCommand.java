package org.shtiroy_ap.telegram.bot.callback;

import org.shtiroy_ap.telegram.service.PreferenceService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@CallbackMapping("myCategoryPage")
@Component
public class MyCategoryPageCommand implements CallbackCommand{

    private final PreferenceService preferenceService;

    public MyCategoryPageCommand(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @Override
    public void execute(CallbackQuery callbackQuery, AbsSender sender, String data) {
        Long chatId = callbackQuery.getMessage().getChatId();
        int page = Integer.parseInt(data);
        SendMessage message = preferenceService.buildMyAlertsMessage(chatId, page);
        try {
            sender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
