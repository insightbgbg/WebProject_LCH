<%@ page import="utils.CookieManager" %>
<%@ page import="utils.JSFunction" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String user_id = request.getParameter("user_id");
	String user_pw = request.getParameter("user_pw");
/*  */
	
	String save_check = request.getParameter("save_check");
	
	if ("must".equals(user_id) && "1234".equals(user_pw)){
		
		if (save_check!=null && save_check.equals("Y")) {
// 로그인 성공 후, 체크 시 쿠키 생성 86400 -> 1일 
			CookieManager.makeCookie(response, "loginId", user_id, 86400);
		}
		else{
// 로그인 성공 후, 언체크 시 쿠키 삭제 

			CookieManager.deleteCookie(response, "loginId");
		}
// 메시지, 페이지 이동
		JSFunction.alertLocation("login success", "IdSaveMain.jsp", out);
		
	}	

// 로그인 실패	-> 뒤로 이동
	else {
		
		//JSFunction.alertBack("login fail", out);
%>
<!-- 유틸리티 클래스가 없다면 
스크립트렛으로 HTML 영역을 만든 뒤, 스크립트 태그 추가해야 함.  -->

	<script>
		alert("login fail");
		history.go(-1);
	</script>

<%

	
	}
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