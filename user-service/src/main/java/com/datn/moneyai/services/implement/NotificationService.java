package com.datn.moneyai.services.implement;

import com.datn.moneyai.exceptions.UserMessageException;
import com.datn.moneyai.models.dtos.notification.NotificationCreateRequest;
import com.datn.moneyai.models.dtos.notification.NotificationGetResponse;
import com.datn.moneyai.models.dtos.notification.NotificationGetsResponse;
import com.datn.moneyai.models.dtos.notification.NotificationUpdateRequest;
import com.datn.moneyai.models.entities.bases.NotificationEntity;
import com.datn.moneyai.models.entities.bases.UserEntity;
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
    private UserEntity getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserMessageException("Người dùng không tồn tại"));
    }

    /**
     * Chuyển đổi đối tượng Notification thành NotificationGetResponse.
     *
     * @param notificationEntity Đối tượng Notification cần chuyển đổi.
     * @return Đối tượng NotificationGetResponse đã được chuyển đổi.
     */
    private NotificationGetResponse mapToGetResponse(NotificationEntity notificationEntity) {
        return NotificationGetResponse.builder()
                .id(notificationEntity.getId())
                .userId(notificationEntity.getUser().getId())
                .type(notificationEntity.getType())
                .title(notificationEntity.getTitle())
                .content(notificationEntity.getContent())
                .isRead(notificationEntity.isRead())
                .createdAt(notificationEntity.getCreatedAt())
                .updatedAt(notificationEntity.getUpdatedAt())
                .build();
    }

    /**
     * Chuyển đổi đối tượng Notification thành NotificationGetsResponse.
     *
     * @param notificationEntity Đối tượng Notification cần chuyển đổi.
     * @return Đối tượng NotificationGetsResponse đã được chuyển đổi.
     */
    private NotificationGetsResponse mapToGetsResponse(NotificationEntity notificationEntity) {
        return NotificationGetsResponse.builder()
                .id(notificationEntity.getId())
                .type(notificationEntity.getType())
                .title(notificationEntity.getTitle())
                .content(notificationEntity.getContent())
                .isRead(notificationEntity.isRead())
                .createdAt(notificationEntity.getCreatedAt())
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
        UserEntity user = getCurrentUser();

        NotificationEntity notificationEntity = NotificationEntity.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .type(request.getType() != null ? request.getType() : NotificationType.SYSTEM)
                .isRead(false)
                .build();

        NotificationEntity savedNotificationEntity = notificationRepository.save(notificationEntity);
        return ApiResult.success(mapToGetResponse(savedNotificationEntity), "Tạo thông báo thành công");
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
        UserEntity user = getCurrentUser();
        NotificationEntity notificationEntity = notificationRepository.findById(id)
                .orElseThrow(() -> new UserMessageException("Thông báo không tồn tại"));

        if (!notificationEntity.getUser().getId().equals(user.getId())) {
            throw new UserMessageException("Bạn không có quyền cập nhật thông báo này");
        }

        if (request.getTitle() != null && !request.getTitle().isEmpty()) {
            notificationEntity.setTitle(request.getTitle());
        }

        if (request.getContent() != null && !request.getContent().isEmpty()) {
            notificationEntity.setContent(request.getContent());
        }

        if (request.getType() != null) {
            notificationEntity.setType(request.getType());
        }
        
        if (request.getIsRead() != null) {
            notificationEntity.setRead(request.getIsRead());
        }

        NotificationEntity updatedNotificationEntity = notificationRepository.save(notificationEntity);
        return ApiResult.success(mapToGetResponse(updatedNotificationEntity), "Cập nhật thông báo thành công");
    }

    /**
     * Lấy danh sách thông báo.
     *
     * @return Đối tượng ApiResult chứa danh sách thông báo.
     */
    @Override
    public ApiResult<List<NotificationGetsResponse>> getsNotification() {
        UserEntity user = getCurrentUser();
        List<NotificationEntity> notificationEntities = notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        List<NotificationGetsResponse> responses = notificationEntities.stream()
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
        UserEntity user = getCurrentUser();
        NotificationEntity notificationEntity = notificationRepository.findById(id)
                .orElseThrow(() -> new UserMessageException("Thông báo không tồn tại"));

        if (!notificationEntity.getUser().getId().equals(user.getId())) {
            throw new UserMessageException("Bạn không có quyền xóa thông báo này");
        }

        notificationRepository.delete(notificationEntity);
        return ApiResult.success(null, "Xóa thông báo thành công");
    }
}
