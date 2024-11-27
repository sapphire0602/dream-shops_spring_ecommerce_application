package com.firstspringtutorial.dreamshops.service.user;

import com.firstspringtutorial.dreamshops.DTO.UserDto;
import com.firstspringtutorial.dreamshops.model.User;
import com.firstspringtutorial.dreamshops.request.CreateUserRequest;
import com.firstspringtutorial.dreamshops.request.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request , Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);

    User getAuthenticatedUser();
}
