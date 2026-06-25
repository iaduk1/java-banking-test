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
public class AccountDto {

    private String accountNumber;
    private String sortCode;
    private String name;
    private String accountType;
    private BigDecimal balance;
    private String currency;
    private OffsetDateTime createdTimestamp;
    private OffsetDateTime updatedTimestamp;
}