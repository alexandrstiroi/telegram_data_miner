package org.shtiroy_ap.telegram.bot.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Интерфейс команд.
 */
public interface BotCommand {
    String getName();
    String getDescription();
    void execute(Update update, AbsSender sender);
}
