package org.shtiroy_ap.telegram.bot.callback;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy_ap.telegram.model.Document;
import org.shtiroy_ap.telegram.service.DocumentDownloadService;
import org.shtiroy_ap.telegram.service.TenderService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.List;

@CallbackMapping("downloadDocs")
@Component
public class DownloadDocumentsCommand implements CallbackCommand{
    private final DocumentDownloadService documentDownloadService;
    private final TenderService tenderService;
    private final Logger log = LogManager.getLogger(DownloadDocumentsCommand.class.getName());

    public DownloadDocumentsCommand(DocumentDownloadService documentDownloadService,
                                    TenderService tenderService){
        this.documentDownloadService = documentDownloadService;
        this.tenderService = tenderService;
    }

    @Override
    public void execute(CallbackQuery callbackQuery, AbsSender sender, String data) {
        Long chatId = callbackQuery.getMessage().getChatId();
        List<Document> documentList = tenderService.fetchDocumentList(data);
        StringBuilder docsLink = new StringBuilder("📂 Доступные документы:\n\n");
        for (Document doc : documentList) {
            try {
                File file = documentDownloadService.downloadToFile(doc);
                InputFile inputFile = new InputFile(file, doc.getTitle());

                SendDocument send = SendDocument.builder()
                        .chatId(chatId.toString())
                        .document(inputFile)
                        .caption(doc.getTitle())
                        .build();
                sender.execute(send);
                documentDownloadService.deleteFile(file);
            } catch (Exception e) {
                log.error("Ошибка при обработке файла {}: {}", doc.getTitle(), e.getMessage());
                //SendMessage message = SendMessage.builder()
                //        .chatId(chatId)
                //        .text("⚠️ Ошибка при отправке файла: " + doc.getTitle())
                //        .build();
                //sender.execute(message);
            }
        }
    }
}
