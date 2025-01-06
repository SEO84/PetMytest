package com.busanit501.bootproject.controller;

import com.busanit501.bootproject.domain.MatchingRoom;
import com.busanit501.bootproject.domain.Pet;
import com.busanit501.bootproject.domain.RoomParticipant;
import com.busanit501.bootproject.domain.User;
import com.busanit501.bootproject.dto.MatchingRoomDTO;
import com.busanit501.bootproject.exception.ResourceNotFoundException;
import com.busanit501.bootproject.repository.UserRepository;
import com.busanit501.bootproject.service.MatchingService;
import com.busanit501.bootproject.service.PetService;
import com.busanit501.bootproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/matching")
public class MatchingController {

    private final MatchingService matchingService;
    private final PetService petService;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public MatchingController(MatchingService matchingService,
                              PetService petService,
                              UserRepository userRepository,
                              UserService userService) {
        this.matchingService = matchingService;
        this.petService = petService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * 매칭방 목록 페이지
     */
    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        User loginUser = getManagedLoginUser(session);
        if (loginUser == null) {
            return "redirect:/user/login";
        }

        List<MatchingRoom> rooms = matchingService.getAllRooms();
        model.addAttribute("rooms", rooms);
        return "matching/list";
    }

    /**
     * 매칭방 생성 폼 페이지
     */
    @GetMapping("/create")
    public String createForm(HttpSession session, Model model) {
        User loginUser = getManagedLoginUser(session);
        if (loginUser == null) {
            return "redirect:/user/login";
        }

        // 사용자의 반려동물 목록을 조회
        List<Pet> userPets = petService.findAllByUserId(loginUser.getUserId());
        model.addAttribute("userPets", userPets);

        // 매칭방 생성 DTO를 모델에 추가
        MatchingRoomDTO dto = new MatchingRoomDTO();
        model.addAttribute("matchingRoomDTO", dto);

        return "matching/create";
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
        User loginUser = getManagedLoginUser(session);
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/user/login";
        }

        if (bindingResult.hasErrors()) {
            List<Pet> userPets = petService.findAllByUserId(loginUser.getUserId());
            model.addAttribute("userPets", userPets);
            return "matching/create";
        }

        try {
            matchingService.createRoom(dto, loginUser);
            redirectAttributes.addFlashAttribute("successMessage", "매칭방이 성공적으로 생성되었습니다.");
            return "redirect:/matching/list";
        } catch (RuntimeException e) {
            List<Pet> userPets = petService.findAllByUserId(loginUser.getUserId());
            model.addAttribute("userPets", userPets);
            model.addAttribute("errorMessage", e.getMessage());
            return "matching/create";
        }
    }


    /**
     * 매칭방 상세 페이지
     */
    @GetMapping("/detail/{id}")
    public String getMatchingRoomDetail(@PathVariable("id") Integer roomId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User loginUser = getManagedLoginUser(session);
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/user/login";
        }

        try {
            MatchingRoom room = matchingService.getRoomById(roomId);
            populateModelForDetail(model, room, loginUser);
            return "matching/detail";
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/matching/list";
        }
    }

    /**
     * 참가 신청 처리 핸들러
     */
    @PostMapping("/apply/{roomId}")
    public String applyRoom(@PathVariable("roomId") Integer roomId,
                            @RequestParam(value = "additionalPetIds", required = false) List<Integer> additionalPetIds,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        User loginUser = getManagedLoginUser(session);
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/user/login";
        }

        try {
            matchingService.applyRoom(roomId, loginUser.getUserId(), additionalPetIds);
            redirectAttributes.addFlashAttribute("successMessage", "참가 신청이 성공적으로 완료되었습니다.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/matching/detail/" + roomId;
    }

    /**
     * 세션에서 로그인된 사용자를 매니지드 엔티티로 재로드합니다.
     */
    private User getManagedLoginUser(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return null;
        }
        return userRepository.findById(loginUser.getUserId())
                .orElse(null);
    }

    /**
     * 매칭방 상세 페이지를 위한 모델을 설정합니다.
     *
     * @param model     모델 객체
     * @param room      매칭방 객체
     * @param loginUser 로그인 사용자 객체
     */
    private void populateModelForDetail(Model model, MatchingRoom room, User loginUser) {
        model.addAttribute("room", room);
        model.addAttribute("loginUser", loginUser); // 로그인 사용자 정보 추가

        List<RoomParticipant> participants = matchingService.getParticipantsByRoomId(room.getRoomId());

        // 참가 상태별로 분류
        List<RoomParticipant> pendingParticipants = matchingService.filterParticipants(participants, RoomParticipant.ParticipantStatus.Pending);
        List<RoomParticipant> acceptedParticipants = matchingService.filterParticipants(participants, RoomParticipant.ParticipantStatus.Accepted);

        model.addAttribute("pendingParticipants", pendingParticipants);
        model.addAttribute("acceptedParticipants", acceptedParticipants);

        boolean isHost = room.getHost().getUserId().equals(loginUser.getUserId());
        model.addAttribute("isHost", isHost);

        if (!isHost) {
            // 호스트가 아닌 경우, 사용자의 반려동물 목록 추가
            List<Pet> userPets = petService.findAllByUserId(loginUser.getUserId());
            model.addAttribute("userPets", userPets);
        }
    }
    @PostMapping("/accept/{roomId}/{userId}")
    public String acceptParticipant(@PathVariable("roomId") Integer roomId,
                                    @PathVariable("userId") Integer userId,
                                    RedirectAttributes redirectAttributes) {
        try {
            matchingService.acceptParticipant(roomId, userId); // 서비스 메서드 호출
            redirectAttributes.addFlashAttribute("successMessage", "참가자가 승인되었습니다.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/matching/detail/" + roomId;
    }

    @PostMapping("/reject/{roomId}/{userId}")
    public String rejectParticipant(@PathVariable("roomId") Integer roomId,
                                    @PathVariable("userId") Integer userId,
                                    RedirectAttributes redirectAttributes) {
        try {
            matchingService.rejectParticipant(roomId, userId); // 서비스 메서드 호출
            redirectAttributes.addFlashAttribute("successMessage", "참가자가 거절되었습니다.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/matching/detail/" + roomId;
    }

    /**
     * 매칭방의 호스트인지 확인합니다.
     *
     * @param roomId            매칭방 ID
     * @param session           HTTP 세션
     * @param redirectAttributes 리다이렉트 시 속성 전달 객체
     * @return 호스트인 경우 true, 아니면 false
     */
    private boolean isAuthorizedHost(Integer roomId, HttpSession session, RedirectAttributes redirectAttributes) {
        User loginUser = getManagedLoginUser(session);
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return false;
        }

        MatchingRoom room = matchingService.getRoomById(roomId);
        if (room == null || !room.getHost().getUserId().equals(loginUser.getUserId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "접근 권한이 없습니다.");
            return false;
        }

        return true;
    }

}
