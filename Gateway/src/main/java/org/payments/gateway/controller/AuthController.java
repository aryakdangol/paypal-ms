package org.payments.gateway.controller;

import lombok.AllArgsConstructor;
import org.payments.gateway.dto.UserDTO;
import org.payments.gateway.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO){
        UserDTO newUser = authService.createUser(userDTO);
        return new ResponseEntity<UserDTO>(newUser, HttpStatus.CREATED);
    }

}
