package org.shtiroy_ap.telegram.bot.callback;

import org.shtiroy_ap.telegram.service.PreferenceService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;

@CallbackMapping("categoryParent")
@Component
public class CategoryParentCommand implements CallbackCommand{
    private final PreferenceService preferenceService;

    public CategoryParentCommand(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @Override
    public void execute(CallbackQuery callbackQuery, AbsSender sender, String data) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String[] parts = data.split(" ");
        String parentStr = parts.length > 1 ? parts[1] : "";
        int parentId = Integer.parseInt(parentStr);
        try {
            SendMessage message = preferenceService.buildCategorySelectionMessage(chatId, 0, parentId);
            sender.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
