package org.shtiroy_ap.telegram.bot.callback;

import org.shtiroy_ap.telegram.service.MessageService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Сервис для анализа заказчика по тендеру.
 */
@Service
@CallbackMapping("ANALYZE")
public class AnalyzeCustomerCommand implements CallbackCommand {
    private final MessageService messageService;

    public AnalyzeCustomerCommand(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void execute(CallbackQuery callbackQuery, AbsSender sender, String data) {
        messageService.sendTextMessage(sender, callbackQuery.getMessage().getChatId(), "🔍 Анализ заказчика по тендеру №" + data);
    }
}
