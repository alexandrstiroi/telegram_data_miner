package org.shtiroy_ap.telegram.bot.callback;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy_ap.telegram.model.Document;
import org.shtiroy_ap.telegram.service.DocumentArchiveService;
import org.shtiroy_ap.telegram.service.TenderService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import static org.shtiroy_ap.telegram.util.StringConstants.BOT_ERROR;

@CallbackMapping("downloadZip")
@Component
public class DownloadZipCommand implements CallbackCommand{
    private final DocumentArchiveService documentArchiveService;
    private final TenderService tenderService;
    private final Logger log = LogManager.getLogger(DownloadZipCommand.class.getName());

    public DownloadZipCommand(DocumentArchiveService documentArchiveService, TenderService tenderService){
        this.documentArchiveService = documentArchiveService;
        this.tenderService = tenderService;
    }

    @Override
    public void execute(CallbackQuery callbackQuery, AbsSender sender, String data) {
        Long chatId = callbackQuery.getMessage().getChatId();
        List<Document> documentList = tenderService.fetchDocumentList(data);
        try {
            File file = documentArchiveService.downloadAndZipDocuments(documentList);
            InputFile inputFile = new InputFile(file, data+".zip");

            SendDocument send = SendDocument.builder()
                    .chatId(chatId.toString())
                    .document(inputFile)
                    .caption("📦 Архив с документами")
                    .build();
            sender.execute(send);
            documentArchiveService.deleteFile(file);
        } catch (IOException ex){
            log.error("Ошибка скачивания файлов {}", ex.getMessage());
        } catch (TelegramApiException ex){
            log.error(BOT_ERROR, ex.getMessage());
        }
    }
}
