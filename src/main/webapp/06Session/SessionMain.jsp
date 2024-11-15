<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	// 시간 서식
	SimpleDateFormat dataFormat = new SimpleDateFormat("HH:mm:ss");

	// session created time
	long creationTime = session.getCreationTime();
	String creationTimeStr = dataFormat.format(new Date(creationTime));

	// session last accessed time
	long lastTime = session.getLastAccessedTime();
	String lastTimeStr = dataFormat.format(new Date(lastTime));
	// 시간을 미리 정의한 문자열로 변환

	// session interval 조절
	//session.setMaxInactiveInterval(60*20);

%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>Session set</h2>
	<ul>
		<li> Session interval : <%= session.getMaxInactiveInterval() %></li>
		<li> Session id : <%= session.getId() %></li>
		<li> Created time : <%= creationTimeStr %></li>
		<li> Last accessed time : <%= lastTimeStr %></li>
	</ul>
	
</body>
</html>