document.addEventListener('DOMContentLoaded', function() {
    // 검색 기능
    const searchBtn = document.querySelector('.search-btn');
    const searchInput = document.querySelector('.search-box input');

    searchBtn.addEventListener('click', function() {
        const searchTerm = searchInput.value;
        // 검색 로직 구현
        console.log('검색어:', searchTerm);
    });

    // 태그 필터링
    const tags = document.querySelectorAll('.tag');
    tags.forEach(tag => {
        tag.addEventListener('click', function() {
            // 태그 필터링 로직 구현
            console.log('선택된 태그:', tag.textContent);
        });
    });

    // 카드 클릭 이벤트
    const cards = document.querySelectorAll('.card');
    cards.forEach(card => {
        card.addEventListener('click', function() {
            // 카드 클릭 시 상세 페이지로 이동
            console.log('카드 클릭됨');
        });
    });
});
