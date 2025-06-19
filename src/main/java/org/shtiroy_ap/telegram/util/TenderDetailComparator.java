package org.shtiroy_ap.telegram.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy_ap.telegram.model.Document;
import org.shtiroy_ap.telegram.model.Enquiry;
import org.shtiroy_ap.telegram.model.EnquiryPeriod;
import org.shtiroy_ap.telegram.model.Lot;
import org.shtiroy_ap.telegram.model.LotItem;
import org.shtiroy_ap.telegram.model.LotSupplier;
import org.shtiroy_ap.telegram.model.Period;
import org.shtiroy_ap.telegram.model.TenderDetailDto;
import org.shtiroy_ap.telegram.model.TenderPeriod;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TenderDetailComparator {
    private static final Logger log = LogManager.getLogger(TenderDetailComparator.class.getName());

    public static List<String> compareTenderDetails(TenderDetailDto oldDetail, TenderDetailDto newDetail) {
        List<String> changes = new ArrayList<>();

        if (oldDetail == null || newDetail == null) {
            log.error("Один из объектов null");
            return Collections.emptyList();
        }

        // Compare basic fields
        compareField(changes, "\uD83C\uDFF7 <b>Наименование</b>", oldDetail.getName(), newDetail.getName());
        compareField(changes, "\uD83E\uDDE9 <b>Категория</b>", oldDetail.getCategory(), newDetail.getCategory());
        compareField(changes, "\uD83D\uDCB5 <b>Наименование категории</b>", oldDetail.getCategoryName(), newDetail.getCategoryName());
        compareField(changes, "\uD83D\uDDD3 <b>Дата</b>", oldDetail.getDate(), newDetail.getDate());
        compareField(changes, "\uD83D\uDD50 <b>Статус</b>", oldDetail.getStatus(), newDetail.getStatus());
        compareField(changes, "\uD83D\uDD50 <b>Детали статуса</b>", oldDetail.getStatusDetails(), newDetail.getStatusDetails());

        // Compare amount with precision
        if (oldDetail.getAmount() != null && newDetail.getAmount() != null) {
            if (oldDetail.getAmount().compareTo(newDetail.getAmount()) != 0) {
                changes.add(String.format("\uD83D\uDCB0 <b>Сумма</b> изменилась с <i>%s %s</i> на <i>%s %s</i>",
                        oldDetail.getAmount(), oldDetail.getCurrency(),
                        newDetail.getAmount(), newDetail.getCurrency()));
            }
        } else if (oldDetail.getAmount() != newDetail.getAmount()) {
            compareField(changes, "\uD83D\uDCB0 <b>Сумма</b>",
                    oldDetail.getAmount() != null ? oldDetail.getAmount().toString() : null,
                    newDetail.getAmount() != null ? newDetail.getAmount().toString() : null);
        }

        // Compare currency if amount hasn't changed
        if (oldDetail.getAmount() == null || newDetail.getAmount() == null ||
                oldDetail.getAmount().compareTo(newDetail.getAmount()) == 0) {
            compareField(changes, "<b>Валюта</b>", oldDetail.getCurrency(), newDetail.getCurrency());
        }

        // Compare auction period
        compareLocalDateTime(changes, "<b>Аукцион</b>", oldDetail.getAuctionPeriod(), newDetail.getAuctionPeriod());

        // Compare documents
        compareDocuments(changes, oldDetail.getDocuments(), newDetail.getDocuments());

        // Compare periods
        comparePeriods(changes, oldDetail.getPeriod(), newDetail.getPeriod());

        // Compare lots
        compareLots(changes, oldDetail.getLots(), newDetail.getLots());

        return changes;
    }

    private static void compareField(List<String> changes, String fieldName, String oldValue, String newValue) {
        if (!Objects.equals(oldValue, newValue)) {
            changes.add(String.format("%s изменился с <i>'%s'</i> <b>на</b> <i>'%s'</i>", fieldName,
                    oldValue != null ? oldValue : "null",
                    newValue != null ? newValue : "null"));
        }
    }

    private static void compareLocalDateTime(List<String> changes, String fieldName,
                                             LocalDateTime oldValue, LocalDateTime newValue) {
        if (!Objects.equals(oldValue, newValue)) {
            changes.add(String.format("%s изменился с <i>'%s'</i> на <i>'%s'</i>", fieldName,
                    oldValue != null ? DateUtil.dateTimeToStr(oldValue) : "-",
                    newValue != null ? DateUtil.dateTimeToStr(newValue) : "-"));
        }
    }

    private static void compareDocuments(List<String> changes, List<Document> oldDocs, List<Document> newDocs) {
        if (oldDocs == null && newDocs == null) return;
        if (oldDocs == null || newDocs == null) {
            changes.add("Перечень документов " + (oldDocs == null ? "добавили" : "удалили"));
            return;
        }

        // Check for added/removed documents
        if (oldDocs.size() != newDocs.size()) {
            changes.add(String.format("Количество документов изменилось с <i>%d</i> на <i>%d</i>", oldDocs.size(), newDocs.size()));
        }

        // Compare individual documents
        for (Document newDoc : newDocs) {
            boolean found = false;
            for (Document oldDoc : oldDocs) {
                if (Objects.equals(newDoc.getUrl(), oldDoc.getUrl())) {
                    // Document exists, check for changes
                    if (!Objects.equals(newDoc.getTitle(), oldDoc.getTitle())) {
                        changes.add(String.format("Название документа изменено с <i>'%s'</i> на <i>'%s'</i> для URL-адреса %s",
                                oldDoc.getTitle(), newDoc.getTitle(), newDoc.getUrl()));
                    }
                    if (!Objects.equals(newDoc.getDescription(), oldDoc.getDescription())) {
                        changes.add(String.format("Описание документа изменено с <i>'%s'</i> на <i>'%s'</i> для URL-адреса %s",
                                oldDoc.getDescription(), newDoc.getDescription(), newDoc.getUrl()));
                    }
                    if (!Objects.equals(newDoc.getDocumentType(), oldDoc.getDocumentType())) {
                        changes.add(String.format("Тип документа изменен с <i>'%s'</i> на <i>'%s'</i> для URL-адреса %s",
                                oldDoc.getDocumentType(), newDoc.getDocumentType(), newDoc.getUrl()));
                    }
                    found = true;
                    break;
                }
            }
            if (!found) {
                changes.add(String.format("\uD83D\uDCC4 Добавлен новый документ: %s (%s)", newDoc.getTitle(), "<a href=\""+newDoc.getUrl()+"\">скачать</a>"));
            }
        }

        // Check for removed documents
        for (Document oldDoc : oldDocs) {
            boolean found = false;
            for (Document newDoc : newDocs) {
                if (Objects.equals(oldDoc.getUrl(), newDoc.getUrl())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                changes.add(String.format("\uD83D\uDCC4 Документ удален: %s", oldDoc.getTitle()));
            }
        }
    }

    private static void comparePeriods(List<String> changes, Period oldPeriod, Period newPeriod) {
        if (oldPeriod == null && newPeriod == null) return;
        if (oldPeriod == null || newPeriod == null) {
            changes.add("Информация о периоде " + (oldPeriod == null ? "добавлено" : "удалено"));
            return;
        }

        // Compare enquiry period
        //compareEnquiryPeriod(changes, oldPeriod.getEnquiryPeriod(), newPeriod.getEnquiryPeriod());

        // Compare tender period
        //compareTenderPeriod(changes, oldPeriod.getTenderPeriod(), newPeriod.getTenderPeriod());

        // Compare enquiries
        compareEnquiries(changes, oldPeriod.getEnquiries(), newPeriod.getEnquiries());
    }

    private static void compareEnquiryPeriod(List<String> changes, EnquiryPeriod oldEp, EnquiryPeriod newEp) {
        if (oldEp == null && newEp == null) return;
        if (oldEp == null || newEp == null) {
            changes.add("Период запроса " + (oldEp == null ? "добавлено" : "удалено"));
            return;
        }

        compareLocalDateTime(changes, "Период рас", oldEp.getStartDate(), newEp.getStartDate());
        compareLocalDateTime(changes, "enquiryPeriod.endDate", oldEp.getEndDate(), newEp.getEndDate());
    }

    private static void compareTenderPeriod(List<String> changes, TenderPeriod oldTp, TenderPeriod newTp) {
        if (oldTp == null && newTp == null) return;
        if (oldTp == null || newTp == null) {
            changes.add("Tender period " + (oldTp == null ? "added" : "removed"));
            return;
        }

        compareLocalDateTime(changes, "tenderPeriod.startDate", oldTp.getStartDate(), newTp.getStartDate());
        compareLocalDateTime(changes, "tenderPeriod.endDate", oldTp.getEndDate(), newTp.getEndDate());
    }

    private static void compareEnquiries(List<String> changes, List<Enquiry> oldEnqs, List<Enquiry> newEnqs) {
        if (oldEnqs == null && newEnqs == null) return;
        if (oldEnqs == null || newEnqs == null) {
            changes.add("Список запросов " + (oldEnqs == null ? "добавлено \n" +
                    newEnqs.stream().map(Enquiry::toString).collect(Collectors.joining("\n"))
                    : "удалено"));
            return;
        }

        if (oldEnqs.size() != newEnqs.size()) {
            changes.add(String.format("Количество запросов изменилось с %d на %d", oldEnqs.size(), newEnqs.size()));
        }

        // Compare enquiries by title and date (assuming these are unique enough)
        for (Enquiry newEnq : newEnqs) {
            boolean found = false;
            for (Enquiry oldEnq : oldEnqs) {
                if (Objects.equals(newEnq.getTitle(), oldEnq.getTitle()) &&
                        Objects.equals(newEnq.getDate(), oldEnq.getDate())) {
                    // Enquiry exists, check for changes
                    compareLocalDateTime(changes, "Дата ответа на запрос '" + newEnq.getTitle() + "'",
                            oldEnq.getDateAnswered(), newEnq.getDateAnswered());
                    compareField(changes, "Описание запроса '" + newEnq.getTitle() + "'",
                            oldEnq.getDescription(), newEnq.getDescription());
                    compareField(changes, "Ответ на запрос '" + newEnq.getTitle() + "'",
                            oldEnq.getAnswer(), newEnq.getAnswer());
                    found = true;
                    break;
                }
            }
            if (!found) {
                changes.add(String.format("<b>Новый запрос добавлен</b>: %s",
                        newEnq.toString()));
            }
        }

        // Check for removed enquiries
        for (Enquiry oldEnq : oldEnqs) {
            boolean found = false;
            for (Enquiry newEnq : newEnqs) {
                if (Objects.equals(oldEnq.getTitle(), newEnq.getTitle()) &&
                        Objects.equals(oldEnq.getDate(), newEnq.getDate())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                changes.add(String.format("Запрос удален: %s (дата: %s)",
                        oldEnq.getTitle(), DateUtil.dateTimeToStr(oldEnq.getDate())));
            }
        }
    }

    private static void compareLots(List<String> changes, List<Lot> oldLots, List<Lot> newLots) {
        if (oldLots == null && newLots == null) return;
        if (oldLots == null || newLots == null) {
            changes.add("Список лотов " + (oldLots == null ? "добавили" : "удалили"));
            return;
        }

        if (oldLots.size() != newLots.size()) {
            changes.add(String.format("Количество лотов изменилось с %d на %d", oldLots.size(), newLots.size()));
        }

        // Compare lots by UUID
        for (Lot newLot : newLots) {
            Lot oldLot = findLotByUuid(oldLots, newLot.getUuid());
            if (oldLot == null) {
                changes.add(String.format("Добавлен новый лот: %s (%s)", newLot.getTitle(), newLot.getUuid()));
                continue;
            }

            // Compare lot properties
            compareField(changes, "описание для '" + newLot.getTitle() + "'",
                    oldLot.getDescription(), newLot.getDescription());
            compareField(changes, "название для UUID " + newLot.getUuid(),
                    oldLot.getTitle(), newLot.getTitle());
            compareField(changes, "статус для '" + newLot.getTitle() + "'",
                    oldLot.getStatus(), newLot.getStatus());

            // Compare lot value
            if (oldLot.getValue() != null && newLot.getValue() != null) {
                if (!Objects.equals(oldLot.getValue().getAmount(), newLot.getValue().getAmount())) {
                    changes.add(String.format("сумма изменилась с %s на %s для лота '%s'",
                            oldLot.getValue().getAmount(), newLot.getValue().getAmount(), newLot.getTitle()));
                }
                if (!Objects.equals(oldLot.getValue().getCurrency(), newLot.getValue().getCurrency())) {
                    changes.add(String.format("валюта изменена с %s на %s для лота '%s'",
                            oldLot.getValue().getCurrency(), newLot.getValue().getCurrency(), newLot.getTitle()));
                }
            } else if (oldLot.getValue() != newLot.getValue()) {
                changes.add(String.format("значение изменено для лота '%s'", newLot.getTitle()));
            }

            // Compare lot items
            compareLotItems(changes, oldLot.getLotItems(), newLot.getLotItems(), newLot.getTitle());

            // Compare suppliers
            if (!newLot.getStatus().equals("cancelled")) {
                compareLotSuppliers(changes, oldLot.getLotSuppliers(), newLot.getLotSuppliers(), newLot.getTitle());
            }
        }

        // Check for removed lots
        for (Lot oldLot : oldLots) {
            if (findLotByUuid(newLots, oldLot.getUuid()) == null) {
                changes.add(String.format("Лот удален: %s (%s)", oldLot.getTitle(), oldLot.getUuid()));
            }
        }
    }

    private static Lot findLotByUuid(List<Lot> lots, String uuid) {
        if (lots == null || uuid == null) return null;
        for (Lot lot : lots) {
            if (uuid.equals(lot.getUuid())) {
                return lot;
            }
        }
        return null;
    }

    private static void compareLotItems(List<String> changes, List<LotItem> oldItems, List<LotItem> newItems, String lotTitle) {
        if (oldItems == null && newItems == null) return;
        if (oldItems == null || newItems == null) {
            changes.add("Позиция для лота '" + lotTitle + "' " + (oldItems == null ? "добавили" : "удалили"));
            return;
        }

        if (oldItems.size() != newItems.size()) {
            changes.add(String.format("Количество позиций лота изменилось с %d на %d для лота '%s'",
                    oldItems.size(), newItems.size(), lotTitle));
        }

        // Compare items by description (assuming it's unique enough within a lot)
        for (LotItem newItem : newItems) {
            LotItem oldItem = findLotItemByDescription(oldItems, newItem.getDescription());
            if (oldItem == null) {
                changes.add(String.format("Новая позиция лота добавлена в '%s': %s", lotTitle, newItem.getDescription()));
                continue;
            }

            if (!Objects.equals(oldItem.getCodeCPV(), newItem.getCodeCPV())) {
                changes.add(String.format("lotItem.codeCPV changed from %s to %s for item '%s' in lot '%s'",
                        oldItem.getCodeCPV(), newItem.getCodeCPV(), newItem.getDescription(), lotTitle));
            }
            if (!Objects.equals(oldItem.getCount(), newItem.getCount())) {
                changes.add(String.format("lotItem.count changed from %d to %d for item '%s' in lot '%s'",
                        oldItem.getCount(), newItem.getCount(), newItem.getDescription(), lotTitle));
            }
        }

        // Check for removed items
        for (LotItem oldItem : oldItems) {
            if (findLotItemByDescription(newItems, oldItem.getDescription()) == null) {
                changes.add(String.format("Лотовая позиция удалена из '%s': %s", lotTitle, oldItem.getDescription()));
            }
        }
    }

    private static LotItem findLotItemByDescription(List<LotItem> items, String description) {
        if (items == null || description == null) return null;
        for (LotItem item : items) {
            if (description.equals(item.getDescription())) {
                return item;
            }
        }
        return null;
    }

    private static void compareLotSuppliers(List<String> changes, List<LotSupplier> oldSuppliers,
                                            List<LotSupplier> newSuppliers, String lotTitle) {
        if (oldSuppliers == null && newSuppliers == null) return;
        if (oldSuppliers == null || newSuppliers == null) {
            changes.add("Поставщики для '" + lotTitle + "' " + (oldSuppliers == null ? "добавлены" : "удалены"));
            return;
        }

        if (oldSuppliers.size() != newSuppliers.size()) {
            changes.add(String.format("Количество поставщиков изменилось с %d на %d для лота '%s'",
                    oldSuppliers.size(), newSuppliers.size(), lotTitle));
        }

        // Compare suppliers by ID (assuming it's unique)
        for (LotSupplier newSupplier : newSuppliers) {
            LotSupplier oldSupplier = findLotSupplierById(oldSuppliers, newSupplier.getId());
            if (oldSupplier == null) {
                changes.add(String.format("Новый поставщик добавлен в лот '%s': %s", lotTitle, newSupplier.getName()));
                continue;
            }

            compareField(changes, "наименование поставщика для лота '" + lotTitle + "'",
                    oldSupplier.getName(), newSupplier.getName());
            compareField(changes, "статус поставщика для лота '" + lotTitle + "'",
                    oldSupplier.getStatus(), newSupplier.getStatus());
            compareField(changes, "описания поставщика для лота '" + lotTitle + "'",
                    oldSupplier.getDescription(), newSupplier.getDescription());

            // Compare supplier value
            if (oldSupplier.getValue() != null && newSupplier.getValue() != null) {
                if (!Objects.equals(oldSupplier.getValue().getAmount(), newSupplier.getValue().getAmount())) {
                    changes.add(String.format("сумма изменилось с %s на %s для поставщика '%s' лот '%s'",
                            oldSupplier.getValue().getAmount(), newSupplier.getValue().getAmount(),
                            newSupplier.getName(), lotTitle));
                }
            }
        }

        // Check for removed suppliers
        for (LotSupplier oldSupplier : oldSuppliers) {
            if (findLotSupplierById(newSuppliers, oldSupplier.getId()) == null) {
                changes.add(String.format("Поставщик удален из лота '%s': %s", lotTitle, oldSupplier.getName()));
            }
        }
    }

    private static LotSupplier findLotSupplierById(List<LotSupplier> suppliers, String id) {
        if (suppliers == null || id == null) return null;
        for (LotSupplier supplier : suppliers) {
            if (id.equals(supplier.getId())) {
                return supplier;
            }
        }
        return null;
    }
}
