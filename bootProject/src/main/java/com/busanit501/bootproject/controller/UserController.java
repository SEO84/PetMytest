// src/main/java/com/busanit501/bootproject/controller/UserController.java
package com.busanit501.bootproject.controller;

import com.busanit501.bootproject.domain.User;
import com.busanit501.bootproject.dto.UserLoginDTO;
import com.busanit501.bootproject.dto.UserRegisterDTO;
import com.busanit501.bootproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // 생성자 주입
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 로그인 폼 페이지
     */
    @GetMapping("/login")
    public String loginForm(Model model) {
        // 로그인 폼을 위한 DTO 추가 (폼 바인딩을 원할 경우)
        model.addAttribute("userLoginDTO", new UserLoginDTO());
        return "login"; // login.html
    }

    /**
     * 로그인 처리
     */
    @PostMapping("/login")
    public String loginSubmit(@Valid @ModelAttribute("userLoginDTO") UserLoginDTO dto,
                              BindingResult bindingResult,
                              HttpSession session,
                              Model model) {
        if (bindingResult.hasErrors()) {
            return "login"; // 오류 메시지와 함께 로그인 폼 재표시
        }

        try {
            User user = userService.login(dto);
            // 로그인 성공 시 세션에 사용자 정보 저장
            session.setAttribute("loginUser", user);
            return "redirect:/matching/list";
        } catch (RuntimeException e) {
            // 로그인 실패 시 오류 메시지 전달
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    /**
     * 로그아웃 처리
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/user/login";
    }

    /**
     * 회원가입 폼 페이지
     */
    @GetMapping("/register")
    public String registerForm(Model model) {
        // 회원가입 폼을 위한 DTO 추가 (폼 바인딩을 원할 경우)
        model.addAttribute("userRegisterDTO", new UserRegisterDTO());
        return "register"; // register.html
    }

    /**
     * 회원가입 처리
     */
    @PostMapping("/register")
    public String registerSubmit(@Valid @ModelAttribute("userRegisterDTO") UserRegisterDTO dto,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            return "register"; // 오류 메시지와 함께 회원가입 폼 재표시
        }

        try {
            userService.registerWithPet(dto);
            // 회원가입 성공 시 로그인 페이지로 리다이렉트
            return "redirect:/user/login";
        } catch (RuntimeException e) {
            // 회원가입 실패 시 오류 메시지 전달
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
