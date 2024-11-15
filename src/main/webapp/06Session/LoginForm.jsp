<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<!-- 
		JSP include method
		1. include directive
		2. action tag
	 -->
	<%--@ include file ="../Common/Link.jsp" --%>		
	<jsp:include page="../Common/Link.jsp" />
	
	<h2>login page</h2>

	<!-- 
		login form value 전송 후,
		회원 정보가 없다면 request 영역에 error msg 저장 후 현재 페이지로 forward
		request 영역은 forward 페이지까지 공유, 
		msg 출력 가능
	 -->
	<!-- 3항연산자 조건 분기 -->
	<span style="color: red; font-size: 1.2em;">
		<%=request.getAttribute("LoginErrMsg")==null ?
			"" : request.getAttribute("LoginErrMsg") %>
	</span>

<%
/* session 영역 속성값 확인
	값이 없다면 logout 상태 -> 로그인 폼을 웹브라우저에 출력
*/

if (session.getAttribute("UserId")==null) {
%>
	
	<script>
	/* 
		login Form 입력 값을 server로 전송하기 전 검증
		input value blank인지 확인
	*/
	function validateForm(form)	{

		/* 
			argument로 전달된 <form> tag의 DOM을 통해
			하위 태그 <input>에 접근 가능
		*/
		
		if (!form.user_id.value) {
			alert("Input id !");
			// 입력을 위해 focus 이동
			form.user_id.focus();
			// submit eventhandler에 false 반환 -> server로 전송은 취소(중단)
			return false;
		}

		if (form.user_pw.value == "") {
			alert("Input password !");
			form.user_pw.focus();
			return false;
		}
	}
	</script>
	<!-- 
		id, pw 입력 후 submit button click 시 -> action 경로로 폼 값 전송
		event handler 통해 javascript function call
		argument this = <form> tag DOM
	 -->

	<form action="LoginProcess.jsp" method="post" name="loginFrm"
		onsubmit = "return validateForm(this);">
		id : <input type="text" name="user_id" /><br />
		password : <input type="password" name="user_pw" /><br />
		<input type="submit" value = "login" />		 
	</form>

<%
} else {
%>
	<%=session.getAttribute("UserName")%> login success <br />
	<a href = "Logout.jsp">log out</a>
		
<% 		
}
%>
	

</body>
</html>




