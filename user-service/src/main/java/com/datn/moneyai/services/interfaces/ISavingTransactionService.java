package com.datn.moneyai.services.interfaces;

import com.datn.moneyai.models.dtos.SavingTransaction.SavingTransactionRequest;
import com.datn.moneyai.models.dtos.SavingTransaction.SavingTransactionResponse;
import com.datn.moneyai.models.global.ApiResult;

public interface ISavingTransactionService {

    ApiResult<SavingTransactionResponse> createSavingTransaction(SavingTransactionRequest savingTransactionRequest);
}
