package com.datn.moneyai.services.interfaces;

import com.datn.moneyai.models.dtos.transaction.TransactionRequest;
import com.datn.moneyai.models.dtos.transaction.TransactionResponse;
import com.datn.moneyai.models.dtos.transaction.TransactionUpdateRequest;
import com.datn.moneyai.models.global.ApiResult;

import java.time.Year;
import java.time.YearMonth;
import java.util.List;

public interface ITransactionService {

    ApiResult<TransactionResponse> createTransaction(TransactionRequest request);

    ApiResult<TransactionResponse> updateTransaction(Long id, TransactionUpdateRequest request);

    ApiResult<Void> deleteTransaction(Long id);

    ApiResult<List<TransactionResponse>> getTransactionsByCategory(Long categoryId);

    ApiResult<List<TransactionResponse>> getTransactionsByMonth(YearMonth monthYear);

    ApiResult<List<TransactionResponse>> getTransactionsByYear(Year month);
}
