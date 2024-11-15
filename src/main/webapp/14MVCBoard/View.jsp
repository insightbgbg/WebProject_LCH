<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

    <script>
        function confirmDeletion(idx) {
            if (confirm('정말로 이 게시물을 삭제할까요 ?')) {
                // 확인을 누르면 삭제 요청을 보냄
                location.href = '../mvcboard/delete.do?idx=' + idx;
            }
        }
    </script>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>파일 첨부형 게시판 - 상세 보기</title>
</head>
<body>
    <h2>파일 첨부형 게시판 - 상세 보기(View)</h2>
    <table border="1" width="90%">
        <colgroup>
            <col width="15%" /> <col width="35%" />
            <col width="15%" /> <col width="*" />
        </colgroup>
		<!-- 
			ViewController dto 전달
		 -->
        <tr>
            <td>번호</td> <td>${ dto.idx }</td>
            <td>작성자</td> <td>${ dto.name }</td>
        </tr>
        <tr>
            <td>작성일</td> <td>${ dto.postdate }</td>
            <td>조회수</td> <td>${ dto.visitcount }</td>
        </tr>
        <tr>
            <td>제목</td>
            <td colspan="3">${ dto.title }</td>
        </tr>
        <tr>
            <td>내용</td>
            <td colspan="3" height="100">
                ${ dto.content }
                
<!-- 첨부 파일이 있다면 -->
			<c:if test="${not empty dto.ofile}">
			    <c:set var="extension" value="${dto.sfile.substring(dto.sfile.lastIndexOf('.') + 1).toLowerCase()}" />
			
			    <c:choose>
			        <%-- 이미지 파일 처리 (png, gif, jpg) --%>
			        <c:when test="${extension == 'png' || extension == 'gif' || extension == 'jpg'}">
			            <br><img src="../Uploads/${dto.sfile}" style="max-width:100%;" />
			        </c:when>
			
			        <%-- 오디오 파일 처리 (mp3, wav) --%>
			        <c:when test="${extension == 'mp3' || extension == 'wav'}">
			            <br><audio controls>
			            	<source src="../Uploads/${dto.sfile}" type="audio/${extension == 'mp3' ? 'mpeg' : 'wav'}">
			            	Your browser does not support the audio element.
			            </audio>
			        </c:when>
			
			        <%-- 비디오 파일 처리 (mp4, avi, wmv) --%>
			        <c:when test="${extension == 'mp4' || extension == 'avi' || extension == 'wmv'}">
			            <br><video controls style="max-width:100%;">
			                <source src="../Uploads/${dto.sfile}" type="video/${extension}">
			            	Your browser does not support the video element.
			            </video>
			        </c:when>
			
			        <%-- 그 외 파일 처리 (다운로드 링크 출력) --%>
			        <c:otherwise>
			            <a href="../mvcboard/download.do?ofile=${dto.ofile}&sfile=${dto.sfile}&idx=${dto.idx}">[다운로드]</a>
			        </c:otherwise>
			    </c:choose>
			</c:if>
            </td>
        </tr>
        <!-- 첨부파일 -->
        <tr>
            <td>첨부파일</td>
            <td>
                <!-- 첨부 파일이 있다면 -->
                <c:if test="${ not empty dto.ofile }">
                    ${ dto.ofile }
					<!-- 
						다운로드 link 출력
						query string 부분은 1줄로 기록 필수
					 -->
					<a href="../mvcboard/download.do?ofile=${ dto.ofile }&sfile=${ dto.sfile }&idx=${ dto.idx }">
					    [다운로드]
					</a>
				</c:if>
			</td>
			<td>다운로드수</td>
			<td>${ dto.downcount }</td>
		</tr>

<!-- 하단 메뉴 (버튼) -->
		<tr>
		    <td colspan="4" align="center">

				<c:if test="${ dto.id == sessionScope.UserId }"> 

			        <button type="button" 
			            onclick="location.href='../mvcboard/edit.do?idx=${ param.idx }';">수정하기</button>
					<button onclick="confirmDeletion(${param.idx});">삭제하기</button>
	<%-- 		    <button type="button" 
			            onclick="location.href='../mvcboard/delete.do?idx=${ param.idx }';">삭제하기</button>
			            onclick="if(confirm('정말로 이 게시물을 삭제할까요 ?')) { location.href='../mvcboard/delete.do?idx=${param.idx}'; }">
			                삭제하기
					</button> --%>

				</c:if> 

		        <button type="button" 
		            onclick="location.href='../mvcboard/listPage.do';">목록 바로가기
		        </button>
		    </td>
		</tr>
	</table>
</body>
</html>

                    
                    
                    