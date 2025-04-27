package org.shtiroy_ap.telegram.bot.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;

public interface CallbackCommand {
    void execute(CallbackQuery callbackQuery, AbsSender sender, String data);
}
