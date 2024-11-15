package model2.mvcboard;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.BoardPage;

// 목록 처리 (paging 기능 추가)
@WebServlet("/board.do")
public class ListPageController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
                         throws ServletException, IOException {

        // DAO 생성
        MVCBoardDAO dao = new MVCBoardDAO();

        // 뷰에 전달할 매개변수 저장용 맵 생성
        Map<String, Object> map = new HashMap<String, Object>();
        
        // 검색어가 있는 경우 map에 추가
        String searchField = req.getParameter("searchField");
        String searchWord = req.getParameter("searchWord");
        if (searchWord != null) {
            map.put("searchField", searchField);
            map.put("searchWord", searchWord);
        }
        // 검색이 적용된 posts 갯수
        int totalCount = dao.selectCount(map);

        /* 페이지 처리 start */
        // application 내장 객체
        ServletContext application = getServletContext();
        // web.xml의 paging 관련 context parameter -> int 값으로 가져옴
        int pageSize = Integer.parseInt(
            application.getInitParameter("POSTS_PER_PAGE"));
        int blockPage = Integer.parseInt(
            application.getInitParameter("PAGES_PER_BLOCK"));
        
        /*
          현재 page : default 1 (목록에 처음 진입했을 때 값이 없는 것을 고려) 
         */
        int pageNum = 1;
        String pageTemp = req.getParameter("pageNum");
        if (pageTemp != null && !pageTemp.equals(""))
            pageNum = Integer.parseInt(pageTemp);

        // 목록에 출력할 게시물 범위 계산
        int start = (pageNum - 1) * pageSize + 1;
        int end = pageNum * pageSize;
        // DAO로 전달하기 위해 map 저장
        map.put("start", start);
        map.put("end", end);
        /* 페이지 처리 end */

        // 목록에 출력한 posts
        List<MVCBoardDTO> boardLists = dao.selectListPage(map);
        dao.close();

        // 뷰에 전달할 매개변수 추가
        // 목록 하단 page 바로가기 link -> map 추가
        String pagingImg = BoardPage.pagingStr(totalCount,
            pageSize, blockPage, pageNum,
            "../mvcboard/listPage.do");
        map.put("pagingImg", pagingImg);
        map.put("totalCount", totalCount);
        map.put("pageSize", pageSize);
        map.put("pageNum", pageNum);

        // 전달할 데이터를 request 영역에 저장 후 ListPage.jsp로 포워드
        req.setAttribute("boardLists", boardLists);
        req.setAttribute("map", map);
        req.getRequestDispatcher("/index.jsp")
            .forward(req, resp);
    }
}
        
        
        