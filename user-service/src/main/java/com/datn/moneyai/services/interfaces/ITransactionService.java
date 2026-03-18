package com.datn.moneyai.services.interfaces;

import com.datn.moneyai.models.dtos.transaction.TransactionRequest;
import com.datn.moneyai.models.dtos.transaction.TransactionResponse;
import com.datn.moneyai.models.dtos.transaction.TransactionUpdateRequest;
import com.datn.moneyai.models.global.ApiResult;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ITransactionService {

  ApiResult<TransactionResponse> createTransaction(TransactionRequest request);

  ApiResult<TransactionResponse> updateTransaction(Long id, TransactionUpdateRequest request);

  ApiResult<Void> deleteTransaction(Long id);

  ApiResult<List<TransactionResponse>> getTransactionsByCategory(Long categoryId);

  ApiResult<BigDecimal> getTotalAmountByCategoryAndMonth(Long categoryId);

  ApiResult<List<TransactionResponse>> getTransactionsByDate(LocalDate date);

  ApiResult<BigDecimal> calculateTotalIncome();

  ApiResult<BigDecimal> calculateTotalExpense();
}
