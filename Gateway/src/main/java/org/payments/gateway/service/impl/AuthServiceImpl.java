package org.payments.gateway.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.payments.gateway.dto.UserDTO;
import org.payments.gateway.entity.User;
import org.payments.gateway.exception.AuthException;
import org.payments.gateway.repository.UserRepository;
import org.payments.gateway.service.AuthService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private  final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDTO createUser(UserDTO newUser) {
        log.info("Creating new user for : {}", newUser.getUsername());

        String username = newUser.getUsername();

        if(userRepository.findByUserName(newUser.getUsername()).isPresent())
            throw new AuthException("Username :" + username + " already exists", 409);

        try{

        String encodedPassword = bCryptPasswordEncoder.encode(newUser.getPassword());
        User user = User.builder()
                .userName(username)
                .password(encodedPassword)
                .build();

        User savedUser = userRepository.save(user);

        log.info("Successfully created new user for: {}", username);

        return UserDTO.builder().username(savedUser.getUserName()).id(savedUser.getId()).build();

        } catch (Exception e){
            log.error("Error occurred with cause: {}", e.getMessage());
            throw new AuthException("Error occurred while creating user", 500);
        }


    }

    @Override
    public User findByUsername(String username) {
        Optional<User> user = userRepository.findByUserName(username);
        if(user.isPresent())
            return user.get();
        else{
            throw new AuthException("Username: "+ username + " does not exist", 404);
        }
    }
}
