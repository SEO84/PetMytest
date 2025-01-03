// src/main/resources/static/js/detail.js

document.addEventListener('DOMContentLoaded', function() {
    // ================================
    // 1) 카카오맵 초기화 (매칭방의 실제 장소 좌표 필요)
    // ================================
    function initMap() {
        if (typeof kakao !== 'undefined') {
            const mapContainer = document.querySelector('.map-placeholder');
            const place = /*[[${room.place}]]*/ '서울특별시';
            const geocoder = new kakao.maps.services.Geocoder();

            geocoder.addressSearch(place, function(result, status) {
                if (status === kakao.maps.services.Status.OK) {
                    const coords = new kakao.maps.LatLng(result[0].y, result[0].x);

                    const map = new kakao.maps.Map(mapContainer, {
                        center: coords,
                        level: 3
                    });

                    const marker = new kakao.maps.Marker({
                        position: coords
                    });
                    marker.setMap(map);

                    const infowindow = new kakao.maps.InfoWindow({
                        content: `<div style="padding:5px;">${place}</div>`
                    });
                    infowindow.open(map, marker);
                } else {
                    console.error('주소 검색 실패:', status);
                }
            });
        }
    }

    // ================================
    // 2) 카카오맵 초기화 호출
    // ================================
    initMap();
});
