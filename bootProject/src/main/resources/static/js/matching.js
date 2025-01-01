// src/main/resources/static/js/matching.js

// 참가 승인 및 거절 시 확인 창 띄우기
document.addEventListener('DOMContentLoaded', function () {
    const approveForms = document.querySelectorAll('form[action$="/approve"]');
    const rejectForms = document.querySelectorAll('form[action$="/reject"]');

    approveForms.forEach(form => {
        form.addEventListener('submit', function (e) {
            if (!confirm('참가 신청을 승인하시겠습니까?')) {
                e.preventDefault();
            }
        });
    });

    rejectForms.forEach(form => {
        form.addEventListener('submit', function (e) {
            if (!confirm('참가 신청을 거절하시겠습니까?')) {
                e.preventDefault();
            }
        });
    });
});
