<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	// 로그아웃 처리 1: session 속성 명 지정 삭제
	session.removeAttribute("UserId");
	session.removeAttribute("UserName");

	/*
		로그아웃 처리 2: session 전체 속성 일괄 삭제
	*/
	session.invalidate();
	
	response.sendRedirect("LoginForm.jsp");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>