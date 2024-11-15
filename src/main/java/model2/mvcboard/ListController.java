package model2.mvcboard;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// mapping은 web.xml에서 정의 (/mvcboard/listPage.do -> model2.mvcboard.ListController)
public class ListController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 게시판 목록은 get 방식 (특정 메뉴를 클릭 후 진입)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        // DAO instance 생성, 생성과 동시에 oracle 연결
        MVCBoardDAO dao = new MVCBoardDAO();

        // 뷰에 전달할 매개변수 저장용 맵 생성
        Map<String, Object> map = new HashMap<String, Object>();

        // 검색어 parameter -> map 저장
        String searchField = req.getParameter("searchField");
        String searchWord = req.getParameter("searchWord");
        // 쿼리스트링으로 전달받은 매개변수 중 검색어가 있다면 map에 저장
        if (searchWord != null) {
            map.put("searchField", searchField);
            map.put("searchWord", searchWord);
        }

        // 게시물의 갯수 count
        int totalCount = dao.selectCount(map);
        // map에 totalCount 저장
        map.put("totalCount", totalCount);
        
        // 게시물 리스트 가져오기
        List<MVCBoardDTO> boardLists = dao.selectList(map);
        dao.close();

        // 전달한 데이터를 request 영역에 저장 후 List.jsp로 포워드
        req.setAttribute("boardLists", boardLists);
        req.setAttribute("map", map);
        req.getRequestDispatcher("/14MVCBoard/List.jsp").forward(req, resp);
    }
}
