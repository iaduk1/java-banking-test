package guru.springframework.msscbrewery.web.controller;

import guru.springframework.msscbrewery.exceptions.ForbiddenException;
import guru.springframework.msscbrewery.services.UserService;
import guru.springframework.msscbrewery.web.model.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;


/**
 * Created by jt on 2019-04-20.
 */
@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody UserDto userDto) throws Exception {

        UserDto newUser = userService.createUser(userDto);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> patchUserDetails(@PathVariable("userId") String userId,
                                                    @RequestBody UserDto updateData,
                                                    Principal principal) throws UserNotFoundException, ForbiddenException {

        String authenticatedUser = principal.getName();
        UserDto updateUser;

        try {
            updateUser = userService.updateUserDetails(userId, updateData, authenticatedUser);
        } catch(Exception e) {
            throw new UserNotFoundException();
        }

        return new ResponseEntity<>(updateUser, HttpStatus.OK);

    }

    @GetMapping("/{userId}")
    public ResponseEntity getUserDetails(@PathVariable("userId") String userId,
                                         Principal principal) throws UserNotFoundException {

        String authenticatedUser = principal.getName();
        UserDto updateUser;
        try {
            updateUser = userService.getUserDetails(userId, authenticatedUser);
        }catch(Exception e){
            throw new UserNotFoundException();
        }

        return new ResponseEntity<>(updateUser, HttpStatus.OK);

    }


    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserUserWithLinkedAccount(@PathVariable("userId") String userId,
                                           Principal principal) throws UserNotFoundException, ForbiddenException {

        String authenticatedUser = principal.getName();
        try {
            userService.deleteUser(userId, authenticatedUser);
        } catch(Exception e) {
            throw new UserNotFoundException();
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User profile not found.")
    public static class UserNotFoundException extends Exception {

    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Cannot delete user. Please close your active bank account first.")
    public static class UserAccountConflictException extends Exception {
        public UserAccountConflictException(String message) {
            super(message);
        }
    }
}
