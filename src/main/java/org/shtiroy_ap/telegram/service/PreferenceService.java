package org.shtiroy_ap.telegram.service;

import org.shtiroy_ap.telegram.entity.Category;
import org.shtiroy_ap.telegram.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class PreferenceService {
    private final CategoryRepository categoryRepository;
    private final int CATEGORIES_PER_PAGE = 5;

    public PreferenceService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
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
}
