package org.shtiroy_ap.telegram.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Объект тендер.
 */
public class TenderDetailDto {
    /**
     * Уникальный идентификатор.
     */
    private Integer id;
    /**
     * Наименование тендера.
     */
    private String name;
    /**
     * Уникальный индетификатор тендера из mtender.md
     */
    private String uniqueId;
    /**
     * Ссылки на тендер.
     */
    private String urls;
    /**
     * Код категории тендера.
     */
    private String category;
    /**
     * Наименование категории.
     */
    private String categoryName;
    /**
     * Сумма по тендеру.
     */
    private BigDecimal amount;
    /**
     * Валюта.
     */
    private String currency;
    /**
     * Дата аукциона.
     */
    private String date;
    /**
     * Уникальный идентификатор заказчика.
     */
    private String costumerId;
    /**
     * Список лотов.
     */
    private List<Lot> lots;

    public TenderDetailDto() {
    }

    public TenderDetailDto(Integer id, String name, String uniqueId, String urls, String category, String categoryName, BigDecimal amount, String currency, String date, String costumerId, List<Lot> lots) {
        this.id = id;
        this.name = name;
        this.uniqueId = uniqueId;
        this.urls = urls;
        this.category = category;
        this.categoryName = categoryName;
        this.amount = amount;
        this.currency = currency;
        this.date = date;
        this.costumerId = costumerId;
        this.lots = lots;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCostumerId() {
        return costumerId;
    }

    public void setCostumerId(String costumerId) {
        this.costumerId = costumerId;
    }

    public List<Lot> getLots() {
        return lots;
    }

    public void setLots(List<Lot> lots) {
        this.lots = lots;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TenderDetailDto detailDto = (TenderDetailDto) o;
        boolean result = Objects.equals(id, detailDto.id) &&
                amount.compareTo(detailDto.getAmount()) == 0 &&
                Objects.equals(date, detailDto.date) &&
                Objects.equals(costumerId, detailDto.costumerId);
        if (!result) {
            return false;
        }
        for (Lot lot : lots){
            for (Lot dLot : detailDto.lots){
                if (dLot.getUuid().equals(lot.getUuid())){
                    result = result &&
                            Objects.equals(dLot.getStatus(), lot.getStatus());
                    if (lot.getLotSuppliers() != null){
                        result = result && Objects.equals(dLot.getLotSuppliers().size(), lot.getLotSuppliers().size());
                    }
                }
            }
        }
        return result;
    }
}
