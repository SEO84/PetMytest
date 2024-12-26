document.addEventListener("DOMContentLoaded", () => {
    // 검색 버튼 기능
    const searchBtn = document.querySelector(".search-btn");
    const searchInput = document.querySelector(".search-section input");

    searchBtn.addEventListener("click", () => {
        const query = searchInput.value.trim();
        if (query) {
            alert(`'${query}' 검색을 실행합니다.`);
            // 검색 기능 추가
        } else {
            alert("검색어를 입력해주세요!");
        }
    });

    // 방 만들기 버튼 클릭 이벤트
    const createRoomBtn = document.querySelector(".create-room-btn");

    createRoomBtn.addEventListener("click", () => {
        alert("방 만들기 페이지로 이동합니다.");
        // 실제 방 만들기 페이지 이동 기능 추가
    });

    // 키워드 클릭 이벤트
    const keywords = document.querySelectorAll(".keywords span");

    keywords.forEach((keyword) => {
        keyword.addEventListener("click", () => {
            alert(`'${keyword.textContent}' 키워드로 필터링합니다.`);
            // 필터링 기능 추가
        });
    });
});
