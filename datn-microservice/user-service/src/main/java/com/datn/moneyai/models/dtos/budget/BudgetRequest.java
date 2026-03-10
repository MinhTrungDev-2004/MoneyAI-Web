package com.datn.moneyai.models.dtos.budget;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BudgetRequest {

    @NotNull(message = "Số tiền hạn mức không được để trống")
    @Positive(message = "Hạn mức phải lớn hơn 0")
    private BigDecimal limitAmount;

    @NotNull(message = "Tháng không được để trống")
    private Integer month;

    @NotNull(message = "Năm không được để trống")
    private Integer year;

    @NotNull(message = "ID danh mục không được để trống")
    private Long categoryId;
}