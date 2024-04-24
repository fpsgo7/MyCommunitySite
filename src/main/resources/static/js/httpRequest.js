/* 해당 자바스크립트는 다른 자바스크립트 파일에서 활용하기위한 변수와
 함수를 작성한 곳이기 때문에 상단에 위치하여야한다. */
// location.replace 사용시 뒤에 토큰 값을 가져와 붙이기위한 문자열이다.
var authorization = '?Authorization='+localStorage.getItem('Authorization');

// http 요청을 위한 함수이다.
function httpRequest(method,url,body,success,fail){
  fetch(url,{
    method: method,
    headers: {
      'Content-Type':  'application/json',
      'Authorization': localStorage.getItem('Authorization')
    },
    body: body

  }).then(response => {

  if(response.status === 200 || response.status === 201 ){
    return success(response);
  }
  else {
    return fail(response);
  }
  });
}