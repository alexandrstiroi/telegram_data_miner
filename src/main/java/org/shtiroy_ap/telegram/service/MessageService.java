package org.shtiroy_ap.telegram.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy_ap.telegram.bot.TelegramBot;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

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
        try {
            sender.execute(message);
        } catch (TelegramApiException e) {
            log.error("Не получилось выслать сообщение {}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Отправка изображения с подписью
     */
    public void sendPhotoMessage(AbsSender sender, Long chatId, String photoUrl, String caption) {
        SendPhoto photo = new SendPhoto();
        photo.setChatId(chatId.toString());
        photo.setPhoto(new InputFile(photoUrl));
        photo.setCaption(caption);
        photo.setParseMode("HTML");
        try {
            sender.execute(photo);
        } catch (TelegramApiException e) {
            log.error("Не получилось выслать сообщение {}", e.getMessage());
            e.printStackTrace();
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

        try {
            sender.execute(message);
        } catch (TelegramApiException e) {
            log.error("Не получилось выслать сообщение {}", e.getMessage());
            e.printStackTrace();
        }
    }
}
