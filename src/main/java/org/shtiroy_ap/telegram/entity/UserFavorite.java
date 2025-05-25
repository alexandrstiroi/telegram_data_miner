package org.shtiroy_ap.telegram.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_favorite")
public class UserFavorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long chatId;
    private String tenderId;
    private String lastSnapshot;
    private LocalDateTime createdAt;
    private boolean active;

    public UserFavorite() {
    }

    public UserFavorite(Integer id, Long chatId, String tenderId, String lastSnapshot, LocalDateTime createdAt, boolean active) {
        this.id = id;
        this.chatId = chatId;
        this.tenderId = tenderId;
        this.lastSnapshot = lastSnapshot;
        this.createdAt = createdAt;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getTenderId() {
        return tenderId;
    }

    public void setTenderId(String tenderId) {
        this.tenderId = tenderId;
    }

    public String getLastSnapshot() {
        return lastSnapshot;
    }

    public void setLastSnapshot(String lastSnapshot) {
        this.lastSnapshot = lastSnapshot;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
