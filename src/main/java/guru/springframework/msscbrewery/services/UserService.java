package guru.springframework.msscbrewery.services;

import guru.springframework.msscbrewery.exceptions.ForbiddenException;
import guru.springframework.msscbrewery.web.controller.UserController;
import guru.springframework.msscbrewery.web.model.UserDto;

import java.util.UUID;

public interface UserService {

    UserDto getUserDetails(UUID userId, String authenticatedUser);

    UserDto createUser(UserDto userDto);

    UserDto getUserDetails(String userId, String authenticatedUser);

    UserDto updateUserDetails(String userId, UserDto userDto, String authenticatedUser) throws ForbiddenException;

    void deleteUser(String userId, String authenticatedUser) throws UserController.UserAccountConflictException, ForbiddenException;

}