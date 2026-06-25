package guru.springframework.msscbrewery.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDto {

    private String accountId;
    private String transactionId;
    private BigDecimal amount;
    private String currency;
    private String type;
    private String reference;
    private String userId;
    private OffsetDateTime createdTimestamp;
}