package guru.springframework.msscbrewery.services;

import guru.springframework.msscbrewery.web.model.AccountDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;

import java.util.HashMap;
import java.util.Map;


@Service
public class AccountServiceImpl implements AccountService {

    private static final Map<String, AccountDto> store = new HashMap<>();
    private static final Map<String, String> ownerStore = new HashMap<>();

    @Override
    public List<AccountDto> getAccounts(String authenticatedUser) {
        return ownerStore.entrySet().stream()
                .filter(e -> e.getValue().equals(authenticatedUser))
                .map(e -> store.get(e.getKey()))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public AccountDto getAccountsById(String accountId, String authenticatedUser) {
        AccountDto account = store.get(accountId);


        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account number: " + accountId + " does not exist");
        }

        if (!authenticatedUser.equals(ownerStore.get(accountId))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied: You do not have an account");
        }

        return account;

    }


    @Override
    public AccountDto createAccount(String userId, AccountDto accountDto) {
        String accountNumber = "01" + String.format("%06d", new Random().nextInt(999999));
        OffsetDateTime now = OffsetDateTime.now();
        AccountDto newAccount = AccountDto.builder()
                .accountNumber(accountNumber)
                .sortCode("10-10-10")
                .name(accountDto.getName())
                .accountType(accountDto.getAccountType())
                .balance(BigDecimal.ZERO)
                .currency("GBP")
                .createdTimestamp(now)
                .updatedTimestamp(now)
                .build();
        store.put(accountNumber, newAccount);
        ownerStore.put(accountNumber, userId);
        return newAccount;
    }

    @Override
    public AccountDto updateAccount(String accountNumber, AccountDto accountDto) {
        return null;
    }


    @Override
    public void deleteAccount(String accountNumber, String userId) {
        if (!store.containsKey(accountNumber)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        if (!userId.equals(ownerStore.get(accountNumber))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        store.remove(accountNumber);
        ownerStore.remove(accountNumber);
    }

    public void updateBalance(String accountId, BigDecimal newBalance) {
        AccountDto existing = store.get(accountId);
        if (existing != null) {
            store.put(accountId, AccountDto.builder()
                    .accountNumber(existing.getAccountNumber())
                    .sortCode(existing.getSortCode())
                    .name(existing.getName())
                    .accountType(existing.getAccountType())
                    .balance(newBalance)
                    .currency(existing.getCurrency())
                    .createdTimestamp(existing.getCreatedTimestamp())
                    .updatedTimestamp(OffsetDateTime.now())
                    .build());
        }
    }
}
