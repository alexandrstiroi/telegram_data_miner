package org.shtiroy_ap.telegram.bot.callback;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy_ap.telegram.service.FavoriteTenderService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.shtiroy_ap.telegram.util.StringConstants.BOT_ERROR;

/**
 * Сервис обрабатывает команду favoriteAdd привязывает тендер к пользователю.
 */
@CallbackMapping("favoriteAdd")
@Component
public class AddToFavoritesCallbackCommand implements CallbackCommand{
    private final FavoriteTenderService favoriteTenderService;
    private final Logger log = LogManager.getLogger(AddToFavoritesCallbackCommand.class.getName());

    /**
     * Конструктор сервиса.
     *
     * @param favoriteTenderService
     */
    public AddToFavoritesCallbackCommand(FavoriteTenderService favoriteTenderService) {
        this.favoriteTenderService = favoriteTenderService;
    }

    /**
     * Класс CallbackCommandManager пересылает выполнения метода если поступила команда favoriteAdd.
     * Добавляем тендер в избранное.
     *
     * @param callbackQuery - callback от пользователя
     * @param sender - интерфейс для отправки сообщений
     * @param data - данные от пользователя
     */
    @Override
    public void execute(CallbackQuery callbackQuery, AbsSender sender, String data) {
        Long chatId = callbackQuery.getFrom().getId();
        log.info("Поступил запрос на сохранение в избранное тендера {}, от пользователя {}", chatId, data);
        boolean added = favoriteTenderService.addFavorite(chatId, data);
        String text = added ? "✅ Тендер добавлен в избранное!" : "⚠️ Этот тендер уже в избранном.";
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            sender.execute(message);
        } catch (TelegramApiException e) {
            log.error(BOT_ERROR, e.getMessage());
        }
    }
}