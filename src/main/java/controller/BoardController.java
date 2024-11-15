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

@WebServlet("/board")
public class BoardController extends HttpServlet {
    private WebProjectDAO dao = new WebProjectDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "add":
                // 게시물 추가
                WebProjectDTO.Board board = new WebProjectDTO.Board();
                board.setMemberId(request.getParameter("memberId"));
                board.setBoardType(request.getParameter("boardType"));
                board.setTitle(request.getParameter("title"));
                board.setContent(request.getParameter("content"));
                
                dao.addBoard(board);
                response.sendRedirect("board?action=list");
                break;

            case "update":
                // 게시물 수정
                int boardId = Integer.parseInt(request.getParameter("boardId"));
                WebProjectDTO.Board updatedBoard = dao.getBoardById(boardId);
                updatedBoard.setTitle(request.getParameter("title"));
                updatedBoard.setContent(request.getParameter("content"));
                
                dao.updateBoard(updatedBoard);
                response.sendRedirect("board?action=view&boardId=" + boardId);
                break;

            case "delete":
                // 게시물 삭제
                int deleteBoardId = Integer.parseInt(request.getParameter("boardId"));
                dao.deleteBoard(deleteBoardId);
                response.sendRedirect("board?action=list");
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "list":
                // 게시물 목록 조회
                List<WebProjectDTO.Board> boards = dao.getAllBoards();
                request.setAttribute("boards", boards);
                request.getRequestDispatcher("board_list.jsp").forward(request, response);
                break;

            case "view":
                // 게시물 상세 조회
                int boardId = Integer.parseInt(request.getParameter("boardId"));
                WebProjectDTO.Board board = dao.getBoardById(boardId);
                
                // 조회수 증가
                dao.incrementViewCount(boardId);
                board.setViewCount(board.getViewCount() + 1);

                request.setAttribute("board", board);
                request.getRequestDispatcher("board_detail.jsp").forward(request, response);
                break;

            case "edit":
                // 게시물 작성 페이지 또는 수정 페이지
                String editType = request.getParameter("type");
                if ("new".equals(editType)) {
                    // 새 게시물 작성
                    request.getRequestDispatcher("board_edit.jsp").forward(request, response);
                } else if ("edit".equals(editType)) {
                    // 기존 게시물 수정
                    int editBoardId = Integer.parseInt(request.getParameter("boardId"));
                    WebProjectDTO.Board editBoard = dao.getBoardById(editBoardId);
                    request.setAttribute("board", editBoard);
                    request.getRequestDispatcher("board_edit.jsp").forward(request, response);
                }
                break;
        }
    }
}
