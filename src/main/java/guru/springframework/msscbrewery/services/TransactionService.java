package guru.springframework.msscbrewery.services;

import guru.springframework.msscbrewery.web.model.AccountDto;
import guru.springframework.msscbrewery.web.model.TransactionDto;
import java.util.List;

public interface TransactionService {

    List<TransactionDto> getTransactions(String accountId, String userId);
    TransactionDto getTransactionById(String accountId, String transactionId, String userId);

    TransactionDto executeTransaction(String accountId, TransactionDto transactionDto, String authenticatedUser);

}
