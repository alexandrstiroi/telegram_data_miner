package org.shtiroy_ap.telegram.model;

import java.math.BigDecimal;
import java.util.List;

public class TenderDetailDto {
    private Integer id;
    private String name;
    private String uniqueId;
    private String urls;
    private String category;
    private String categoryName;
    private BigDecimal amount;
    private String currency;
    private String date;
    private String costumerId;
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
}
