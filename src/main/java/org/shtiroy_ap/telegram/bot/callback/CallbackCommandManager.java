package org.shtiroy_ap.telegram.bot.callback;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CallbackCommandManager {
    private final Map<String, CallbackCommand> commandMap = new HashMap<>();
    private final Logger log = LogManager.getLogger(CallbackCommandManager.class.getName());

    public CallbackCommandManager(List<CallbackCommand> commands) {
        for (CallbackCommand command : commands) {
            CallbackMapping mapping = command.getClass().getAnnotation(CallbackMapping.class);
            if (mapping != null) {
                commandMap.put(mapping.value(), command);
            }
        }
    }

    public void handleCallback(CallbackQuery callbackQuery, AbsSender sender) {
        String[] parts = callbackQuery.getData().split("_", 2);
        String commandKey = parts[0];
        String data = parts.length > 1 ? parts[1] : "";
        CallbackCommand command = commandMap.get(commandKey);
        if (command != null) {
            command.execute(callbackQuery, sender, data);
        } else {
            log.info("Unknown callback command: {}", commandKey);
        }
    }
}
