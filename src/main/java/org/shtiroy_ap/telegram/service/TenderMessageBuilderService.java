package org.shtiroy_ap.telegram.service;

import org.shtiroy_ap.telegram.model.*;
import org.shtiroy_ap.telegram.util.DateUtil;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TenderMessageBuilderService {

    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,###.##");
    private static final int TELEGRAM_LIMIT = 4096;
    private static final int PHOTO_CAPTION_LIMIT = 1024;

    public String buildTenderMessage(TenderDetailDto tender) {
        StringBuilder message = new StringBuilder();

        message.append("<b>🏷 Название:</b>\n").append(escape(tender.getName())).append("\n");
        message.append("<b>🔗 Ссылка:</b> ").append("<a href=\"").append(tender.getUrls()).append("\">Открыть тендер</a>\n");
        message.append("<b>🧩 Категория:</b>\n").append(escape(tender.getCategory())).append(" - ").append(escape(tender.getCategoryName())).append("\n");
        message.append("<b>💵 Сумма:</b> ").append(MONEY_FORMAT.format(tender.getAmount())).append(" ").append(escape(tender.getCurrency())).append("\n");
        message.append("<b>🗓 Даты:</b>\n").append(getDataInfo(tender.getPeriod(), tender.getAuctionPeriod())).append("\n\n");

        if (tender.getLots() != null && !tender.getLots().isEmpty()) {
            message.append("<b>📦 Лоты:</b>\n");
            for (Lot lot : tender.getLots()) {
                message.append(buildLotMessage(lot));
            }
        }
        if (tender.getPeriod().getEnquiries() != null && !tender.getPeriod().getEnquiries().isEmpty()){
            message.append("<b>Разъяснения:</b>\n");
            for (Enquiry enquiry: tender.getPeriod().getEnquiries()){
                message.append(buildEnqMessage(enquiry));
            }
        }
        if (tender.getDocuments() != null && !tender.getDocuments().isEmpty()) {
            message.append("<b>\uD83D\uDCC4 Документы:</b>\n");
            for (Document document : tender.getDocuments()){
                message.append(buildDocMessage(document));
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

    private String getDataInfo(Period period, LocalDateTime auction){
        StringBuilder sb = new StringBuilder();
        if (period != null && period.getEnquiryPeriod() != null){
            sb.append("Период разъяснений: c ").append(DateUtil.dateTimeToStr(period.getEnquiryPeriod().getStartDate()))
                    .append(" по ").append(DateUtil.dateTimeToStr(period.getEnquiryPeriod().getEndDate()))
                    .append("\n");
        }
        if (period != null && period.getTenderPeriod() != null){
            sb.append("Подача предложений: с ").append(DateUtil.dateTimeToStr(period.getTenderPeriod().getStartDate()))
                    .append(" по ").append(DateUtil.dateTimeToStr(period.getTenderPeriod().getEndDate()))
                    .append("\n");
        }
        if (auction != null){
            sb.append("Аукцион: ").append(DateUtil.dateTimeToStr(auction)).append("\n");
        }
        return sb.toString();
    }

    private String buildDocMessage(Document document){
        return "\n<b>🔹 " + escape(document.getTitle()) + "</b> " +
                escape(document.getDescription());
    }

    private String buildEnqMessage(Enquiry enquiry){
        return "\n<b>🔹 Название вопроса: " + escape(enquiry.getTitle()) + "</b> " +
                escape(enquiry.getDescription()) + "\n " +
                escape(enquiry.getAnswer());
    }

    /**
     * Делит текст по строкам, обеспечивая, чтобы первая часть шла в caption (1024 символа),
     * а оставшиеся — в SendMessage (4096 символов). Разделение по \n.
     *
     * @param text исходный текст
     * @return список частей сообщения
     */
    public static List<String> splitMessageByLines(String text) {
        List<String> result = new ArrayList<>();

        if (text == null || text.isEmpty()) {
            return result;
        }

        String[] lines = text.split("\n");
        StringBuilder current = new StringBuilder();
        int currentLimit = PHOTO_CAPTION_LIMIT;

        for (String line : lines) {
            // +1 потому что строка будет добавляться с символом новой строки
            int projectedLength = current.length() + line.length() + 1;

            if (projectedLength > currentLimit) {
                // Сохраняем накопленное
                result.add(current.toString().trim());
                current = new StringBuilder();
                currentLimit = TELEGRAM_LIMIT; // После caption идут большие блоки
            }

            current.append(line).append("\n");
        }

        // Добавим то, что осталось
        if (current.length() > 0) {
            result.add(current.toString().trim());
        }

        return result;
    }
}
