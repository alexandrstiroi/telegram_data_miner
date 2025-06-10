package org.shtiroy_ap.telegram.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private String status;
    private String statusDetails;
    private Period period;
    private LocalDateTime auctionPeriod;
    private List<Document> documents;

    public TenderDetailDto() {
    }

    public TenderDetailDto(Integer id, String name, String uniqueId, String urls, String category, String categoryName, BigDecimal amount, String currency, String date, String costumerId, List<Lot> lots, String status, String statusDetails, Period period, LocalDateTime auctionPeriod, List<Document> documents) {
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
        this.status = status;
        this.statusDetails = statusDetails;
        this.period = period;
        this.auctionPeriod = auctionPeriod;
        this.documents = documents;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDetails() {
        return statusDetails;
    }

    public void setStatusDetails(String statusDetails) {
        this.statusDetails = statusDetails;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public LocalDateTime getAuctionPeriod() {
        return auctionPeriod;
    }

    public void setAuctionPeriod(LocalDateTime auctionPeriod) {
        this.auctionPeriod = auctionPeriod;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TenderDetailDto detailDto = (TenderDetailDto) o;
        boolean result = Objects.equals(id, detailDto.id) &&
                amount.compareTo(detailDto.getAmount()) == 0 &&
                Objects.equals(date, detailDto.date) &&
                Objects.equals(costumerId, detailDto.costumerId) &&
                Objects.equals(status, detailDto.status) &&
                Objects.equals(statusDetails, detailDto.statusDetails);
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
