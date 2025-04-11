package org.shtiroy_ap.telegram.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "allowed_user")
public class AllowedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long chatId;
    private String username;
    private String firstname;
    private boolean isActive;
}
