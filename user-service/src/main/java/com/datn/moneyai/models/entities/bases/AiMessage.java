package com.datn.moneyai.models.entities.bases;

import com.datn.moneyai.models.entities.enums.AiMessageRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ai_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private AiConversation conversation;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AiMessageRole role;

    @Column(columnDefinition = "TEXT")
    private String content;
}
