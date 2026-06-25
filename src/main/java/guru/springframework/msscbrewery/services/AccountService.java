package guru.springframework.msscbrewery.services;

import guru.springframework.msscbrewery.web.model.AccountDto;


import java.math.BigDecimal;
import java.util.List;


public interface AccountService {

    List<AccountDto> getAccounts(String userId);

    AccountDto getAccountsById(String accountId, String authenticatedUser);

    AccountDto createAccount(String name, AccountDto accountDto);

    AccountDto updateAccount(String accountNumber, AccountDto accountDto);

    void deleteAccount(String accountNumber, String name);

    void updateAccount(String accountId, BigDecimal subtract);

    void updateBalance(String accountId, BigDecimal subtract);
}
