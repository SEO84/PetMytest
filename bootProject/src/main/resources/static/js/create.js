// create.js

document.addEventListener("DOMContentLoaded", () => {
    // 주소찾기 버튼
    const findAddressBtn = document.getElementById("findAddressBtn");
    if (findAddressBtn) {
        findAddressBtn.addEventListener("click", () => {
            alert("주소찾기 기능은 추후 연결 예정입니다.");
        });
    }

    // ================================
    // 1) 새 펫 등록 모달 열기/닫기
    // ================================
    const addPetBtn = document.getElementById("addPetBtn");
    const newPetModal = document.getElementById("newPetModal");
    const closeModalBtn = document.getElementById("closeModalBtn");

    if (addPetBtn && newPetModal) {
        addPetBtn.addEventListener("click", () => {
            newPetModal.style.display = "flex";  // 모달 열기
        });
    }
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
    const savePetBtn = document.getElementById("savePetBtn");
    if (savePetBtn) {
        savePetBtn.addEventListener("click", () => {
            const newPetName = document.getElementById("newPetName").value;
            const newPetType = document.getElementById("newPetType").value;
            const newPetAge = document.getElementById("newPetAge").value;
            const newPetGender = document.getElementById("newPetGender").value;
            const newPetWeight = document.getElementById("newPetWeight").value;
            const newPetPersonality = document.getElementById("newPetPersonality").value;

            // 간단한 유효성 체크
            if (!newPetName) {
                alert("반려동물 이름을 입력하세요.");
                return;
            }

            // 실제로는 AJAX or fetch로 서버에 펫 등록 요청 (예: /pet/register)
            // 여기서는 예시로만 작성
            fetch("/pet/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    name: newPetName,
                    type: newPetType,
                    age: newPetAge,
                    gender: newPetGender,
                    weight: newPetWeight,
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

                    // 1) <select>에 새로 추가
                    const petSelect = document.getElementById("selectedPetId");
                    const newOption = document.createElement("option");
                    newOption.value = petId;
                    newOption.textContent = name;
                    petSelect.appendChild(newOption);

                    // 2) 모달 닫기 & 폼 reset
                    newPetModal.style.display = "none";
                    document.getElementById("newPetName").value = "";
                    document.getElementById("newPetType").value = "";
                    document.getElementById("newPetAge").value = "";
                    document.getElementById("newPetGender").value = "Male";
                    document.getElementById("newPetWeight").value = "";
                    document.getElementById("newPetPersonality").value = "";

                    alert(`반려동물 [${name}]이 등록되었습니다!`);
                })
                .catch(error => {
                    alert("새 펫 등록 실패: " + error.message);
                });
        });
    }
});
