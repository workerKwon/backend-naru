package com.naru.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.naru.backend.dto.LoginDto;
import com.naru.backend.dto.UserDto;
import com.naru.backend.model.User;
import com.naru.backend.service.UserService;
import com.naru.backend.util.TokenUtil;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.registerUser(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDto loginDto) {
        try {
            return ResponseEntity.ok(userService.authenticateUser(loginDto));
        } catch (UsernameNotFoundException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        if (!TokenUtil.isValidToken(token)) {
            return ResponseEntity.badRequest().body("유효하지 않은 토큰 형식입니다.");
        }

        boolean verified = userService.verifyEmail(token);
        if (verified) {
            return ResponseEntity.ok("이메일이 성공적으로 인증되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("유효하지 않은 토큰입니다.");
        }
    }
}
