package org.shtiroy_ap.telegram.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_user")
public class User {
    @Id
    private Long chatId;

    private String username;

    private boolean authorized;
    @ManyToOne
    private Company company;

    private LocalDateTime authenticatedAt;

    public User() {}

    public User(Long chatId, String username, boolean authorized) {
        this.chatId = chatId;
        this.username = username;
        this.authorized = authorized;
    }

    public User(Long chatId, String username, boolean authorized, Company company, LocalDateTime authenticatedAt) {
        this.chatId = chatId;
        this.username = username;
        this.authorized = authorized;
        this.company = company;
        this.authenticatedAt = authenticatedAt;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }
}
