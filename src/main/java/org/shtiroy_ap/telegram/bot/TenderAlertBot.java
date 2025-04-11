package org.shtiroy_ap.telegram.bot;

import lombok.extern.slf4j.Slf4j;
import org.shtiroy_ap.telegram.bot.commands.CommandsHandler;
import org.shtiroy_ap.telegram.config.BotProperties;
import org.shtiroy_ap.telegram.service.LogService;
import org.shtiroy_ap.telegram.util.Consts;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class TenderAlertBot extends TelegramLongPollingBot {
    public final BotProperties botProperties;
    public final CommandsHandler commandsHandler;
    private final LogService logService;

    public TenderAlertBot(BotProperties botProperties, CommandsHandler commandsHandler, LogService logService){
        super(botProperties.getToken());
        this.botProperties = botProperties;
        log.info("token {}, name {}", botProperties.getToken(), botProperties.getName());
        this.commandsHandler = commandsHandler;
        this.logService = logService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            logService.insertLog(update);
            String messageTest = update.getMessage().getText();
            String chartId = update.getMessage().getChatId().toString();
            log.info("message {}, chartId {}", messageTest, chartId);
            if (update.getMessage().getText().startsWith("/")){
                sendMessage(commandsHandler.handleCommands(update));
            } else {
                sendMessage(new SendMessage(chartId, Consts.CANT_UNDERSTAND));
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }

    private void sendMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
