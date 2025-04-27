package org.shtiroy_ap.telegram.model;

public class TenderDto {
    private Integer id;
    private String name;
    private String url;
    private String customerName;
    private String customerId;
    private String value;
    private String date;
    private String uniqueId;
    private String category;

    public TenderDto() {
    }

    public TenderDto(Integer id, String name, String url, String customerName, String customerId, String value, String date, String uniqueId,
                     String category) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.customerName = customerName;
        this.customerId = customerId;
        this.value = value;
        this.date = date;
        this.uniqueId = uniqueId;
        this.category = category;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
