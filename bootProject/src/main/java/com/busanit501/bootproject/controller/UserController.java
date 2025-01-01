package com.busanit501.bootproject.controller;

import com.busanit501.bootproject.domain.User;
import com.busanit501.bootproject.dto.UserLoginDTO;
import com.busanit501.bootproject.dto.UserRegisterDTO;
import com.busanit501.bootproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 로그인 폼
    @GetMapping("/login")
    public String loginForm() {
        return "user/login";  // templates/user/login.html
    }

    // 로그인 처리
    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute UserLoginDTO loginDTO, HttpSession session, Model model) {
        User user = userService.login(loginDTO);
        if (user == null) {
            model.addAttribute("error", "이메일 또는 비밀번호가 올바르지 않습니다.");
            return "user/login";
        }
        session.setAttribute("loginUser", user);
        return "redirect:/matching/list";
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }

    // 회원가입 폼
    @GetMapping("/register")
    public String registerForm() {
        return "user/register"; // templates/user/register.html
    }

    // 회원가입 + 펫 등록 처리
    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute UserRegisterDTO registerDTO, Model model) {
        try {
            userService.registerWithPet(registerDTO);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "user/register";
        }
        return "redirect:/user/login";
    }
}
