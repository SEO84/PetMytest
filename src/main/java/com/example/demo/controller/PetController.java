package com.example.demo.controller;

import com.example.demo.dto.PetDTO;
import com.example.demo.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    /**
     * 반려동물 등록
     */
    @PostMapping
    public ResponseEntity<PetDTO> createPet(@RequestBody PetDTO petDTO) {
        PetDTO created = petService.createPet(petDTO);
        return ResponseEntity
                .created(URI.create("/api/pets/" + created.getPetId()))
                .body(created);
    }

    /**
     * 반려동물 수정
     */
    @PutMapping("/{petId}")
    public ResponseEntity<PetDTO> updatePet(@PathVariable Integer petId,
                                            @RequestBody PetDTO petDTO) {
        petDTO.setPetId(petId);
        PetDTO updated = petService.updatePet(petDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * 반려동물 삭제
     */
    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePet(@PathVariable Integer petId) {
        petService.deletePet(petId);
        return ResponseEntity.noContent().build();
    }

    // 필요하다면 반려동물 조회 API 추가
    // @GetMapping("/{petId}")
    // public ResponseEntity<PetDTO> getPetById(@PathVariable Integer petId) { ... }
}
