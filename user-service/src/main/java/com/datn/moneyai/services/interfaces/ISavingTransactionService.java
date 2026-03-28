package com.datn.moneyai.services.interfaces;

import com.datn.moneyai.models.dtos.saving_transaction.SavingGoalDetailResponse;
import com.datn.moneyai.models.dtos.saving_transaction.SavingTransactionRequest;
import com.datn.moneyai.models.dtos.saving_transaction.SavingTransactionResponse;
import com.datn.moneyai.models.global.ApiResult;


public interface ISavingTransactionService {

    ApiResult<SavingTransactionResponse> createSavingTransaction(SavingTransactionRequest savingTransactionRequest);

    ApiResult<SavingGoalDetailResponse> getSavingTransaction(Long savingGoalId);
}
