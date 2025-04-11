package org.shtiroy_ap.telegram.bot.commands;

import lombok.RequiredArgsConstructor;
import org.shtiroy_ap.telegram.util.Consts;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Component
public class HelpCommand implements Command {
    @Override
    public SendMessage apply(Update update) {
        long chartId =update.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chartId);
        sendMessage.setText(Consts.HELP_COMMAND);
        return sendMessage;
    }
}
