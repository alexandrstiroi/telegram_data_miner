package org.shtiroy_ap.telegram.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user_retries")
public class UserRetries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long chatId;
    private int tries;
    private LocalDateTime createStamp;
    private LocalDateTime modifyStamp;
}
