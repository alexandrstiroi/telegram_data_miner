package org.shtiroy_ap.telegram.bot;

import org.shtiroy_ap.telegram.bot.callback.CallbackCommandManager;
import org.shtiroy_ap.telegram.config.BotConfig;
import org.shtiroy_ap.telegram.bot.command.CommandDispatcher;
import org.shtiroy_ap.telegram.service.LogService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;
    private final CommandDispatcher dispatcher;
    private final CallbackCommandManager callbackManager;
    private final LogService logService;

    public TelegramBot(BotConfig config, CommandDispatcher dispatcher, CallbackCommandManager callbackManager,
                       LogService logService) {
        super(config.getToken());
        this.config = config;
        this.dispatcher = dispatcher;
        this.callbackManager = callbackManager;
        this.logService = logService;
    }

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            logService.insertLog(update);
            dispatcher.dispatch(update, this);
        } else if (update.hasCallbackQuery()) {
            logService.insertLog(update.getCallbackQuery());
            callbackManager.handleCallback(update.getCallbackQuery(), this);
        }
    }
}
