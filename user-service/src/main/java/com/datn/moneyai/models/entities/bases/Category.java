package com.datn.moneyai.models.entities.bases;
import com.datn.moneyai.models.entities.User;
import com.datn.moneyai.models.entities.enums.CategoryType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "name", "type"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categories extends BaseEntity {
    @Column(nullable = false)
    private String name;

    private String icon;

    private String colorCode;

    @Enumerated(EnumType.STRING)
    private CategoryType type;

    private Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}