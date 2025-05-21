package org.payments.gateway.service;

import org.payments.gateway.dto.UserDTO;

public interface AuthService {

    public UserDTO createUser(UserDTO userDTO);

}
