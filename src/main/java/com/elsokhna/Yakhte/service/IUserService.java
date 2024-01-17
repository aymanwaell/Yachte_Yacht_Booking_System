package com.elsokhna.Yakhte.service;

import com.elsokhna.Yakhte.exception.UserAlreadyExistsException;
import com.elsokhna.Yakhte.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IUserService {

    User registerUser (User user) throws UserAlreadyExistsException;
    List<User> getUsers();
    void deleteUser(String email);
    User getUser(String email);
}
