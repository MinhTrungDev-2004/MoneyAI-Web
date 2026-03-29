package com.datn.moneyai.services.interfaces;

import com.datn.moneyai.models.dtos.saving.SavingGoalRequest;
import com.datn.moneyai.models.dtos.saving.SavingGoalResponse;

import java.util.List;

public interface ISavingGoalService {
    SavingGoalResponse createSavingGoal(SavingGoalRequest request);

    List<SavingGoalResponse> getsSavingGoal();

    SavingGoalResponse updateSavingGoal(Long id, SavingGoalRequest request);

    void deleteSavingGoal(Long id);
}
