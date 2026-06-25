package guru.springframework.msscbrewery.web.controller;

import guru.springframework.msscbrewery.services.TransactionService;
import guru.springframework.msscbrewery.web.model.TransactionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v1/accounts")
public class TransactionController {

    private final TransactionService transactionService;
    private static final String TRANSACTION_TYPE_DEPOSIT = "DEPOSIT";
    private static final String TRANSACTION_TYPE_WITHDRAWAL = "WITHDRAWAL";



    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    @PostMapping("/{accountId}/transactions")
    public ResponseEntity<TransactionDto> executeTransaction(@PathVariable("accountId") String accountId,
                                                     @RequestBody TransactionDto transactionDto,
                                                     Principal principal) {

        String authenticatedUser = principal.getName();

        if (!TRANSACTION_TYPE_DEPOSIT.equalsIgnoreCase(transactionDto.getType()) &&
                !TRANSACTION_TYPE_WITHDRAWAL.equalsIgnoreCase(transactionDto.getType())) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        TransactionDto confirmation = transactionService.executeTransaction(accountId, transactionDto, authenticatedUser);

        return new ResponseEntity<>(confirmation, HttpStatus.CREATED);
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity getTransactions(@PathVariable("accountId") String accountId,
                                          Principal principal) {
        return new ResponseEntity<>(transactionService.getTransactions(accountId, principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/{accountId}/transactions/{transactionId}")
    public ResponseEntity getTransaction(@PathVariable("accountId") String accountId,
                                         @PathVariable("transactionId") String transactionId,
                                         Principal principal) {
        return new ResponseEntity<>(transactionService.getTransactionById(accountId, transactionId, principal.getName()), HttpStatus.OK);
    }

}
