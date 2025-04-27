package org.shtiroy_ap.telegram.model;

public class LotItem {
    private String codeCPV;
    private String description;
    private Integer count;

    public LotItem() {
    }

    public LotItem(String codeCPV, String description, Integer count) {
        this.codeCPV = codeCPV;
        this.description = description;
        this.count = count;
    }

    public String getCodeCPV() {
        return codeCPV;
    }

    public void setCodeCPV(String codeCPV) {
        this.codeCPV = codeCPV;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
