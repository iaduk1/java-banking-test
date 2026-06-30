package guru.springframework.msscbrewery.services;

import guru.springframework.msscbrewery.web.model.AccountDto;
import guru.springframework.msscbrewery.web.model.TransactionDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@Service
public class TransactionImpl implements TransactionService {

    private static final String TRANSACTION_TYPE_DEPOSIT = "DEPOSIT";
    private static final String TRANSACTION_TYPE_WITHDRAWAL = "WITHDRAWAL";
    private final AccountService accountService;

    private static final Map<String, List<TransactionDto>> transactionStore = new HashMap<>();

    public TransactionImpl(AccountService accountService) {
        this.accountService = accountService;
    }


    @Override

    public List<TransactionDto> getTransactions(String accountId, String userId) {

        return transactionStore.getOrDefault(accountId, new ArrayList<>());
    }

    @Override
    public TransactionDto getTransactionById(String accountId, String transactionId, String userId) {
        return transactionStore.getOrDefault(accountId, new ArrayList<>())
                .stream().filter(f -> f.getTransactionId().equals(transactionId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trnactions could not be found"));
    }

    @Override
    public TransactionDto executeTransaction(String accountId, TransactionDto transactionDto, String authenticatedUser) {
        AccountDto account = accountService.getAccountsById(accountId, authenticatedUser);

        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }

        BigDecimal amount = transactionDto.getAmount();

        if (TRANSACTION_TYPE_WITHDRAWAL.equalsIgnoreCase(transactionDto.getType())) {
            if (account.getBalance().compareTo(amount) < 0) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Insufficient funds on balance");
            }
            accountService.updateBalance(accountId, account.getBalance().subtract(amount));
        } else {
            accountService.updateBalance(accountId, account.getBalance().add(amount));
        }
        String transactionId = "transact-" + UUID.randomUUID().toString().replace("-", "").substring(0, 7);

        TransactionDto savedTransaction = TransactionDto.builder()
                .accountId(accountId)
                .amount(amount)
                .transactionId(transactionId)
                .currency(transactionDto.getCurrency())
                .type(transactionDto.getType())
                .reference(transactionDto.getReference())
                .userId(authenticatedUser)
                .createdTimestamp(OffsetDateTime.now())
                .build();
        List<TransactionDto> transactions = transactionStore.getOrDefault(accountId, new ArrayList<>());
        System.out.println(transactionStore.keySet());
        transactions.add(savedTransaction);
        transactionStore.put(accountId, transactions);

        return savedTransaction;
    }

}