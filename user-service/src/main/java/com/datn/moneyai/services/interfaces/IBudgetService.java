package com.datn.moneyai.services.interfaces;

import com.datn.moneyai.models.dtos.budget.BudgetRequest;
import com.datn.moneyai.models.dtos.budget.BudgetResponse;
import java.util.List;

public interface IBudgetService {
    BudgetResponse createBudget(BudgetRequest request);

    BudgetResponse updateBudget(Long id, BudgetRequest request);

    BudgetResponse getBudget(Long id);

    BudgetResponse getBudgetByCategory(Long categoryId, Integer month, Integer year);

    List<BudgetResponse> listBudgets(Integer month, Integer year);

    void deleteBudget(Long id);
}
