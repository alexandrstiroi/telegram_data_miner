package org.shtiroy_ap.telegram.bot.callback;

import org.shtiroy_ap.telegram.entity.User;
import org.shtiroy_ap.telegram.repository.TenderPreferenceRepository;
import org.shtiroy_ap.telegram.repository.UserRepository;
import org.shtiroy_ap.telegram.service.PreferenceService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@CallbackMapping("categoryDelete")
@Component
public class CategoryDeleteCommand implements CallbackCommand{

    private final TenderPreferenceRepository tenderPreferenceRepository;
    private final UserRepository userRepository;
    private final PreferenceService preferenceService;

    public CategoryDeleteCommand(TenderPreferenceRepository tenderPreferenceRepository, UserRepository userRepository,
                                 PreferenceService preferenceService) {
        this.tenderPreferenceRepository = tenderPreferenceRepository;
        this.userRepository = userRepository;
        this.preferenceService = preferenceService;
    }

    @Override
    public void execute(CallbackQuery callbackQuery, AbsSender sender, String data) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String[] parts = data.split(" ");
        int page = Integer.parseInt(parts[0]);
        String code = parts.length > 1 ? parts[1] : "";
        User user = userRepository.findByChatId(chatId).get();
        tenderPreferenceRepository.deleteByUserAndCategoryId(user, code);
        try {
            SendMessage message = preferenceService.buildMyAlertsMessage(chatId, page);
            sender.execute(message);
            sender.execute(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text("✅ Категория удалена из ваших предпочтений.")
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
