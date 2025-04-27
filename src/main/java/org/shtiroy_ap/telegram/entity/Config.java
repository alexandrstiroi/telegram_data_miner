package org.shtiroy_ap.telegram.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_config")
public class Config {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String parament;
    private String value;
    private LocalDateTime modifyStamp;

    public Config() {
    }

    public Config(String parament, String value, LocalDateTime modifyStamp) {
        this.parament = parament;
        this.value = value;
        this.modifyStamp = modifyStamp;
    }

    public Config(Integer id, String parament, String value, LocalDateTime modifyStamp) {
        this.id = id;
        this.parament = parament;
        this.value = value;
        this.modifyStamp = modifyStamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParament() {
        return parament;
    }

    public void setParament(String parament) {
        this.parament = parament;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocalDateTime getModifyStamp() {
        return modifyStamp;
    }

    public void setModifyStamp(LocalDateTime modifyStamp) {
        this.modifyStamp = modifyStamp;
    }
}
