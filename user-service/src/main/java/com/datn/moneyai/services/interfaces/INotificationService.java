package com.datn.moneyai.services.interfaces;

import com.datn.moneyai.models.dtos.notification.NotificationCreateRequest;
import com.datn.moneyai.models.dtos.notification.NotificationGetResponse;
import com.datn.moneyai.models.dtos.notification.NotificationGetsResponse;
import com.datn.moneyai.models.dtos.notification.NotificationUpdateRequest;
import com.datn.moneyai.models.global.ApiResult;

import java.util.List;

public interface INotificationService {
    ApiResult<NotificationGetResponse> createNotification(NotificationCreateRequest request);

    ApiResult<NotificationGetResponse> updateNotification(Long id, NotificationUpdateRequest request);

    ApiResult<List<NotificationGetsResponse>> getsNotification();

    ApiResult<Void> deleteNotification(Long id);
}
