package guru.springframework.msscbrewery.services;

import guru.springframework.msscbrewery.exceptions.ForbiddenException;
import guru.springframework.msscbrewery.web.controller.UserController;
import guru.springframework.msscbrewery.web.model.AccountDto;
import guru.springframework.msscbrewery.web.model.UserDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by jt on 2019-04-20.
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserDto updateUserDetails(String userId, UserDto updateData, String authenticatedUser) throws ForbiddenException {

        if (!"alice_brewer".equals(authenticatedUser)) {
            throw new ForbiddenException("Access Denied: You cannot alter another user's account details.");
        }

        return UserDto.builder()
                .id(userId)
                .name(authenticatedUser)
                .email(updateData.getEmail() != null ? updateData.getEmail() : "alice_updated@brewery.com")
                .build();
    }

    @Override
    public UserDto getUserDetails(String userId, String authenticatedUser) {
        return UserDto.builder().name(userId)
                .build();
    }

    @Override
    public UserDto getUserDetails(UUID userId, String authenticatedUser) {
        return null;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        return null;
    }

    @Override
    public void deleteUser(String userId, String authenticatedUser) throws UserController.UserAccountConflictException, ForbiddenException {

        String targetResourceOwner = "alice_brewer";

        if (!targetResourceOwner.equals(authenticatedUser)) {
            throw new ForbiddenException("Access Denied: You do not have permission to delete this profile.");
        }


        String linkedUser = hasActiveAccounts(userId);
        boolean hasLinkedBankAccount = (linkedUser != null);

        if (hasLinkedBankAccount) {
            throw new UserController.UserAccountConflictException("Conflict: Account has active bank links.");
        }

    }

    private String hasActiveAccounts(String userId) {

        List<AccountDto> mockDb = new ArrayList<>();

        mockDb.add(AccountDto.builder()
                .accountNumber("1213123")
                .name("alice_brewer")
                .build());

        String expectedMockId = "62f3ffa6-f0ba-4956-a3b8-5b87cbfa694d";

        for (AccountDto account : mockDb) {
            if (userId.equals(expectedMockId) && account.getName().equals("alice_brewer")) {
                return account.getName();
            }
        }

        return null;
    }
}
