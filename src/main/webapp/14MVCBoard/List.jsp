<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- JSTL을 사용하기 위한 taglib 지시어 -->
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>파일 첨부형 게시판</title>
<style>a{text-decoration:none;}</style>
</head>
<body>
<h2>파일 첨부형 게시판 - 목록 보기(List)</h2>

<!-- 검색 폼 -->
<!-- 검색어 입력 후 '검색' 버튼 누르면 -> current page로 form value 전송
	action attribute가 없으면 변화 없음. -->
<form method="get">
    <table border="1" width="90%">
        <tr>
            <td align="center">
                <select name="searchField">
                    <option value="title">제목</option>
                    <option value="content">내용</option>
                </select>
                <input type="text" name="searchWord" />
                <input type="submit" value="검색하기" />
            </td>
        </tr>
    </table>
</form>
<!-- 목록 테이블 -->
<table border="1" width="90%">
    <tr>
        <th width="10%">번호</th>
        <th width="*">제목</th>
        <th width="15%">작성자</th>
        <th width="10%">조회수</th>
        <th width="15%">작성일</th>
        <th width="8%">첨부</th>
    </tr>

    <c:choose>
        <c:when test="${ empty boardLists }">
        <!-- List에 저장된 값이 없다면 
        	requestScope 생략됨.-->
            <tr>
                <td colspan="6" align="center">
                    등록된 게시물이 없습니다^_^
                </td>
            </tr>
        </c:when>
		<c:otherwise>
		    <!-- List에 저장된 값이 있다면 
		    	해당 loop data -> var에 저장 -->
		    <c:forEach items="${ boardLists }" var="row" varStatus="loop">
		        <tr align="center">
		            <!-- loop.index 0부터 증감, 역순으로 숫자 부여 -->
		            <td>${ map.totalCount - loop.index }</td>
		            <td align="left">
		                <!-- 제목 click 시 열람 page 이동, 
		                	게시물의 일련번호를 parameter로 전달 -->
		                <a href="../mvcboard/view.do?idx=${ row.idx }">
		                    ${ row.title }
		                </a>
		            </td>
		            <!-- row = MVCBoardDTO, getter 값 출력 -->
		            <td>${ row.id }</td>
		            <td>${ row.visitcount }</td>
		            <td>${ row.postdate }</td>
		            <td>
		                <!-- 첨부파일이 있을 때만 출력 -->
		                <c:if test="${ not empty row.ofile }">
		                    <a href="../mvcboard/download.do?ofile=${ row.ofile }&sfile=${ row.sfile }&idx=${ row.idx }">[Down]</a>
		                </c:if>
		            </td>
		        </tr>
		    </c:forEach>
		</c:otherwise>
	</c:choose>
</table>

<!-- 하단 메뉴 (바로가기, 글쓰기) -->
<table border="1" width="90%">
    <tr align="center">
        <td></td>
        <td width="100">
            <button type="button" onclick="location.href='../mvcboard/write.do';">
                글쓰기
            </button>
        </td>
    </tr>
</table>
</body>
</html>
        
        

