package org.shtiroy_ap.telegram.bot.commands;

import lombok.extern.slf4j.Slf4j;
import org.shtiroy_ap.telegram.util.Consts;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Component
@Slf4j
public class CommandsHandler {
    private final Map<String, Command> commands;

    public CommandsHandler(StartCommand startCommand, HelpCommand helpCommand, ActivateCommand activateCommand){
        this.commands = Map.of("/start", startCommand,
                "/help",helpCommand,
                "/activate", activateCommand);
    }

    public SendMessage handleCommands(Update update){
        String messageText = update.getMessage().getText();
        String command = messageText.split(" ")[0];
        long chatId = update.getMessage().getChatId();

        var commandHandler = commands.get(command);
        if (commandHandler != null) {
            return  commandHandler.apply(update);
        } else {
            return new SendMessage(String.valueOf(chatId), Consts.UNKNOWN_COMMAND);
        }
    }
}
