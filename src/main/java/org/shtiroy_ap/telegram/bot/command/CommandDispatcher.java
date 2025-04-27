package org.shtiroy_ap.telegram.bot.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy_ap.telegram.service.AuthService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CommandDispatcher {
    private final Map<String, BotCommand> commandMap;
    private final AuthService authService;
    private final Logger log = LogManager.getLogger(CommandDispatcher.class.getName());


    public CommandDispatcher(List<BotCommand> commands, AuthService authService) {
        this.authService = authService;
        this.commandMap = commands.stream()
                .collect(Collectors.toMap(BotCommand::getName, Function.identity()));
    }

    public void dispatch(Update update, AbsSender sender) {
        Long chatId = update.getMessage().getChatId();
        String message = update.getMessage().getText();

        if (!authService.isAuthorized(chatId)) {
            authService.handleAuthorization(update, sender);
            return;
        }

        String commandKey = message.split(" ")[0];
        BotCommand command = commandMap.getOrDefault(commandKey, null);

        if (command != null) {
            command.execute(update, sender);
        } else {
            SendMessage error = new SendMessage(chatId.toString(), "Неизвестная команда.");
            try {
                sender.execute(error);
            } catch (TelegramApiException e) {
                log.error("Ошибка {}",e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
