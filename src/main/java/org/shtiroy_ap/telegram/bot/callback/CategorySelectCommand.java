package org.shtiroy_ap.telegram.bot.callback;

import org.shtiroy_ap.telegram.entity.TenderPreference;
import org.shtiroy_ap.telegram.entity.User;
import org.shtiroy_ap.telegram.repository.TenderPreferenceRepository;
import org.shtiroy_ap.telegram.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@CallbackMapping("selectCategory")
@Component
public class CategorySelectCommand implements CallbackCommand{
    private final TenderPreferenceRepository tenderPreferenceRepository;
    private final UserRepository userRepository;

    public CategorySelectCommand(TenderPreferenceRepository tenderPreferenceRepository, UserRepository userRepository) {
        this.tenderPreferenceRepository = tenderPreferenceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(CallbackQuery callbackQuery, AbsSender sender, String data) {
        Long chatId = callbackQuery.getMessage().getChatId();
        User user = userRepository.findByChatId(chatId).orElse(null);
        //Integer categoryId = Integer.parseInt(data);
        tenderPreferenceRepository.save(new TenderPreference(data, user));
        try {
            sender.execute(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text("✅ Категория добавлена в ваши предпочтения.")
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
