package org.shtiroy_ap.telegram.model;

import java.math.BigDecimal;

/**
 * Объект сумма тендера или лота.
 */
public class Value {
    /**
     * Сумма.
     */
    private BigDecimal amount;
    /**
     * Валюта.
     */
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
