package org.shtiroy_ap.telegram.model;

import java.math.BigDecimal;

public class Value {
    private BigDecimal amount;
    private String currency;

    public Value() {
    }

    public Value(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
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
}
