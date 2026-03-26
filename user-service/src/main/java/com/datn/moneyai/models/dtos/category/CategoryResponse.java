package com.datn.moneyai.models.dtos.category;

import com.datn.moneyai.models.entities.enums.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    private Long id;

    private Long userId;

    private String name;

    private CategoryType type;

    private String icon;

    private String colorCode;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
