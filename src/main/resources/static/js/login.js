const loginButton = document.getElementById('login-btn');

if (loginButton) {
    loginButton.addEventListener('click', event => {
    alert("버튼눌림");
    // JavaScript 값이나 객체를 JSON 문자열로 변환
    body = JSON.stringify({
      email: document.getElementById('username').value,
      password: document.getElementById('password').value,
    });
    function success(response) {
      alert('로그인 성공하였습니다.');
      localStorage.setItem('Authorization',response.headers.get('Authorization'));
      // 로그인 이전에는 아직 저장소에 인증값이 없기 때문에 여기서 대입해준다.
      authorization = '?Authorization='+localStorage.getItem('Authorization');
      location.replace('/user'+authorization);
    }
    function fail(response) {
      alert('로그인 실패하였습니다.');
      location.replace('/login');
    }
    httpRequest('POST','/login',body,success,fail);
  });
}