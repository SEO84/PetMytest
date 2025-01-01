package com.busanit501.bootproject.controller;

import com.busanit501.bootproject.domain.MatchingRoom;
import com.busanit501.bootproject.domain.Pet;
import com.busanit501.bootproject.domain.RoomParticipant;
import com.busanit501.bootproject.domain.User;
import com.busanit501.bootproject.dto.MatchingRoomDTO;
import com.busanit501.bootproject.dto.ParticipantDTO;
import com.busanit501.bootproject.service.MatchingService;
import com.busanit501.bootproject.service.PetService;
import com.busanit501.bootproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/matching")
public class MatchingController {

    private final MatchingService matchingService;
    private final UserService userService;
    @Autowired
    private PetService petService;

    public MatchingController(MatchingService matchingService,
                              UserService userService) {
        this.matchingService = matchingService;
        this.userService = userService;
    }

    // 1) 매칭방 리스트
    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        // 1) 로그인 체크 (필요 시)
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/user/login";
        }
        // 2) 매칭방 목록 가져오기
        List<MatchingRoom> rooms = matchingService.getAllRooms();
        model.addAttribute("rooms", rooms);

        // 3) 폼에 바인딩할 DTO 준비
        model.addAttribute("matchingRoomDTO", new MatchingRoomDTO());

        return "matching/list"; // 이 페이지 안에 생성 폼 + 목록 함께 있음
    }


    // 2) 매칭방 생성 폼
    @GetMapping("/create")
    public String createForm(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/user/login";
        }
        // 모델에 담아서 뷰로 전달
        model.addAttribute("loginUser", loginUser);

        List<Pet> userPets = petService.findAllByUserId(loginUser.getUserId());
        model.addAttribute("userPets", userPets);

        return "matching/create";
    }

    // 2) 매칭방 생성 처리
    @PostMapping("/create")
    public String createSubmit(
            @ModelAttribute MatchingRoomDTO matchingRoomDTO,
            @RequestParam(required = false) String selectedPetIds,
            HttpSession session) {

        // 1) 로그인 유저 확인
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/user/login";
        }

        // 2) selectedPetIds 예: "1,2,5"
        List<Integer> petIdList = new ArrayList<>();
        if (selectedPetIds != null && !selectedPetIds.trim().isEmpty()) {
            for (String idStr : selectedPetIds.split(",")) {
                petIdList.add(Integer.parseInt(idStr));
            }
        }

        // 3) 매칭방 생성 로직
        matchingService.createRoom(loginUser, matchingRoomDTO);


        return "redirect:/matching/list";
    }


    // 3) 매칭방 상세
    @GetMapping("/detail/{roomId}")
    public String detail(@PathVariable Integer roomId, Model model, HttpSession session) {
        // 1) 매칭방 찾기
        MatchingRoom room = matchingService.getRoomById(roomId);
        if (room == null) {
            return "redirect:/matching/list"; // 방 없으면 목록으로
        }
        model.addAttribute("room", room);

        // 2) 해당 방 참가자 목록 가져오기
        List<RoomParticipant> allParticipants = matchingService.getParticipantsByRoomId(roomId);

        // 3) 'Accepted' 상태인 참가자만 필터링
        List<RoomParticipant> acceptedParticipants = allParticipants.stream()
                .filter(p -> p.getStatus() == RoomParticipant.ParticipantStatus.Accepted)
                .collect(Collectors.toList());

        // 4) RoomParticipant의 userId로 User 엔티티를 찾아, 표시할 DTO(이름 등)로 변환
        List<ParticipantDTO> participantDTOList = new ArrayList<>();
        for (RoomParticipant rp : acceptedParticipants) {
            User user = userService.findById(rp.getUserId());
            if (user != null) {
                ParticipantDTO dto = new ParticipantDTO();
                dto.setUserName(user.getName());   // 참가자 이름
                // dto.setProfileImagePath(user.getProfilePicture()); // 필요 시 이미지 경로
                participantDTOList.add(dto);
            }
        }

        // 5) Thymeleaf로 넘김
        model.addAttribute("participants", participantDTOList);

        return "matching/detail"; // detail.html (아래 템플릿)
    }


    // 참여 신청 (roomId 하나만 PathVariable로 받음)
    @PostMapping("/apply/{roomId}")
    public String apply(
            @PathVariable Integer roomId,
            @RequestParam(name="petId", required=false) Integer petId,
            HttpSession session
    ) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/user/login";
        }
        matchingService.applyRoom(roomId, loginUser.getUserId(), petId);
        return "redirect:/matching/detail/" + roomId;
    }


    // 참가 승인 (roomId, userId 두 개)
    @PostMapping("/accept/{roomId}/{userId}")
    public String accept(@PathVariable Integer roomId, @PathVariable Integer userId) {
        matchingService.acceptParticipant(roomId, userId);
        return "redirect:/matching/detail/" + roomId;
    }

    // 참가 거절 (roomId, userId 두 개)
    @PostMapping("/reject/{roomId}/{userId}")
    public String reject(@PathVariable Integer roomId, @PathVariable Integer userId) {
        matchingService.rejectParticipant(roomId, userId);
        return "redirect:/matching/detail/" + roomId;
    }
}
