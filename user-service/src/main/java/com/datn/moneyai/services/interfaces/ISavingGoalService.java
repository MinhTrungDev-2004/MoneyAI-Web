package com.datn.moneyai.services.interfaces;

import com.datn.moneyai.models.dtos.saving.SavingGoalRequest;
import com.datn.moneyai.models.dtos.saving.SavingGoalResponse;
import com.datn.moneyai.models.global.ApiResult;

import java.util.List;

public interface ISavingGoalService {
    ApiResult<SavingGoalResponse> createSavingGoal(SavingGoalRequest request);

    ApiResult<List<SavingGoalResponse>> getsSavingGoal();

    ApiResult<SavingGoalResponse> updateSavingGoal(Long id, SavingGoalRequest request);

    ApiResult<SavingGoalResponse> deleteSavingGoal(Long id);
}
