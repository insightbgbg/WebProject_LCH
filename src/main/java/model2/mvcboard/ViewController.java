package model2.mvcboard;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// annotation matpping
@WebServlet("/mvcboard/view.do")
public class ViewController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /*
     	service : get/post 판단
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 게시물 불러오기
        MVCBoardDAO dao = new MVCBoardDAO();
        // idx parameter 
        String idx = req.getParameter("idx");

        dao.updateVisitCount(idx);          // 조회수 1 증가

        // SELECT
        MVCBoardDTO dto = dao.selectView(idx);
        dao.close();

        // 줄바꿈 처리 : <br> 태그로 변경
        dto.setContent(dto.getContent()
            .replaceAll("\r\n", "<br/>"));

        // 게시물(dto) 저장 후 뷰로 포워드
        req.setAttribute("dto", dto);
        req.getRequestDispatcher("/14MVCBoard/View.jsp")
            .forward(req, resp);
    }
}
