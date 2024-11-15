package utils;

public class BoardPage {

	/*
	 totalCount : 전체 post 갯수
	 pageSize : web.xml parameter, posts 수 / 1page
	 blockPage : web.xml parameter, pages 수 / 1block
	 pageNum : 현재 page num
	 reqUrl : 게시판 목록의 요청명 또는 현재 page URL
	 */
	
	public static String pagingStr(int totalCount, int pageSize, int blockPage,
                                   int pageNum, String reqUrl) {
        String pagingStr = "";

        /*
         totalPages : 전체 page 수 계산 (ceil : 무조건 올림)
         	(double) : 계산 값을 double로 함으로써 소수점 이하 값 살리기 (추가하지 않으면 int로 표현되고, 소수점이 이하 값 삭제됨)
         */
        int totalPages = (int) (Math.ceil(((double) totalCount / pageSize)));

        // '이전 page block 바로가기' 출력
        /*
          pageTemp : page block에서 첫번째 page 번호 
          				blockPage는 web.xml에서 5로 설정, 1,6,11,16,21 ....
         */
        int pageTemp = (((pageNum - 1) / blockPage) * blockPage) + 1;
        
        // 첫번째 block이 아닐 때만 '이전 page 바로가기' 링크 출력
        if (pageTemp != 1) {
            pagingStr += "<a href='" + reqUrl + "?pageNum=1'>[첫 페이지]</a>";
            pagingStr += "&nbsp;";
            pagingStr += "<a href='" + reqUrl + "?pageNum=" + (pageTemp - 1)
                         + "'>[이전 블록]</a>";
        }

        int blockCount = 1;
        while (blockCount <= blockPage && pageTemp <= totalPages) {
            // 현재 page는 링크 걸지 않음
        	if (pageTemp == pageNum) {
                pagingStr += "&nbsp;" + pageTemp + "&nbsp;";
            // 현재 page가 아니면 링크 부착
        	} else {
                pagingStr += "&nbsp;<a href='" + reqUrl + "?pageNum=" + pageTemp
                             + "'>" + pageTemp + "</a>&nbsp;";
            }
            pageTemp++;
            blockCount++;
        }

        if (pageTemp <= totalPages) {
            pagingStr += "<a href='" + reqUrl + "?pageNum=" + pageTemp
                         + "'>[다음 블록]</a>";
            pagingStr += "&nbsp;";
            pagingStr += "<a href='" + reqUrl + "?pageNum=" + totalPages
                         + "'>[마지막 페이지]</a>";
        }

        return pagingStr;
    }
}
