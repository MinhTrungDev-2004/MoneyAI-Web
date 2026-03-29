package com.datn.moneyai.models.dtos.notification;

import com.datn.moneyai.models.entities.enums.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationGetsResponse {

    private Long id;

    private NotificationType type;

    private String title;

    private String content;

    private boolean isRead;

    private LocalDateTime createdAt;
}
