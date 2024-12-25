package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    /**
     * 메인 페이지
     */
    @GetMapping("/")
    public String home() {
        return "index"; // => templates/index.html
    }

    /**
     * 로그인 페이지
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // => templates/login.html
    }

    /**
     * 회원가입 페이지
     */
    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // => templates/signup.html
    }
}
