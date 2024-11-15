<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<h2>Cookie set</h2>

	<%
/*  
쿠키는 생성만로만 생성
setter 생성 불가
*/
	
		Cookie cookie = new Cookie("myCookie","쿠키맛나요");
		cookie.setPath(request.getContextPath());
		cookie.setMaxAge(3600);
		response.addCookie(cookie);
/*
클라이언트에 쿠키 생성
*/
		
		%>

	<h2>Cookie value</h2>

	<%
		Cookie[] cookies=request.getCookies();
	
		if (cookies!=null) {
	
			for (Cookie c : cookies) {
				String cookieName = c.getName();
				String cookieValue = c.getValue();
				out.println(String.format("%s : %s<br/>", cookieName, cookieValue));
			}
		}
/*  
쿠키 생성 직후는 값 확인 불가
페이지 이동 또는 새로고침 후 확인 가능
*/
		
		%>

	<h2>3. page move</h2>
	<!-- 
	유효 시간 내 어플 어디서든 확인 가능
	페이지 이동 후에도 유지
	 -->
	
	<a href ="CookieResult.jsp">
		next page, cookie value
	</a>
</body>
</html>