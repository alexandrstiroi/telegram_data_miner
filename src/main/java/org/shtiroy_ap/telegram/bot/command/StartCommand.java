package org.shtiroy_ap.telegram.bot.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class StartCommand implements BotCommand{
    @Override
    public String getName() {
        return "/start";
    }

    @Override
    public String getDescription() {
        return "Начать работу с ботом";
    }

    @Override
    public void execute(Update update, AbsSender sender) {
        Long chatId = update.getMessage().getChatId();
        String name = update.getMessage().getFrom().getFirstName();
        String text = "Привет, " + name + "! 👋 Добро пожаловать в бот.";
        try {
            sender.execute(new SendMessage(chatId.toString(), text));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
