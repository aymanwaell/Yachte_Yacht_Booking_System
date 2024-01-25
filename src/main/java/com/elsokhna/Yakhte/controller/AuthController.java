package com.elsokhna.Yakhte.controller;

import com.elsokhna.Yakhte.exception.UserAlreadyExistsException;
import com.elsokhna.Yakhte.model.User;
import com.elsokhna.Yakhte.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IUserService userService;

    @PostMapping("register_user")
    public ResponseEntity<?> registerUser(User user){
        try {
            userService.registerUser(user);
            return ResponseEntity.ok("Registration Successful!");
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

}
