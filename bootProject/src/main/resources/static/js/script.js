// script.js
document.addEventListener('DOMContentLoaded', function() {
    // 검색 기능
    const searchInput = document.querySelector('.search-input');
    searchInput?.addEventListener('input', function(e) {
        const searchTerm = e.target.value;
        // 검색 로직 구현
        console.log('Searching for:', searchTerm);
    });

    // 방 만들기 버튼
    const createPostButton = document.querySelector('.create-post');
    createPostButton?.addEventListener('click', function() {
        window.location.href = '/create-matching.html';
    });

    // 매칭 등록 폼 제출
    const matchingForm = document.getElementById('matchingForm');
    matchingForm?.addEventListener('submit', function(e) {
        e.preventDefault();

        const formData = new FormData(matchingForm);
        const matchingData = {
            title: formData.get('title'),
            content: formData.get('content'),
            location: formData.get('location'),
            meetingTime: formData.get('meetingTime'),
            meetingDate: formData.get('meetingDate')
        };

        // API 호출 로직
        console.log('Submitting matching data:', matchingData);
        // submitMatchingData(matchingData);
    });

    // 주소 검색 버튼
    const searchLocationButton = document.querySelector('.search-location');
    searchLocationButton?.addEventListener('click', function() {
        // 카카오 주소 검색 API 연동
        // new daum.Postcode({
        //     oncomplete: function(data) {
        //         document.querySelector('input[name="location"]').value = data.address;
        //     }
        // }).open();
    });

    // 반려동물 추가 버튼
    const addPetButton = document.querySelector('.add-pet');
    addPetButton?.addEventListener('click', function() {
        // 반려동물 추가 모달 또는 폼 표시
        console.log('Add pet clicked');
    });

    // 취소 버튼
    const cancelButton = document.querySelector('.cancel-btn');
    cancelButton?.addEventListener('click', function() {
        if(confirm('작성을 취소하시겠습니까?')) {
            window.location.href = '/matching-list.html';
        }
    });

    // 이미지 업로드 미리보기
    function setupImagePreview() {
        const imageInput = document.querySelector('input[type="file"]');
        const previewContainer = document.querySelector('.post-image');

        imageInput?.addEventListener('change', function(e) {
            const file = e.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    previewContainer.style.backgroundImage = `url(${e.target.result})`;
                }
                reader.readAsDataURL(file);
            }
        });
    }

    // 시간 입력 포맷팅
    function formatTime(input) {
        input.addEventListener('blur', function(e) {
            let time = e.target.value;
            if (time.length === 4) {
                time = time.replace(/(\d{2})(\d{2})/, "$1:$2");
                e.target.value = time;
            }
        });
    }

    // 유틸리티 함수들
    const utils = {
        formatDate: function(date) {
            return new Date(date).toLocaleDateString('ko-KR', {
                month: 'numeric',
                day: 'numeric'
            });
        },

        validateForm: function(formData) {
            const required = ['title', 'location', 'meetingTime'];
            for (let field of required) {
                if (!formData.get(field)) {
                    alert(`${field}을(를) 입력해주세요.`);
                    return false;
                }
            }
            return true;
        }
    };
});