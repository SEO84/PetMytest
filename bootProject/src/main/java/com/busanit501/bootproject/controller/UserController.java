package com.busanit501.bootproject.controller;

import com.busanit501.bootproject.domain.User;
import com.busanit501.bootproject.dto.UserLoginDTO;
import com.busanit501.bootproject.dto.UserRegisterDTO;
import com.busanit501.bootproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 로그인 폼 페이지
     */
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("userLoginDTO", new UserLoginDTO());
        return "user/login"; // src/main/resources/templates/user/login.html
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
            return "user/login"; // 유효성 검사 오류 시 로그인 폼 재표시
        }

        try {
            User user = userService.login(dto);
            session.setAttribute("loginUser", user); // 세션에 사용자 정보 저장
            return "redirect:/matching/list"; // 로그인 성공 후 매칭방 목록으로 이동
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "user/login"; // 로그인 실패 시 로그인 폼 재표시
        }
    }

    /**
     * 회원가입 폼 페이지
     */
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("userRegisterDTO", new UserRegisterDTO());
        return "user/register"; // src/main/resources/templates/user/register.html
    }

    /**
     * 회원가입 처리
     */
    @PostMapping("/register")
    public String registerSubmit(@Valid @ModelAttribute("userRegisterDTO") UserRegisterDTO dto,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            return "user/register"; // 유효성 검사 오류 시 회원가입 폼 재표시
        }

        try {
            userService.registerWithPet(dto);
            return "redirect:/user/login"; // 회원가입 성공 후 로그인 페이지로 이동
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "user/register"; // 회원가입 실패 시 회원가입 폼 재표시
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
     * 홈 페이지 (로그인 후 접근)
     */
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        User loginUser = getLoginUser(session);
        if (loginUser == null) {
            return "redirect:/user/login";
        }
        model.addAttribute("user", loginUser);
        return "home"; // src/main/resources/templates/home.html
    }

    // --------------------- 내부 헬퍼 메서드 ---------------------

    /**
     * 세션에서 로그인한 사용자 정보를 가져옴
     */
    private User getLoginUser(HttpSession session) {
        return (User) session.getAttribute("loginUser");
    }
}
