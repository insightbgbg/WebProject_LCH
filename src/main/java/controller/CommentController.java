package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.WebProjectDAO;
import model.WebProjectDTO;

@WebServlet("/comment")
public class CommentController extends HttpServlet {
    private WebProjectDAO dao = new WebProjectDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false); // 기존 세션만 가져옴
        WebProjectDTO.Member loggedInUser = (session != null) ? (WebProjectDTO.Member) session.getAttribute("loggedInUser") : null;

        // 로그인 상태 확인
        if (loggedInUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        switch (action) {
            case "add":
                handleAddComment(request, response, loggedInUser);
                break;

            case "edit":
                handleEditComment(request, response, loggedInUser);
                break;

            case "delete":
                handleDeleteComment(request, response, loggedInUser);
                break;

            default:
                response.sendRedirect("error.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "list":
                handleListComments(request, response);
                break;

            default:
                response.sendRedirect("error.jsp");
        }
    }

    // 댓글 추가
    private void handleAddComment(HttpServletRequest request, HttpServletResponse response, WebProjectDTO.Member loggedInUser) throws IOException, ServletException {
        int boardId = Integer.parseInt(request.getParameter("boardId"));
        String content = request.getParameter("content");

        if (content == null || content.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Comment content cannot be empty.");
            request.getRequestDispatcher("board?action=view&boardId=" + boardId).forward(request, response);
            return;
        }

        WebProjectDTO.Comment comment = new WebProjectDTO.Comment();
        comment.setBoardId(boardId);
        comment.setMemberId(loggedInUser.getMemberId());
        comment.setContent(content);

        dao.addComment(comment);
        response.sendRedirect("board?action=view&boardId=" + boardId);
    }

    // 댓글 수정
    private void handleEditComment(HttpServletRequest request, HttpServletResponse response, WebProjectDTO.Member loggedInUser) throws IOException, ServletException {
        int commentId = Integer.parseInt(request.getParameter("commentId"));
        String content = request.getParameter("content");

        WebProjectDTO.Comment comment = dao.getCommentById(commentId);
        if (comment != null && comment.getMemberId().equals(loggedInUser.getMemberId())) {
            comment.setContent(content);
            dao.updateComment(comment);
        } else {
            request.setAttribute("errorMessage", "You are not authorized to edit this comment.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("board?action=view&boardId=" + comment.getBoardId());
    }

    // 댓글 삭제
    private void handleDeleteComment(HttpServletRequest request, HttpServletResponse response, WebProjectDTO.Member loggedInUser) throws IOException {
        int commentId = Integer.parseInt(request.getParameter("commentId"));

        WebProjectDTO.Comment comment = dao.getCommentById(commentId);
        if (comment != null && comment.getMemberId().equals(loggedInUser.getMemberId())) {
            dao.deleteComment(commentId);
        }

        response.sendRedirect("board?action=view&boardId=" + comment.getBoardId());
    }

    // 댓글 리스트
    private void handleListComments(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int boardId = Integer.parseInt(request.getParameter("boardId"));

        List<WebProjectDTO.Comment> comments = dao.getCommentsByBoardId(boardId);
        request.setAttribute("comments", comments);
        request.getRequestDispatcher("comments_list.jsp").forward(request, response);
    }
}
