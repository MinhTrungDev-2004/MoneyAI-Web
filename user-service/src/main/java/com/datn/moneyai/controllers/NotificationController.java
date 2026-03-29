package com.datn.moneyai.controllers;

import com.datn.moneyai.models.dtos.notification.NotificationCreateRequest;
import com.datn.moneyai.models.dtos.notification.NotificationGetResponse;
import com.datn.moneyai.models.dtos.notification.NotificationGetsResponse;
import com.datn.moneyai.models.dtos.notification.NotificationUpdateRequest;
import com.datn.moneyai.models.global.ApiResult;
import com.datn.moneyai.services.interfaces.INotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
@Tag(name = "NotificationController", description = "Quản lý thông báo của hệ thống gửi đến người dùng")
public class NotificationController {

    private final INotificationService notificationService;

    @Operation(summary = "Tạo mới một thông báo")
    @PostMapping
    public ResponseEntity<ApiResult<NotificationGetResponse>> createNotification(
            @Valid @RequestBody NotificationCreateRequest request) {
        NotificationGetResponse response = notificationService.createNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResult.success(response, "Tạo thông báo thành công"));
    }

    @Operation(summary = "Cập nhật thông tin thông báo")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<NotificationGetResponse>> updateNotification(
            @PathVariable Long id,
            @Valid @RequestBody NotificationUpdateRequest request) {
        NotificationGetResponse response = notificationService.updateNotification(id, request);
        return ResponseEntity.ok(ApiResult.success(response, "Cập nhật thông báo thành công"));
    }

    @Operation(summary = "Lấy danh sách tất cả thông báo của người dùng hiện tại")
    @GetMapping
    public ResponseEntity<ApiResult<List<NotificationGetsResponse>>> getAllNotification() {
        List<NotificationGetsResponse> response = notificationService.getsNotification();
        return ResponseEntity.ok(ApiResult.success(response, "Lấy danh sách thông báo thành công"));
    }

    @Operation(summary = "Xóa một thông báo")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(ApiResult.success(null, "Xóa thông báo thành công"));
    }
}
