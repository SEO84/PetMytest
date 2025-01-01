// detail-script.js
document.addEventListener('DOMContentLoaded', function() {
    // 카카오맵 초기화 (API 키 필요)
    function initMap() {
        if (typeof kakao !== 'undefined') {
            const mapContainer = document.querySelector('.map-placeholder');
            const options = {
                center: new kakao.maps.LatLng(37.5665, 126.9780),
                level: 3
            };
            const map = new kakao.maps.Map(mapContainer, options);

            // 마커 추가
            const marker = new kakao.maps.Marker({
                position: map.getCenter()
            });
            marker.setMap(map);
        }
    }

    // 신청하기 버튼 이벤트
    const applyButton = document.querySelector('.btn-apply');
    applyButton?.addEventListener('click', async function() {
        try {
            const response = await fetch('/api/matching/apply', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    matchingId: getMatchingIdFromUrl(),
                })
            });

            if (response.ok) {
                alert('신청이 완료되었습니다.');
                updateApplyButton();
            } else {
                throw new Error('신청 실패');
            }
        } catch (error) {
            alert('신청 중 오류가 발생했습니다.');
            console.error(error);
        }
    });

    // 신청 취소 버튼 이벤트
    const cancelButton = document.querySelector('.btn-chat');
    cancelButton?.addEventListener('click', async function() {
        if (confirm('신청을 취소하시겠습니까?')) {
            try {
                const response = await fetch('/api/matching/cancel', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        matchingId: getMatchingIdFromUrl(),
                    })
                });

                if (response.ok) {
                    alert('신청이 취소되었습니다.');
                    updateApplyButton();
                } else {
                    throw new Error('취소 실패');
                }
            } catch (error) {
                alert('취소 중 오류가 발생했습니다.');
                console.error(error);
            }
        }
    });

    // 버튼 상태 업데이트
    function updateApplyButton() {
        const applyButton = document.querySelector('.btn-apply');
        const cancelButton = document.querySelector('.btn-chat');

        // API로 현재 신청 상태 확인
        fetch(`/api/matching/status/${getMatchingIdFromUrl()}`)
            .then(response => response.json())
            .then(data => {
                if (data.isApplied) {
                    applyButton.style.display = 'none';
                    cancelButton.style.display = 'block';
                } else {
                    applyButton.style.display = 'block';
                    cancelButton.style.display = 'none';
                }
            });
    }

    // URL에서 매칭 ID 추출
    function getMatchingIdFromUrl() {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get('id');
    }

    // 초기화
    initMap();
    updateApplyButton();
});