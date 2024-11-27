package com.firstspringtutorial.dreamshops.service.user;

import com.firstspringtutorial.dreamshops.DTO.UserDto;
import com.firstspringtutorial.dreamshops.exceptions.AlreadyExistsException;
import com.firstspringtutorial.dreamshops.exceptions.ResourceNotFoundException;
import com.firstspringtutorial.dreamshops.model.User;
import com.firstspringtutorial.dreamshops.repository.UserRepository;
import com.firstspringtutorial.dreamshops.request.CreateUserRequest;
import com.firstspringtutorial.dreamshops.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("USER NOT FOUND!"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository
                        .existsByEmail(request.getEmail()))
                .map(req ->{
                    User user = new User();
                    user.setEmail(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    return userRepository.save(user);
                }).orElseThrow(() -> new AlreadyExistsException( "Oops!" + request.getEmail() + "User Already Exists"));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new ResourceNotFoundException("USER NOT FOUND!"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository :: delete ,
                () -> {throw new ResourceNotFoundException("USER NOT FOUND");}
        );
    }

    @Override
    public UserDto convertUserToDto(User user){
        return modelMapper.map(user , UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }
}
