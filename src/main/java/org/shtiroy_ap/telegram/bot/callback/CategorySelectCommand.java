package org.shtiroy_ap.telegram.bot.callback;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy_ap.telegram.entity.TenderPreference;
import org.shtiroy_ap.telegram.entity.User;
import org.shtiroy_ap.telegram.repository.TenderPreferenceRepository;
import org.shtiroy_ap.telegram.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.shtiroy_ap.telegram.util.StringConstants.BOT_ERROR;

/**
 * Сервис обработки комадны selectCategory.
 * Добавляет категорию в избранное для отслеживания.
 */
@CallbackMapping("selectCategory")
@Component
public class CategorySelectCommand implements CallbackCommand{
    private final TenderPreferenceRepository tenderPreferenceRepository;
    private final UserRepository userRepository;
    private final Logger log = LogManager.getLogger(CategorySelectCommand.class.getName());

    public CategorySelectCommand(TenderPreferenceRepository tenderPreferenceRepository, UserRepository userRepository) {
        this.tenderPreferenceRepository = tenderPreferenceRepository;
        this.userRepository = userRepository;
    }

    /**
     * Класс CallbackCommandManager пересылает выполнения метода если поступила команда selectCategory.
     *
     * @param callbackQuery - callback от пользователя
     * @param sender - интерфейс для отправки сообщений
     * @param data - данные от пользователя
     */
    @Override
    public void execute(CallbackQuery callbackQuery, AbsSender sender, String data) {
        Long chatId = callbackQuery.getMessage().getChatId();
        User user = userRepository.findByChatId(chatId).orElse(null);
        tenderPreferenceRepository.save(new TenderPreference(data, user));
        try {
            sender.execute(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text("✅ Категория добавлена в ваши предпочтения.")
                    .build());
        } catch (TelegramApiException e) {
            log.error(BOT_ERROR, e.getMessage());
        }
    }
}
