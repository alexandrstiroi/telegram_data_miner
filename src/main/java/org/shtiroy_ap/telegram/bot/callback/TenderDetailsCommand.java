package org.shtiroy_ap.telegram.bot.callback;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy_ap.telegram.model.TenderDetailDto;
import org.shtiroy_ap.telegram.service.MessageService;
import org.shtiroy_ap.telegram.service.TenderMessageBuilderService;
import org.shtiroy_ap.telegram.service.TenderService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Service
@CallbackMapping("DETAILS")
public class TenderDetailsCommand implements CallbackCommand {
    private final MessageService messageService;
    private final TenderService tenderService;
    private TenderMessageBuilderService tenderMessageBuilderService;
    private final Logger log = LogManager.getLogger(TenderDetailsCommand.class.getName());

    public TenderDetailsCommand(MessageService messageService, TenderService tenderService,
                                TenderMessageBuilderService tenderMessageBuilderService) {
        this.messageService = messageService;
        this.tenderService = tenderService;
        this.tenderMessageBuilderService = tenderMessageBuilderService;
    }

    @Override
    public void execute(CallbackQuery callbackQuery, AbsSender sender, String data) {
        TenderDetailDto dto = tenderService.fetchTenderDetail(data);
        String htmlMessage = tenderMessageBuilderService.buildTenderMessage(dto);
        if (htmlMessage.length() < 4096) {
            messageService.sendTextMessage(sender, callbackQuery.getMessage().getChatId(), htmlMessage);
        } else {
            List<String> messageParts = tenderMessageBuilderService.splitMessage(htmlMessage);
            for (String part : messageParts) {
                messageService.sendTextMessage(sender, callbackQuery.getMessage().getChatId(), part);
            }

        }
    }
}
