package org.shtiroy_ap.telegram.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(schema = "public", name = "pin_code")
public class PinCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private int maxActivations;

    private int usedActivations;

    @ManyToOne
    private Company company;

    public PinCode() {
    }

    public PinCode(Long id, String code, int maxActivations, int usedActivations, Company company) {
        this.id = id;
        this.code = code;
        this.maxActivations = maxActivations;
        this.usedActivations = usedActivations;
        this.company = company;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getMaxActivations() {
        return maxActivations;
    }

    public void setMaxActivations(int maxActivations) {
        this.maxActivations = maxActivations;
    }

    public int getUsedActivations() {
        return usedActivations;
    }

    public void setUsedActivations(int usedActivations) {
        this.usedActivations = usedActivations;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public boolean isAvailable() {
        return usedActivations < maxActivations;
    }

    public void incrementUsage() {
        this.usedActivations++;
    }
}
