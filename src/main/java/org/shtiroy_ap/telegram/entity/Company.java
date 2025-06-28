package org.shtiroy_ap.telegram.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(schema = "public", name = "t_company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "company")
    private List<PinCode> pinCodes;

    public Company() {
    }

    public Company(Long id, String name, List<PinCode> pinCodes) {
        this.id = id;
        this.name = name;
        this.pinCodes = pinCodes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PinCode> getPinCodes() {
        return pinCodes;
    }

    public void setPinCodes(List<PinCode> pinCodes) {
        this.pinCodes = pinCodes;
    }
}
