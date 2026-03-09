package com.datn.moneyai.services.implement;

import com.datn.moneyai.exceptions.UserMessageException;
import com.datn.moneyai.models.dtos.notification.NotificationCreateRequest;
import com.datn.moneyai.models.dtos.notification.NotificationGetResponse;
import com.datn.moneyai.models.dtos.notification.NotificationGetsResponse;
import com.datn.moneyai.models.dtos.notification.NotificationUpdateRequest;
import com.datn.moneyai.models.entities.bases.Notification;
import com.datn.moneyai.models.entities.bases.User;
import com.datn.moneyai.models.entities.enums.NotificationType;
import com.datn.moneyai.models.global.ApiResult;
import com.datn.moneyai.repositories.NotificationRepository;
import com.datn.moneyai.repositories.UserRepository;
import com.datn.moneyai.services.interfaces.INotificationService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotificationService implements INotificationService {
    final NotificationRepository notificationRepository;
    final UserRepository userRepository;

    /**
     * Lấy thông tin người dùng hiện tại từ Security Context.
     *
     * @return Đối tượng User hiện tại.
     * @throws UserMessageException Nếu người dùng không tồn tại.
     */
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserMessageException("Người dùng không tồn tại"));
    }

    /**
     * Chuyển đổi đối tượng Notification thành NotificationGetResponse.
     *
     * @param notification Đối tượng Notification cần chuyển đổi.
     * @return Đối tượng NotificationGetResponse đã được chuyển đổi.
     */
    private NotificationGetResponse mapToGetResponse(Notification notification) {
        return NotificationGetResponse.builder()
                .id(notification.getId())
                .userId(notification.getUser().getId())
                .type(notification.getType())
                .title(notification.getTitle())
                .content(notification.getContent())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .updatedAt(notification.getUpdatedAt())
                .build();
    }

    /**
     * Chuyển đổi đối tượng Notification thành NotificationGetsResponse.
     *
     * @param notification Đối tượng Notification cần chuyển đổi.
     * @return Đối tượng NotificationGetsResponse đã được chuyển đổi.
     */
    private NotificationGetsResponse mapToGetsResponse(Notification notification) {
        return NotificationGetsResponse.builder()
                .id(notification.getId())
                .type(notification.getType())
                .title(notification.getTitle())
                .content(notification.getContent())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    /**
     * Tạo thông báo mới.
     *
     * @param request Đối tượng NotificationCreateRequest chứa thông tin cần tạo.
     * @return Đối tượng ApiResult chứa thông tin về kết quả tạo thông báo.
     */
    @Override
    public ApiResult<NotificationGetResponse> createNotification(NotificationCreateRequest request) {
        User user = getCurrentUser();

        Notification notification = Notification.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .type(request.getType() != null ? request.getType() : NotificationType.SYSTEM)
                .isRead(false)
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        return ApiResult.success(mapToGetResponse(savedNotification), "Tạo thông báo thành công");
    }

    /**
     * Cập nhật thông báo.
     *
     * @param id      ID của thông báo cần cập nhật.
     * @param request Đối tượng NotificationUpdateRequest chứa thông tin cần cập
     *                nhật.
     * @return Đối tượng ApiResult chứa thông tin về kết quả cập nhật thông báo.
     */
    @Override
    public ApiResult<NotificationGetResponse> updateNotification(Long id, NotificationUpdateRequest request) {
        User user = getCurrentUser();
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new UserMessageException("Thông báo không tồn tại"));

        if (!notification.getUser().getId().equals(user.getId())) {
            throw new UserMessageException("Bạn không có quyền cập nhật thông báo này");
        }

        if (request.getTitle() != null && !request.getTitle().isEmpty()) {
            notification.setTitle(request.getTitle());
        }
        if (request.getContent() != null && !request.getContent().isEmpty()) {
            notification.setContent(request.getContent());
        }
        if (request.getType() != null) {
            notification.setType(request.getType());
        }
        if (request.getIsRead() != null) {
            notification.setRead(request.getIsRead());
        }

        Notification updatedNotification = notificationRepository.save(notification);
        return ApiResult.success(mapToGetResponse(updatedNotification), "Cập nhật thông báo thành công");
    }

    /**
     * Lấy danh sách thông báo.
     *
     * @return Đối tượng ApiResult chứa danh sách thông báo.
     */
    @Override
    public ApiResult<List<NotificationGetsResponse>> getsNotification() {
        User user = getCurrentUser();
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        List<NotificationGetsResponse> responses = notifications.stream()
                .map(this::mapToGetsResponse)
                .collect(Collectors.toList());
        return ApiResult.success(responses, "Lấy danh sách thông báo thành công");
    }

    /**
     * Xóa thông báo.
     *
     * @param id ID của thông báo cần xóa.
     * @return Đối tượng ApiResult chứa thông tin về kết quả xóa thông báo.
     */
    @Override
    public ApiResult<Void> deleteNotification(Long id) {
        User user = getCurrentUser();
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new UserMessageException("Thông báo không tồn tại"));

        if (!notification.getUser().getId().equals(user.getId())) {
            throw new UserMessageException("Bạn không có quyền xóa thông báo này");
        }

        notificationRepository.delete(notification);
        return ApiResult.success(null, "Xóa thông báo thành công");
    }
}
