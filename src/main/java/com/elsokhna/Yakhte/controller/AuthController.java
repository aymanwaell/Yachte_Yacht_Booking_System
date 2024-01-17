package com.elsokhna.Yakhte.controller;

import com.elsokhna.Yakhte.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final IUserService userService;
}
