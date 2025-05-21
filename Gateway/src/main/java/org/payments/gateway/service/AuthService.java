package org.payments.gateway.service;

import org.payments.gateway.dto.UserDTO;
import org.payments.gateway.entity.User;


public interface AuthService {

    public UserDTO createUser(UserDTO userDTO);

    public User findByUsername(String username);

}
