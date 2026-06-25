package guru.springframework.msscbrewery.web.controller;

import guru.springframework.msscbrewery.exceptions.ForbiddenException;
import guru.springframework.msscbrewery.services.AccountService;
import guru.springframework.msscbrewery.web.model.AccountDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;


@RestController
@RequestMapping("/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{accountId}")
    public ResponseEntity getAssociatedAccounts(@PathVariable("accountId") String accountId,
                                                Principal principal) {
        String authenticatedUser = principal.getName();
        try {
            AccountDto savedAccount = accountService.getAccountsById(accountId, authenticatedUser);
            if (savedAccount == null) {
                return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(savedAccount, HttpStatus.OK);
        } catch(Exception e) {
            System.out.println("Error class: " + e.getClass().getName());
            System.out.println("Error message: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto, Principal principal) {
        AccountDto created = accountService.createAccount(principal.getName(), accountDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PatchMapping("/{accountNumber}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable String accountNumber,
                                                    @RequestBody AccountDto accountDto,
                                                    Principal principal) throws  UserController.UserNotFoundException {

        AccountDto updated = accountService.updateAccount(accountNumber, accountDto);
        if (updated == null) {
            throw new UserController.UserNotFoundException();
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountNumber,
                                              Principal principal) throws ForbiddenException {
        try {
            accountService.deleteAccount(accountNumber, principal.getName());
        }catch(Exception e) {
            throw new ForbiddenException("Access Denied. You cannot delete this account");
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}