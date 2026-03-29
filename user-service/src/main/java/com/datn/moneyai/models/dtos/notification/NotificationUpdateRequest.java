package com.datn.moneyai.models.dtos.notification;

import com.datn.moneyai.models.entities.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationUpdateRequest {

    @NotBlank(message = "Vui lòng nhập tiêu đề thông báo")
    private String title;

    @NotBlank(message = "Vui lòng nhập nội dung thông báo")
    private String content;

    private NotificationType type;

    private Boolean isRead;
}
