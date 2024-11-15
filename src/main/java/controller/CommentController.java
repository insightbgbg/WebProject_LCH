package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.WebProjectDAO;
import model.WebProjectDTO;

@WebServlet("/comment")
public class CommentController extends HttpServlet {
    private WebProjectDAO dao = new WebProjectDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            // 댓글 추가
            WebProjectDTO.Comment comment = new WebProjectDTO.Comment();
            comment.setCommentId(Integer.parseInt(request.getParameter("commentId")));
            comment.setBoardId(Integer.parseInt(request.getParameter("boardId")));
            comment.setMemberId(request.getParameter("memberId"));
            comment.setContent(request.getParameter("content"));

            dao.addComment(comment);
            response.sendRedirect("comment_list.jsp?boardId=" + comment.getBoardId());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String boardId = request.getParameter("boardId");

        if (boardId != null) {
            // 특정 게시물에 대한 댓글 목록 조회
            List<WebProjectDTO.Comment> comments = dao.getCommentsByBoardId(Integer.parseInt(boardId));
            request.setAttribute("comments", comments);
            request.getRequestDispatcher("comment_list.jsp").forward(request, response);
        }
    }
}
