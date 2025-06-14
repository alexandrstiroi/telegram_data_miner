package org.shtiroy_ap.telegram.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.List;

import static org.shtiroy_ap.telegram.util.StringConstants.BOT_ERROR;

@Service
public class MessageService {
    private final Logger log = LogManager.getLogger(MessageService.class.getName());

    /**
     * Отправка простого текстового сообщения
     */
    public void sendTextMessage(AbsSender sender, Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        message.setParseMode("HTML"); // Позволяет использовать <b>, <i>, <a> и т.д.
        send(sender, message);
    }

    /**
     * Отправка изображения с подписью
     */
    public void sendPhotoMessage(AbsSender sender, Long chatId, String caption, Integer tenderId, String customerId) {
        try {
            ClassPathResource imgResource = new ClassPathResource("images/tender_new.png");
            SendPhoto photo = new SendPhoto();
            photo.setChatId(chatId.toString());
            photo.setPhoto(new InputFile(imgResource.getInputStream(), "tender_new.png"));
            photo.setCaption(caption);
            photo.setParseMode("HTML");

            // Создание кнопок
            InlineKeyboardButton detailsButton = new InlineKeyboardButton();
            detailsButton.setText("📄 Подробнее о тендере (" + tenderId + ")");
            detailsButton.setCallbackData("DETAILS_" + tenderId); // потом обработаем это через CallbackQuery

            InlineKeyboardButton analyzeButton = new InlineKeyboardButton();
            analyzeButton.setText("🔍 Анализ заказчика");
            analyzeButton.setCallbackData("ANALYZE_" + customerId);

            // Добавляем кнопки в разметку
            List<InlineKeyboardButton> keyboardRow = List.of(detailsButton, analyzeButton);
            List<List<InlineKeyboardButton>> keyboard = List.of(keyboardRow);

            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            markup.setKeyboard(keyboard);

            photo.setReplyMarkup(markup);

            sender.execute(photo);
        } catch (TelegramApiException | IOException exception) {
            log.error(BOT_ERROR, exception.getMessage());
        }
    }

    /**
     * Отправка простого текстового сообщения с нопками
     */
    public void sendTextMessage(AbsSender sender, Long chatId, String messageText, Integer tenderId, String customerId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(messageText);
        message.setParseMode("HTML");

        // Создание кнопок
        InlineKeyboardButton detailsButton = new InlineKeyboardButton();
        detailsButton.setText("📄 Подробнее о тендере (" + tenderId + ")");
        detailsButton.setCallbackData("DETAILS_" + tenderId); // потом обработаем это через CallbackQuery

        InlineKeyboardButton analyzeButton = new InlineKeyboardButton();
        analyzeButton.setText("🔍 Анализ заказчика");
        analyzeButton.setCallbackData("ANALYZE_" + customerId);

        // Добавляем кнопки в разметку
        List<InlineKeyboardButton> keyboardRow = List.of(detailsButton, analyzeButton);
        List<List<InlineKeyboardButton>> keyboard = List.of(keyboardRow);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);

        message.setReplyMarkup(markup);
        send(sender, message);
    }

    private void send(AbsSender sender, BotApiMethodMessage message){
        try {
            sender.execute(message);
        } catch (TelegramApiException e) {
            log.error(BOT_ERROR, e.getMessage());
        }
    }
}
