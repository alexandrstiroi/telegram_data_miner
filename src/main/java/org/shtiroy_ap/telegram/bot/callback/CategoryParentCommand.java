package org.shtiroy_ap.telegram.bot.callback;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy_ap.telegram.service.PreferenceService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static org.shtiroy_ap.telegram.util.StringConstants.BOT_ERROR;

/**
 * Сервис обработки комадны categoryParent.
 * Пересылает дочерние категории.
 */
@CallbackMapping("categoryParent")
@Component
public class CategoryParentCommand implements CallbackCommand{
    private final PreferenceService preferenceService;
    private final Logger log = LogManager.getLogger(CategoryParentCommand.class.getName());

    public CategoryParentCommand(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    /**
     * Класс CallbackCommandManager пересылает выполнения метода если поступила команда categoryParent.
     *
     * @param callbackQuery - callback от пользователя
     * @param sender - интерфейс для отправки сообщений
     * @param data - данные от пользователя
     */
    @Override
    public void execute(CallbackQuery callbackQuery, AbsSender sender, String data) {
        Long chatId = callbackQuery.getMessage().getChatId();
        log.info("Поступил запрос на получение категорий {}, от пользователя {}", data, chatId);
        String[] parts = data.split(" ");
        String parentStr = parts.length > 1 ? parts[1] : "";
        int parentId = Integer.parseInt(parentStr);
        try {
            SendMessage message = preferenceService.buildCategorySelectionMessage(chatId, 0, parentId);
            sender.execute(message);
        } catch (Exception e) {
            log.error(BOT_ERROR, e.getMessage());
        }
    }
}
