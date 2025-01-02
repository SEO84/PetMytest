// MatchingController.java
package com.busanit501.bootproject.controller;

import com.busanit501.bootproject.domain.MatchingRoom;
import com.busanit501.bootproject.domain.Pet;
import com.busanit501.bootproject.domain.RoomParticipant;
import com.busanit501.bootproject.domain.User;
import com.busanit501.bootproject.dto.MatchingRoomDTO;
import com.busanit501.bootproject.service.MatchingService;
import com.busanit501.bootproject.service.PetService;
import com.busanit501.bootproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/matching")
public class MatchingController {

    private final MatchingService matchingService;
    private final UserService userService;
    private final PetService petService;

    public MatchingController(MatchingService matchingService,
                              UserService userService,
                              PetService petService) {
        this.matchingService = matchingService;
        this.userService = userService;
        this.petService = petService;
    }

    /**
     * 매칭방 목록 페이지
     */
    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/user/login";
        }

        List<MatchingRoom> rooms = matchingService.getAllRooms();
        model.addAttribute("rooms", rooms);

        return "matching/list"; // list.html
    }

    /**
     * 매칭방 생성 폼 페이지
     */
    @GetMapping("/create")
    public String createForm(HttpSession session, Model model) {
        // 로그인 체크
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/user/login";
        }

        // 사용자의 반려동물 목록 가져오기
        List<Pet> userPets = petService.findAllByUserId(loginUser.getUserId());
        model.addAttribute("userPets", userPets);

        // 매칭방 생성 폼을 위한 DTO 추가
        model.addAttribute("matchingRoomDTO", new MatchingRoomDTO());

        return "matching/create"; // create.html
    }

    /**
     * 매칭방 생성 처리
     */
    @PostMapping("/create")
    public String createSubmit(@Valid @ModelAttribute("matchingRoomDTO") MatchingRoomDTO dto,
                               BindingResult bindingResult,
                               HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/user/login";
        }

        if (bindingResult.hasErrors()) {
            // 사용자의 반려동물 목록 다시 모델에 추가
            List<Pet> userPets = petService.findAllByUserId(loginUser.getUserId());
            model.addAttribute("userPets", userPets);
            return "matching/create"; // 오류 메시지와 함께 폼 재표시
        }

        try {
            matchingService.createRoom(dto, loginUser);
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            // 사용자의 반려동물 목록 다시 모델에 추가
            List<Pet> userPets = petService.findAllByUserId(loginUser.getUserId());
            model.addAttribute("userPets", userPets);
            return "matching/create"; // 오류 메시지와 함께 폼 재표시
        }

        return "redirect:/matching/list";
    }

    /**
     * 매칭방 상세 페이지
     */
    @GetMapping("/detail/{roomId}")
    public String detail(@PathVariable Integer roomId,
                         Model model,
                         HttpSession session) {
        MatchingRoom room = matchingService.getRoomById(roomId);
        if (room == null) {
            model.addAttribute("errorMessage", "매칭방을 찾을 수 없습니다.");
            return "error"; // error.html
        }
        model.addAttribute("room", room);

        // 참가자 목록 조회
        List<RoomParticipant> participants = matchingService.getParticipantsByRoomId(roomId);

        // 방장 제외 및 Rejected 상태 제외
        List<RoomParticipant> filteredParticipants = participants.stream()
                .filter(p -> !p.getUser().getUserId().equals(room.getHostPet().getUser().getUserId()))
                .filter(p -> p.getStatus() != RoomParticipant.ParticipantStatus.Rejected)
                .collect(Collectors.toList());
        model.addAttribute("participants", filteredParticipants);

        // 로그인 유저 확인
        User loginUser = (User) session.getAttribute("loginUser");
        boolean isHost = false;
        if (loginUser != null &&
                room.getHostPet().getUser().getUserId().equals(loginUser.getUserId())) {
            // 방장인 경우
            isHost = true;
        }
        model.addAttribute("isHost", isHost);

        if (isHost) {
            // 방장인 경우 승인 대기 중인 참가자만 표시
            List<RoomParticipant> pendingParticipants = filteredParticipants.stream()
                    .filter(p -> p.getStatus() == RoomParticipant.ParticipantStatus.Pending)
                    .collect(Collectors.toList());
            model.addAttribute("pendingParticipants", pendingParticipants);

            // 방장인 경우 승인된 참가자도 표시
            List<RoomParticipant> acceptedParticipants = filteredParticipants.stream()
                    .filter(p -> p.getStatus() == RoomParticipant.ParticipantStatus.Accepted)
                    .collect(Collectors.toList());
            model.addAttribute("acceptedParticipants", acceptedParticipants);
        } else if (loginUser != null) {
            // 일반 유저인 경우 승인된 참가자만 표시
            List<RoomParticipant> acceptedParticipants = filteredParticipants.stream()
                    .filter(p -> p.getStatus() == RoomParticipant.ParticipantStatus.Accepted)
                    .collect(Collectors.toList());
            model.addAttribute("acceptedParticipants", acceptedParticipants);

            // 일반 유저라면, 반려동물 목록을 넘겨준다.
            List<Pet> userPets = petService.findAllByUserId(loginUser.getUserId());
            model.addAttribute("userPets", userPets);
        }

        return "matching/detail"; // detail.html
    }

    /**
     * 참여 신청 (다중 펫)
     * - additionalPetIds: 여러 반려동물 ID를 리스트로 받음
     */
    @PostMapping("/apply/{roomId}")
    public String apply(
            @PathVariable Integer roomId,
            @RequestParam(required = false) List<Integer> additionalPetIds,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/user/login";
        }

        try {
            // 여러 펫이면 여러 번 applyRoom 호출
            if (additionalPetIds == null || additionalPetIds.isEmpty()) {
                // 최소 하나의 펫 선택을 요구
                throw new RuntimeException("적어도 하나의 반려동물을 선택해야 합니다.");
            } else {
                for (Integer pId : additionalPetIds) {
                    matchingService.applyRoom(roomId, loginUser.getUserId(), pId);
                }
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/matching/detail/" + roomId;
        }

        return "redirect:/matching/detail/" + roomId;
    }

    /**
     * 참가 승인
     */
    @PostMapping("/accept/{roomId}/{userId}/{petId}")
    public String accept(@PathVariable Integer roomId,
                         @PathVariable Integer userId,
                         @PathVariable Integer petId,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/user/login";
        }

        // 방 정보 및 방장 확인
        MatchingRoom room = matchingService.getRoomById(roomId);
        if (room == null || !room.getHostPet().getUser().getUserId().equals(loginUser.getUserId())) {
            // 방이 없거나 방장이 아니라면 접근 금지
            redirectAttributes.addFlashAttribute("errorMessage", "접근 권한이 없습니다.");
            return "redirect:/matching/list";
        }

        try {
            matchingService.acceptParticipant(roomId, userId, petId);
        } catch (RuntimeException e) {
            // 예외 처리 (예: 현재 인원이 초과된 경우)
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/matching/detail/" + roomId;
    }

    /**
     * 참가 거절
     */
    @PostMapping("/reject/{roomId}/{userId}/{petId}")
    public String reject(@PathVariable Integer roomId,
                         @PathVariable Integer userId,
                         @PathVariable Integer petId,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/user/login";
        }

        // 방 정보 및 방장 확인
        MatchingRoom room = matchingService.getRoomById(roomId);
        if (room == null || !room.getHostPet().getUser().getUserId().equals(loginUser.getUserId())) {
            // 방이 없거나 방장이 아니라면 접근 금지
            redirectAttributes.addFlashAttribute("errorMessage", "접근 권한이 없습니다.");
            return "redirect:/matching/list";
        }

        try {
            matchingService.rejectParticipant(roomId, userId, petId);
        } catch (RuntimeException e) {
            // 예외 처리
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/matching/detail/" + roomId;
    }
}
