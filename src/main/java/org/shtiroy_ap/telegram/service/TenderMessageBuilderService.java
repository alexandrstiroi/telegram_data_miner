package org.shtiroy_ap.telegram.service;

import org.shtiroy_ap.telegram.model.Lot;
import org.shtiroy_ap.telegram.model.LotItem;
import org.shtiroy_ap.telegram.model.LotSupplier;
import org.shtiroy_ap.telegram.model.TenderDetailDto;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class TenderMessageBuilderService {

    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,###.##");
    private static final int TELEGRAM_LIMIT = 4096;

    public String buildTenderMessage(TenderDetailDto tender) {
        StringBuilder message = new StringBuilder();

        message.append("<b>🏷 Название:</b>\n").append(escape(tender.getName())).append("\n");
        message.append("<b>🔗 Ссылка:</b> ").append("<a href=\"").append(tender.getUrls()).append("\">Открыть тендер</a>\n");
        message.append("<b>🧩 Категория:</b>\n").append(escape(tender.getCategory())).append(" - ").append(escape(tender.getCategoryName())).append("\n");
        message.append("<b>💵 Сумма:</b> ").append(MONEY_FORMAT.format(tender.getAmount())).append(" ").append(escape(tender.getCurrency())).append("\n");
        message.append("<b>🗓 Даты:</b>\n").append(escape(tender.getDate())).append("\n\n");

        if (tender.getLots() != null && !tender.getLots().isEmpty()) {
            message.append("<b>📦 Лоты:</b>\n");
            for (Lot lot : tender.getLots()) {
                message.append(buildLotMessage(lot));
            }
        }

        return message.toString();
    }

    private String buildLotMessage(Lot lot) {
        StringBuilder message = new StringBuilder();
        message.append("\n<b>🔹 ").append(escape(lot.getTitle())).append(":</b> ").append(escape(lot.getDescription())).append("\n");
        if (lot.getValue() != null) {
            message.append("<b>💰 Сумма:</b> ")
                    .append(MONEY_FORMAT.format(lot.getValue().getAmount()))
                    .append(" ")
                    .append(escape(lot.getValue().getCurrency()))
                    .append("\n");
        }
        message.append("<b>📋 Статус:</b> ").append(escape(lot.getStatus())).append("\n");

        if (lot.getLotSuppliers() != null && !lot.getLotSuppliers().isEmpty()) {
            for (LotSupplier supplier : lot.getLotSuppliers()) {
                if (supplier.getName() != null) {
                    message.append("<b>👥 Поставщик:</b> ").append(escape(supplier.getName()));
                    if (supplier.getValue() != null) {
                        message.append(" (")
                                .append(MONEY_FORMAT.format(supplier.getValue().getAmount()))
                                .append(" ")
                                .append(escape(supplier.getValue().getCurrency()))
                                .append(")");
                    }
                    message.append("\n");
                }
            }
        }

        if (lot.getLotItems() != null && !lot.getLotItems().isEmpty()) {
            message.append("<b>🛠 Товары:</b>\n");
            for (LotItem item : lot.getLotItems()) {
                message.append("- ").append(escape(item.getDescription())).append(" (").append(item.getCount()).append(" шт.)\n");
            }
        }

        return message.toString();
    }

    private String escape(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    public List<String> splitMessage(String message) {
        List<String> parts = new ArrayList<>();
        int length = message.length();
        for (int i = 0; i < length; i += TELEGRAM_LIMIT) {
            int end = Math.min(i + TELEGRAM_LIMIT, length);

            // Постараемся завершить часть на ближайшем \n перед пределом
            if (end < length) {
                int lastNewLine = message.lastIndexOf("\n", end);
                if (lastNewLine > i) {
                    end = lastNewLine;
                }
            }
            parts.add(message.substring(i, end));
            i = end - 1; // -1, потому что цикл ещё инкрементирует i
        }
        return parts;
    }
}
