package com.datn.moneyai.services.interfaces;

import com.datn.moneyai.models.dtos.saving_transaction.SavingGoalDetailResponse;
import com.datn.moneyai.models.dtos.saving_transaction.SavingTransactionRequest;
import com.datn.moneyai.models.dtos.saving_transaction.SavingTransactionResponse;


public interface ISavingTransactionService {

    SavingTransactionResponse createSavingTransaction(SavingTransactionRequest savingTransactionRequest);

    SavingGoalDetailResponse getSavingTransaction(Long savingGoalId);
}
