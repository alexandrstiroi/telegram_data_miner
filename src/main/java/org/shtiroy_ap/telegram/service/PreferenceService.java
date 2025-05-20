package org.shtiroy_ap.telegram.service;

import org.shtiroy_ap.telegram.entity.Category;
import org.shtiroy_ap.telegram.entity.TenderPreference;
import org.shtiroy_ap.telegram.entity.User;
import org.shtiroy_ap.telegram.repository.CategoryRepository;
import org.shtiroy_ap.telegram.repository.TenderPreferenceRepository;
import org.shtiroy_ap.telegram.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PreferenceService {
    private final CategoryRepository categoryRepository;
    private final TenderPreferenceRepository tenderPreferenceRepository;
    private final UserRepository userRepository;
    private final int CATEGORIES_PER_PAGE = 5;

    public PreferenceService(CategoryRepository categoryRepository, TenderPreferenceRepository tenderPreferenceRepository,
                             UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.tenderPreferenceRepository = tenderPreferenceRepository;
        this.userRepository = userRepository;
    }

    public SendMessage buildCategorySelectionMessage(Long chatId, int page, int parentId) {
        List<Category> categories = categoryRepository.findByParentId(parentId);
        int totalPages = (int) Math.ceil((double) categories.size() / CATEGORIES_PER_PAGE);
        page = Math.max(0, Math.min(page, totalPages - 1));

        int start = page * CATEGORIES_PER_PAGE;
        int end = Math.min(start + CATEGORIES_PER_PAGE, categories.size());
        List<Category> pageCategories = categories.subList(start, end);

        StringBuilder textBuilder = new StringBuilder("<b>Выберите категорию для отслеживания:</b>\n\n");
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (int i = 0; i < pageCategories.size(); i++) {
            Category category = pageCategories.get(i);
            int index = i + 1;
            textBuilder.append(index).append("️⃣ <b>")
                    .append(category.getCode()).append("</b> — ")
                    .append(category.getCategoryName()).append("\n");

            InlineKeyboardButton selectBtn = new InlineKeyboardButton("✅" + " В избранное");
            selectBtn.setCallbackData("selectCategory_" + category.getCode());
            InlineKeyboardButton childrenBtn = new InlineKeyboardButton("🔽 " + category.getCode());
            childrenBtn.setCallbackData("categoryParent_" + page + " " + category.getId());
            keyboard.add(List.of(childrenBtn, selectBtn));
        }

        // Пагинация
        List<InlineKeyboardButton> navRow = new ArrayList<>();
        if (page > 0) {
            InlineKeyboardButton back = new InlineKeyboardButton("⬅️ Назад");
            back.setCallbackData("categoryPage_" + (page - 1)+" "+parentId);
            navRow.add(back);
        }
        if (page < totalPages - 1) {
            InlineKeyboardButton next = new InlineKeyboardButton("Вперёд ➡️");
            next.setCallbackData("categoryPage_" + (page + 1)+" "+parentId);
            navRow.add(next);
        }
        if (!navRow.isEmpty()) {
            keyboard.add(navRow);
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);

        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(textBuilder.toString())
                .parseMode("HTML")
                .replyMarkup(markup)
                .build();
    }

    public SendMessage buildMyAlertsMessage(Long chatId, int page){
        User user = userRepository.findByChatId(chatId).get();
        List<String> codes = tenderPreferenceRepository.findByUser(user).stream()
                .map(TenderPreference::getCategoryId)
                .toList();
        List<Category> categories = codes.stream()
                        .map(categoryRepository::findByCode)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList();
        int totalPages = (int) Math.ceil((double) categories.size() / CATEGORIES_PER_PAGE);
        page = Math.max(0, Math.min(page, totalPages - 1));

        int start = page * CATEGORIES_PER_PAGE;
        int end = Math.min(start + CATEGORIES_PER_PAGE, categories.size());
        List<Category> pageCategories = categories.subList(start, end);

        StringBuilder textBuilder = new StringBuilder("<b>Список кодов отслеживания:</b>\n\n");
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (int i = 0; i < pageCategories.size(); i++) {
            Category category = pageCategories.get(i);
            int index = i + 1;
            textBuilder.append(index).append("️⃣ <b>")
                    .append(category.getCode()).append("</b> — ")
                    .append(category.getCategoryName()).append("\n");

            InlineKeyboardButton deleteBtn = new InlineKeyboardButton("\uD83D\uDDD1\uFE0F Удалить - " + category.getCode());
            deleteBtn.setCallbackData("categoryDelete_" + page +" " + category.getCode());
            keyboard.add(Collections.singletonList(deleteBtn));
        }

        // Пагинация
        List<InlineKeyboardButton> navRow = new ArrayList<>();
        if (page > 0) {
            InlineKeyboardButton back = new InlineKeyboardButton("⬅️ Назад");
            back.setCallbackData("myCategoryPage_" + (page - 1));
            navRow.add(back);
        }
        if (page < totalPages - 1) {
            InlineKeyboardButton next = new InlineKeyboardButton("Вперёд ➡️");
            next.setCallbackData("myCategoryPage_" + (page + 1));
            navRow.add(next);
        }
        if (!navRow.isEmpty()) {
            keyboard.add(navRow);
        }

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);

        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(textBuilder.toString())
                .parseMode("HTML")
                .replyMarkup(markup)
                .build();
    }
}
