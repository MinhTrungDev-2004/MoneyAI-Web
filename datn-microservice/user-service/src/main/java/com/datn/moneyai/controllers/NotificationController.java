package com.datn.moneyai.controllers;

import com.datn.moneyai.models.dtos.notification.NotificationCreateRequest;
import com.datn.moneyai.models.dtos.notification.NotificationGetResponse;
import com.datn.moneyai.models.dtos.notification.NotificationGetsResponse;
import com.datn.moneyai.models.dtos.notification.NotificationUpdateRequest;
import com.datn.moneyai.models.global.ApiResult;
import com.datn.moneyai.services.interfaces.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
    private final INotificationService notificationService;

    /**
     * API tạo mới một thông báo (Notification).
     *
     * @param request Dữ liệu đầu vào chứa thông tin thông báo cần tạo.
     * @return ResponseEntity chứa ApiResult mang theo đối tượng
     *         NotificationGetResponse
     *         vừa tạo.
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResult<NotificationGetResponse>> createNotification(
            @Valid @RequestBody NotificationCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.createNotification(request));
    }

    /**
     * API cập nhật thông tin một thông báo (Notification) theo ID.
     *
     * @param id      ID của thông báo cần cập nhật.
     * @param request Dữ liệu đầu vào chứa thông tin thông báo cần cập nhật.
     * @return ResponseEntity chứa ApiResult mang theo đối tượng
     *         NotificationGetResponse
     *         vừa được cập nhật.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResult<NotificationGetResponse>> updateNotification(@PathVariable Long id,
            @Valid @RequestBody NotificationUpdateRequest request) {
        return ResponseEntity.ok(notificationService.updateNotification(id, request));
    }

    /**
     * API lấy danh sách tất cả thông báo (Notification) của người dùng hiện tại.
     *
     * @return ResponseEntity chứa ApiResult mang theo danh sách đối tượng
     *         NotificationGetsResponse.
     */
    @GetMapping("/gets-all")
    public ResponseEntity<ApiResult<List<NotificationGetsResponse>>> getAllNotification() {
        return ResponseEntity.ok(notificationService.getsNotification());
    }

    /**
     * API xóa một thông báo (Notification) theo ID.
     *
     * @param id ID của thông báo cần xóa.
     * @return ResponseEntity chứa ApiResult mang theo null nếu xóa thành công.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResult<Void>> deleteNotification(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.deleteNotification(id));
    }
}
