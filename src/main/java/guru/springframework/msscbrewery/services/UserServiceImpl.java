package guru.springframework.msscbrewery.services;

import guru.springframework.msscbrewery.exceptions.ForbiddenException;
import guru.springframework.msscbrewery.web.model.UserDto;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by jt on 2019-04-20.
 */
@Service
public class UserServiceImpl implements UserService {


    private static final Map<String, UserDto> store = new HashMap<>();

    @Override
    public UserDto updateUserDetails(String userId, UserDto updateData, String authenticatedUser) throws ForbiddenException {

        if (!userId.equals(authenticatedUser)) {
            throw new ForbiddenException("Access Denied: You cannot alter another user's account details.");
        }
        UserDto currentUser = store.get(userId);
        UserDto updatedUser = UserDto.builder()
                .id(userId)
                .name(updateData.getName())
                .address(updateData.getAddress())
                .phoneNumber(updateData.getPhoneNumber())
                .email(updateData.getPhoneNumber())
                .createdTimestamp(currentUser.getCreatedTimestamp())
                .updatedTimestamp(OffsetDateTime.now())
                .build();
        store.put(userId, updatedUser);

        return updatedUser;
    }

    @Override
    public UserDto getUserDetails(String userId, String authenticatedUser) {
        return store.get(userId);
    }

    @Override
    public UserDto createUser(UserDto userDto) {

        String id = "usr-" + UUID.randomUUID().toString().replace("-", "").substring(0, 7);
        OffsetDateTime now = OffsetDateTime.now();

        UserDto newUser = UserDto.builder()
                .id(id)
                .name(userDto.getName())
                .address(userDto.getAddress())
                .email(userDto.getEmail())
                .createdTimestamp(now)
                .updatedTimestamp(now)
                .build();

        store.put(id, newUser);
        return newUser;
    }

    @Override
    public void deleteUser(String userId, String authenticatedUser) throws ForbiddenException {


        if (!userId.equals(authenticatedUser)) {
            throw new ForbiddenException("Access Denied: You do not have permission to delete this profile.");
        }
        if (store.get(userId) == null) {
            throw new ForbiddenException("User does not exist");
        }

        store.remove(userId);
    }
}
