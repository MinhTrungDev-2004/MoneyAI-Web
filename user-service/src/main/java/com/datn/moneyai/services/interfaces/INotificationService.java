package com.datn.moneyai.services.interfaces;

import com.datn.moneyai.models.dtos.notification.NotificationCreateRequest;
import com.datn.moneyai.models.dtos.notification.NotificationGetResponse;
import com.datn.moneyai.models.dtos.notification.NotificationGetsResponse;
import com.datn.moneyai.models.dtos.notification.NotificationUpdateRequest;

import java.util.List;

public interface INotificationService {
    NotificationGetResponse createNotification(NotificationCreateRequest request);

    NotificationGetResponse updateNotification(Long id, NotificationUpdateRequest request);

    List<NotificationGetsResponse> getsNotification();

    void deleteNotification(Long id);
}
