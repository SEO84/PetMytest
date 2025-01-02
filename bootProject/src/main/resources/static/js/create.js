// src/main/resources/static/js/create.js

document.addEventListener("DOMContentLoaded", () => {
    // ================================
    // 1) 새 펫 등록 모달 열기/닫기
    // ================================
    const newPetModal = document.getElementById("newPetModal");
    const closeModalBtn = document.getElementById("closeModalBtn");
    const savePetBtn = document.getElementById("savePetBtn");

    if (closeModalBtn) {
        closeModalBtn.addEventListener("click", () => {
            newPetModal.style.display = "none"; // 모달 닫기
        });
    }
    // 모달 바깥 검은 배경 클릭 시 닫히게 할 수도 있음
    window.addEventListener("click", (event) => {
        if (event.target === newPetModal) {
            newPetModal.style.display = "none";
        }
    });

    // ================================
    // 2) 새 펫 정보 저장
    // ================================
    if (savePetBtn) {
        savePetBtn.addEventListener("click", () => {
            const newPetName = document.getElementById("newPetName").value.trim();
            const newPetType = document.getElementById("newPetType").value.trim();
            const newPetAge = document.getElementById("newPetAge").value;
            const newPetGender = document.getElementById("newPetGender").value;
            const newPetWeight = document.getElementById("newPetWeight").value;
            const newPetPersonality = document.getElementById("newPetPersonality").value.trim();

            // 간단한 유효성 체크
            if (!newPetName) {
                alert("반려동물 이름을 입력하세요.");
                return;
            }
            if (!newPetType) {
                alert("반려동물 종류를 입력하세요.");
                return;
            }
            if (newPetAge === "" || newPetAge < 0) {
                alert("반려동물 나이를 올바르게 입력하세요.");
                return;
            }
            if (!newPetGender) {
                alert("반려동물 성별을 선택하세요.");
                return;
            }
            if (newPetWeight === "" || newPetWeight < 0) {
                alert("반려동물 몸무게를 올바르게 입력하세요.");
                return;
            }

            // 실제로는 AJAX or fetch로 서버에 펫 등록 요청 (예: /pet/register)
            fetch("/pet/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    name: newPetName,
                    type: newPetType,
                    age: parseInt(newPetAge),
                    gender: newPetGender,
                    weight: parseFloat(newPetWeight),
                    personality: newPetPersonality
                })
            })
                .then(response => {
                    if (!response.ok) throw new Error("서버 에러");
                    return response.json();
                })
                .then(data => {
                    // data 에 새로 생성된 petId, name 등이 반환되었다고 가정
                    const { petId, name } = data;

                    // 1) <select>에 새로 추가 (방장의 반려동물 선택)
                    const hostPetSelect = document.querySelector("select[th\\:field='*{hostPetId}']");
                    if (hostPetSelect) {
                        const newHostOption = document.createElement("option");
                        newHostOption.value = petId;
                        newHostOption.textContent = name;
                        hostPetSelect.appendChild(newHostOption);
                    }

                    // 2) <select>에 새로 추가 (추가 반려동물 선택)
                    const additionalPetSelects = document.querySelectorAll("select[name='additionalPetIds']");
                    additionalPetSelects.forEach(select => {
                        const newOption = document.createElement("option");
                        newOption.value = petId;
                        newOption.textContent = name;
                        select.appendChild(newOption);
                    });

                    // 3) 기존 userPets 배열에 새 펫 추가
                    userPets.push({ petId: petId, name: name });

                    // 4) 모달 닫기 & 폼 reset
                    newPetModal.style.display = "none";
                    document.getElementById("newPetName").value = "";
                    document.getElementById("newPetType").value = "";
                    document.getElementById("newPetAge").value = "";
                    document.getElementById("newPetGender").value = "";
                    document.getElementById("newPetWeight").value = "";
                    document.getElementById("newPetPersonality").value = "";

                    alert(`반려동물 [${name}]이 등록되었습니다!`);
                })
                .catch(error => {
                    alert("새 펫 등록 실패: " + error.message);
                });
        });
    }

    // ================================
    // 3) 추가 펫 선택 기능
    // ================================
    // 이 부분은 이미 create.html의 inline 스크립트에서 관리하고 있으므로 별도로 구현하지 않아도 됩니다.
});
