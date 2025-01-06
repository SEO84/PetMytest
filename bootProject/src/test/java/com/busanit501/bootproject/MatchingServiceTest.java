package com.busanit501.bootproject;

import com.busanit501.bootproject.domain.MatchingRoom;
import com.busanit501.bootproject.domain.Pet;
import com.busanit501.bootproject.domain.RoomParticipant;
import com.busanit501.bootproject.domain.User;
import com.busanit501.bootproject.dto.MatchingRoomDTO;
import com.busanit501.bootproject.repository.MatchingRoomRepository;
import com.busanit501.bootproject.repository.PetRepository;
import com.busanit501.bootproject.repository.RoomParticipantRepository;
import com.busanit501.bootproject.repository.UserRepository;
import com.busanit501.bootproject.service.MatchingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class MatchingServiceTest {

    @InjectMocks
    private MatchingService matchingService;

    @Mock
    private MatchingRoomRepository matchingRoomRepository;

    @Mock
    private RoomParticipantRepository roomParticipantRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PetRepository petRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testCreateRoom_Success() {
//        // Given
//        User hostUser = new User();
//        hostUser.setUserId(1);
//        hostUser.setName("testuser");
//        hostUser.setEmail("test@example.com");
//
//        Pet hostPet = new Pet();
//        hostPet.setPetId(2);
//        hostPet.setName("Buddy");
//        hostPet.setUser(hostUser);
//
//        MatchingRoomDTO dto = new MatchingRoomDTO();
//        dto.setTitle("Test Room");
//        dto.setDescription("This is a test matching room.");
//        dto.setPlace("Seoul Park");
//        dto.setMeetingDate(LocalDate.now().plusDays(1));
//        dto.setMeetingTime(LocalTime.of(14, 0));
//        dto.setMaxParticipants(10);
//        dto.setHostPetId(2);
//        dto.setAdditionalPetIds(List.of());
//
//        when(petRepository.findByPetIdAndUserUserId(2, 1)).thenReturn(Optional.of(hostPet));
//
//        MatchingRoom savedRoom = new MatchingRoom();
//        savedRoom.setRoomId(1);
//        savedRoom.setUser(hostUser);
//        savedRoom.setHost(hostUser);
//        savedRoom.setHostPet(hostPet);
//        savedRoom.setTitle(dto.getTitle());
//        savedRoom.setDescription(dto.getDescription());
//        savedRoom.setPlace(dto.getPlace());
//        savedRoom.setMeetingDate(dto.getMeetingDate());
//        savedRoom.setMeetingTime(dto.getMeetingTime());
//        savedRoom.setMaxParticipants(dto.getMaxParticipants());
//
//        when(matchingRoomRepository.save(any(MatchingRoom.class))).thenReturn(savedRoom);
//
//        // When
////        MatchingRoom result = matchingService.createRoom(dto, hostUser);
//
//        // Then
//        assertNotNull(result);
//        assertEquals("Test Room", result.getTitle());
//        assertEquals("This is a test matching room.", result.getDescription());
//        verify(matchingRoomRepository, times(1)).save(any(MatchingRoom.class));
//        verify(roomParticipantRepository, times(1)).save(any(RoomParticipant.class));
//    }

//    @Test
//    public void testCreateRoom_HostPetNotFound() {
//        // Given
//        User hostUser = new User();
//        hostUser.setUserId(1);
//        hostUser.setName("testuser");
//        hostUser.setEmail("test@example.com");
//
//        MatchingRoomDTO dto = new MatchingRoomDTO();
//        dto.setTitle("Test Room");
//        dto.setDescription("This is a test matching room.");
//        dto.setPlace("Seoul Park");
//        dto.setMeetingDate(LocalDate.now().plusDays(1));
//        dto.setMeetingTime(LocalTime.of(14, 0));
//        dto.setMaxParticipants(10);
//        dto.setHostPetId(2);
//        dto.setAdditionalPetIds(List.of());
//
//        when(petRepository.findByPetIdAndUserUserId(2, 1)).thenReturn(Optional.empty());
//
//        // When & Then
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            matchingService.createRoom(dto, hostUser);
//        });
//
//        assertEquals("해당 반려동물을 찾을 수 없습니다.", exception.getMessage());
//        verify(matchingRoomRepository, never()).save(any(MatchingRoom.class));
//    }

    // 추가적인 테스트 케이스 작성...
}
