<%@ page import="membership.MemberDTO"%>
<%@ page import="membership.MemberDAO"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
// form submit 후 form value 가져 오기
	String userId=request.getParameter("user_id");
	String userPwd=request.getParameter("user_pw");
	
	// application 내장 객체 통해, web.xml에 저장된 접속 정보 가져 오기
	String oracleDriver=application.getInitParameter("OracleDriver");
	String oracleURL=application.getInitParameter("OracleURL");
	String oracleId=application.getInitParameter("OracleId");
	String oraclePwd=application.getInitParameter("OraclePwd");
	
	// 위 4개의 정보를 인수로 DAO instance 생성
	MemberDAO dao= new MemberDAO(oracleDriver,oracleURL,oracleId,oraclePwd);
	
	// 입력받은 id, pw를 통해, 메서드 호출 (=query)
	MemberDTO memberDTO = dao.getMemberDTO(userId,userPwd);
	
	// DB 연결 종료
	dao.close();

	// DTO 객체에 id가 있다면 login success
	if (memberDTO.getId()!=null) {
		// session scope id, name 저장
		session.setAttribute("UserId", memberDTO.getId());
		session.setAttribute("UserName", memberDTO.getName());
		// session 정보는 브라우져를 닫을 때까지 유효
		response.sendRedirect("LoginForm.jsp");
	}
	else {
		
		/* login fail -> request scope err msg 저장
		   login page forward
		   request scope forward page data 공유 */
		
		request.setAttribute("LoginErrMsg", "login error !");
		request.getRequestDispatcher("LoginForm.jsp").forward(request, response);
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