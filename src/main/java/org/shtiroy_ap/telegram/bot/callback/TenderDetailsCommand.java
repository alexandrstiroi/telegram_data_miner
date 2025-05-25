package org.shtiroy_ap.telegram.bot.callback;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy_ap.telegram.model.TenderDetailDto;
import org.shtiroy_ap.telegram.service.TenderMessageBuilderService;
import org.shtiroy_ap.telegram.service.TenderService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static org.shtiroy_ap.telegram.util.StringConstants.BOT_ERROR;

/**
 * Сервис обработки комадны DETAILS.
 * Запрос всех деталей по тендеру.
 */
@Service
@CallbackMapping("DETAILS")
public class TenderDetailsCommand implements CallbackCommand {
    private final TenderService tenderService;
    private final TenderMessageBuilderService tenderMessageBuilderService;
    private final Logger log = LogManager.getLogger(TenderDetailsCommand.class.getName());

    public TenderDetailsCommand(TenderService tenderService, TenderMessageBuilderService tenderMessageBuilderService) {
        this.tenderService = tenderService;
        this.tenderMessageBuilderService = tenderMessageBuilderService;
    }

    /**
     * Класс CallbackCommandManager пересылает выполнения метода если поступила команда categoryPage.
     *
     * @param callbackQuery - callback от пользователя
     * @param sender - интерфейс для отправки сообщений
     * @param data - данные от пользователя
     */
    @Override
    public void execute(CallbackQuery callbackQuery, AbsSender sender, String data) {
        TenderDetailDto dto = tenderService.fetchTenderDetail(data);
        String htmlMessage = tenderMessageBuilderService.buildTenderMessage(dto);
        // Создание кнопок
        InlineKeyboardButton detailsButton = new InlineKeyboardButton();
        detailsButton.setText("⭐ В избранное (" + data + ")");
        detailsButton.setCallbackData("favoriteAdd_" + data); // потом обработаем это через CallbackQuery

        // Добавляем кнопки в разметку
        List<InlineKeyboardButton> keyboardRow = List.of(detailsButton);
        List<List<InlineKeyboardButton>> keyboard = List.of(keyboardRow);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        SendMessage message = null;
        try{
            if (htmlMessage.length() < 4096) {
                message = SendMessage.builder()
                    .chatId(callbackQuery.getMessage().getChatId())
                    .text(htmlMessage)
                    .parseMode("HTML")
                    .replyMarkup(markup)
                    .build();
                sender.execute(message);
            } else {
                List<String> messageParts = tenderMessageBuilderService.splitMessage(htmlMessage);
                for (int i = 0; i <= messageParts.size() - 1; i++) {
                    if (i == messageParts.size() - 1) {
                        message = SendMessage.builder()
                            .chatId(callbackQuery.getMessage().getChatId())
                            .text(messageParts.get(i))
                            .parseMode("HTML")
                            .replyMarkup(markup)
                            .build();
                    } else {
                        message = SendMessage.builder()
                            .chatId(callbackQuery.getMessage().getChatId())
                            .text(messageParts.get(i))
                            .parseMode("HTML")
                            .build();
                    }
                    sender.execute(message);
                }
            }
        } catch (TelegramApiException ex){
            log.error(BOT_ERROR, ex.getMessage());
        }
    }
}
