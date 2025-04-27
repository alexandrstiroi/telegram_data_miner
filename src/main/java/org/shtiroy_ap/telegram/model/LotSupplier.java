package org.shtiroy_ap.telegram.model;

public class LotSupplier {
    private String id;
    private String name;
    private String status;
    private String description;
    private Value value;

    public LotSupplier() {
    }

    public LotSupplier(String id, String name, String status, String description, Value value) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.description = description;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }
}
