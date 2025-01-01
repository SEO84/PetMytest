package com.busanit501.bootproject.controller;

import com.busanit501.bootproject.domain.Pet;
import com.busanit501.bootproject.domain.User;
import com.busanit501.bootproject.dto.PetDTO;
import com.busanit501.bootproject.service.PetService;
import com.busanit501.bootproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pet")
public class PetController {

    private final PetService petService;
    private final UserService userService;

    public PetController(PetService petService, UserService userService) {
        this.petService = petService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerPet(@RequestBody PetDTO dto, HttpSession session) {
        // 1) 세션에서 로그인 유저 찾기
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
        }

        // 2) DB 등록
        Pet pet = petService.createPet(loginUser.getUserId(), dto);
        // 3) JSON으로 petId, petName 등 반환
        Map<String, Object> result = new HashMap<>();
        result.put("petId", pet.getPetId());
        result.put("name", pet.getName());
        return ResponseEntity.ok(result);
    }
}

