package guru.springframework.msscbrewery.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String id;
    private String name;
    private AddressDto address;
    private String phoneNumber;
    private String email;
    private OffsetDateTime createdTimestamp;
    private OffsetDateTime updatedTimestamp;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddressDto {
        private String line1;
        private String line2;
        private String line3;
        private String town;
        private String county;
        private String postcode;
    }
}