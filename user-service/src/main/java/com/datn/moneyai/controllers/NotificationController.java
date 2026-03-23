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
@Tag(name = "Thông Báo - NotificationController", description = "Quản lý thông báo của hệ thống gửi đến người dùng")
public class NotificationController {

    private final INotificationService notificationService;

    @Operation(summary = "Tạo mới một thông báo")
    @PostMapping
    public ResponseEntity<ApiResult<NotificationGetResponse>> createNotification(
            @Valid @RequestBody NotificationCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.createNotification(request));
    }

    @Operation(summary = "Cập nhật thông tin thông báo")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<NotificationGetResponse>> updateNotification(
            @PathVariable Long id,
            @Valid @RequestBody NotificationUpdateRequest request) {
        return ResponseEntity.ok(notificationService.updateNotification(id, request));
    }

    @Operation(summary = "Lấy danh sách tất cả thông báo của người dùng hiện tại")
    @GetMapping
    public ResponseEntity<ApiResult<List<NotificationGetsResponse>>> getAllNotification() {
        return ResponseEntity.ok(notificationService.getsNotification());
    }

    @Operation(summary = "Xóa một thông báo")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> deleteNotification(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.deleteNotification(id));
    }
}