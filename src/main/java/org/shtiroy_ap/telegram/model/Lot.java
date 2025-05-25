package org.shtiroy_ap.telegram.model;

import java.util.List;

/**
 * Объект для передачи информации о лоте.
 */
public class Lot {
    /**
     * uuid идентификатор из mtender.md
     */
    private String uuid;

    /**
     * Описание лота.
     */
    private String description;
    /**
     * Наименование лота.
     */
    private String title;
    /**
     * Стоимость лота.
     */
    private Value value;
    /**
     * Статус лота.
     */
    private String status;
    /**
     * Список товаров в лоте.
     */
    private List<LotItem> lotItems;
    /**
     * Участники по лоту.
     */
    private List<LotSupplier> lotSuppliers;

    public Lot() {
    }

    public Lot(String uuid, String description, String title, Value value, String status, List<LotItem> lotItems, List<LotSupplier> lotSuppliers) {
        this.uuid = uuid;
        this.description = description;
        this.title = title;
        this.value = value;
        this.status = status;
        this.lotItems = lotItems;
        this.lotSuppliers = lotSuppliers;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<LotItem> getLotItems() {
        return lotItems;
    }

    public void setLotItems(List<LotItem> lotItems) {
        this.lotItems = lotItems;
    }

    public List<LotSupplier> getLotSuppliers() {
        return lotSuppliers;
    }

    public void setLotSuppliers(List<LotSupplier> lotSuppliers) {
        this.lotSuppliers = lotSuppliers;
    }
}
