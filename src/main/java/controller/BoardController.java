package controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.WebProjectDAO;
import model.WebProjectDTO;
import utils.BoardPage;

@WebServlet("/board")
public class BoardController extends HttpServlet {
    private WebProjectDAO dao = new WebProjectDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	String action = request.getParameter("action");
        HttpSession session = request.getSession(false); // 기존 세션만 가져옴
        WebProjectDTO.Member loggedInUser = (session != null) ? (WebProjectDTO.Member) session.getAttribute("loggedInUser") : null;

        if (loggedInUser == null) { // 로그인 확인
            response.sendRedirect("login.jsp");
            return;
        } 

        switch (action) {
            case "add": // 글 작성
                handleAddPost(request, response, loggedInUser);
                break;

            case "edit": // 글 수정
                handleEditPost(request, response, loggedInUser);
                break;

            case "delete": // 글 삭제
                handleDeletePost(request, response, loggedInUser);
                break;

            case "like": // 좋아요
                handleLikePost(request, response);
                break;

            default:
                response.sendRedirect("error.jsp"); // 알 수 없는 action
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	String action = request.getParameter("action");

    	HttpSession session = request.getSession(false); // 기존 세션만 가져옴
        WebProjectDTO.Member loggedInUser = (session != null) ? (WebProjectDTO.Member) session.getAttribute("loggedInUser") : null;
        
        switch (action) {
            case "add": // 글 작성 페이지

                if (loggedInUser == null) { // 로그인 확인
                    response.sendRedirect("login.jsp");
                    return;
                }
            	
            	
            	/* String boardType = request.getParameter("boardType"); 
            	String boardType = URLDecoder.decode(request.getParameter("boardType"), "UTF-8");
            	
            	// 디버깅
            	System.out.println("================== boardType ======= : " + boardType);
            	System.out.println("================== loggedInUser ======= : " + loggedInUser);
            	
                // 글쓰기 권한 제한 (Q&A, File은 회원만 가능)
                if ((boardType.equalsIgnoreCase("q") || boardType.equalsIgnoreCase("file")) && loggedInUser == null) {
                    // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
                    request.setAttribute("errorMessage", "You must be logged in to post in this board.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    return;
                }  */  	
            	
            	request.getRequestDispatcher("board_add.jsp").forward(request, response);
                break;

            case "edit": // 글 수정 페이지

            	handleGetEdit(request, response);
                break;

            case "list": // 게시글 목록

            	// 디버깅
            	// System.out.println("================== 컨트롤러 GET list 진입 ====================");
            	
            	handleList(request, response);
                break;

            case "view": // 게시글 상세 보기
                handleView(request, response);
                break;

            default:
                response.sendRedirect("error.jsp"); // 알 수 없는 action
        }
    }

    // 글 작성 처리
    private void handleAddPost(HttpServletRequest request, HttpServletResponse response, WebProjectDTO.Member loggedInUser) throws ServletException, IOException {

    	String boardType = request.getParameter("boardType");

    	String title = request.getParameter("title");
        String content = request.getParameter("content");

        // 모든 항목이 기록되었는데 확인
        if (boardType == null || boardType.isEmpty() || title == null || content == null) {
            request.setAttribute("errorMessage", "All fields are required.");
            request.getRequestDispatcher("board_add.jsp").forward(request, response);
            return;
        }

        WebProjectDTO.Board board = new WebProjectDTO.Board();
        board.setMemberId(loggedInUser.getMemberId());
        board.setBoardType(boardType);
        board.setTitle(title);
        board.setContent(content);

        dao.addBoard(board);
        
    	// 디버깅
    	//System.out.println("================== boardType ====================" + boardType);
    	//System.out.println("================== title ====================" + title);
        
        //response.sendRedirect("board?action=list");
        response.sendRedirect("board?action=list&boardType=" + boardType);
    }

    // 글 수정 처리
    private void handleEditPost(HttpServletRequest request, HttpServletResponse response, WebProjectDTO.Member loggedInUser) throws ServletException, IOException {
        int boardId = Integer.parseInt(request.getParameter("boardId"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");

 // 디버깅 (add 진입 여부)
    	//System.out.println("============ boardId,title,content =========== : " + boardId + ", " + title + ", " + content);	        	
        
        
        WebProjectDTO.Board boardToEdit = dao.getBoardById(boardId);
        if (boardToEdit == null || !boardToEdit.getMemberId().equals(loggedInUser.getMemberId())) {
            request.setAttribute("errorMessage", "You are not authorized to edit this post.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        boardToEdit.setTitle(title);
        boardToEdit.setContent(content);
        dao.updateBoard(boardToEdit);

        response.sendRedirect("board?action=view&boardId=" + boardId);
    }

    // 글 삭제 처리
    private void handleDeletePost(HttpServletRequest request, HttpServletResponse response, WebProjectDTO.Member loggedInUser) throws IOException {
        int boardId = Integer.parseInt(request.getParameter("boardId"));
        WebProjectDTO.Board boardToDelete = dao.getBoardById(boardId);

        if (boardToDelete != null && boardToDelete.getMemberId().equals(loggedInUser.getMemberId())) {
            dao.deleteBoard(boardId);
            response.sendRedirect("board?action=list");
        } else {
            response.sendRedirect("error.jsp");
        }
    }

    // 좋아요 처리
    private void handleLikePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int boardId = Integer.parseInt(request.getParameter("boardId"));
        dao.incrementLikeCount(boardId);
        response.sendRedirect("board?action=view&boardId=" + boardId);
    }

    // 글 수정 페이지
    private void handleGetEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int boardId = Integer.parseInt(request.getParameter("boardId"));
        WebProjectDTO.Board board = dao.getBoardById(boardId);

        if (board != null) {
            request.setAttribute("board", board);
            request.getRequestDispatcher("board_edit.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Board not found.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    // 게시글 목록
/*    private void handleList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext application = getServletContext();
        int pageSize = Integer.parseInt(application.getInitParameter("POSTS_PER_PAGE"));
        int blockPage = Integer.parseInt(application.getInitParameter("PAGES_PER_BLOCK"));

        int pageNum = 1;
        String pageTemp = request.getParameter("pageNum");
        if (pageTemp != null && !pageTemp.equals("")) {
            pageNum = Integer.parseInt(pageTemp);
        }

        Map<String, Object> map = new HashMap<>();
        int totalCount = dao.selectBoardCount(map);

        int start = (pageNum - 1) * pageSize + 1;
        int end = pageNum * pageSize;
        map.put("start", start);
        map.put("end", end);

        List<WebProjectDTO.Board> boards = dao.getBoardListWithPaging(map);
        String pagingImg = BoardPage.pagingStr(totalCount, pageSize, blockPage, pageNum, "board?action=list");

        request.setAttribute("boards", boards);
        request.setAttribute("pagingImg", pagingImg);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("pageNum", pageNum);

        request.getRequestDispatcher("board_list.jsp").forward(request, response);
    } */
    
    private void handleList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 게시판 유형 가져오기
    	String boardType = request.getParameter("boardType"); 
	
	   /* String boardTypeParam = request.getParameter("boardType");
	   
	    // DB에 저장된 형식으로 변환
	    String boardType = null;
	    if ("free".equalsIgnoreCase(boardTypeParam)) {
	        boardType = "Free";
	    } else if ("qa".equalsIgnoreCase(boardTypeParam)) {
	        boardType = "Q&A";
	    } else if ("file".equalsIgnoreCase(boardTypeParam)) {
	        boardType = "File";
	    }

    	// 디버깅
    	System.out.println("==============boardTypeParam, boardType ================ : " +boardTypeParam +", "+ boardType );
    
	    */
    	
        if (boardType == null || boardType.isEmpty()) {
            boardType = "free"; // 기본값 설정
        }

        // 페이징 설정
        ServletContext application = getServletContext();
        int pageSize = Integer.parseInt(application.getInitParameter("POSTS_PER_PAGE"));
        int blockPage = Integer.parseInt(application.getInitParameter("PAGES_PER_BLOCK"));

        int pageNum = 1;
        String pageTemp = request.getParameter("pageNum");
        if (pageTemp != null && !pageTemp.isEmpty()) {
            pageNum = Integer.parseInt(pageTemp);
        }

        Map<String, Object> map = new HashMap<>();
        
        map.put("boardType", boardType);

     // 검색 조건이 있을 경우 map에 추가

        String searchField = request.getParameter("searchField"); // 검색 필드
        String searchWord = request.getParameter("searchWord");   // 검색어        
        
        if (searchField != null && !searchField.isEmpty() && searchWord != null && !searchWord.isEmpty()) {
            map.put("searchField", searchField);
            map.put("searchWord", searchWord);
        }        
        
        int totalCount = dao.selectBoardCount(map);
    	// 디버깅
    	//System.out.println("==============totalCount ================ : " +totalCount);
       
        int start = (pageNum - 1) * pageSize + 1;
        int end = pageNum * pageSize;
        map.put("start", start);
        map.put("end", end);

    	// 디버깅
    	// System.out.println("===== pageSize,blockPage,pageNum,pageTemp,start,end == : " +pageSize+","+blockPage+","+pageNum+","+pageTemp+","+start+","+end);
        
        
        // 데이터 조회
        List<WebProjectDTO.Board> boards = dao.getBoardListWithPaging(map);
        
        // 디버깅 (첫번째 데이터의 제목)
        // System.out.println("첫 번째 게시물의 제목: " + boards.get(0).getTitle());
        
        
        String pagingImg = BoardPage.pagingStr(totalCount, pageSize, blockPage, pageNum, "board?action=list&boardType=" + boardType);

        // 결과를 request에 저장
        request.setAttribute("boards", boards);

        request.setAttribute("pagingImg", pagingImg);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("pageNum", pageNum);
        request.setAttribute("boardType", boardType);

        // board_list.jsp로 포워드
        request.getRequestDispatcher("board_list.jsp").forward(request, response);
    }
    

    /*
    // 게시글 상세 보기
    private void handleView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	int boardId = Integer.parseInt(request.getParameter("boardId"));

    	WebProjectDTO.Board board = dao.getBoardById(boardId);

        board.setContent(board.getContent().replaceAll("\r\n", "<br/>"));
        
        if (board != null) {
            dao.incrementViewCount(boardId);
            request.setAttribute("board", board);
            
            request.getRequestDispatcher("board_view.jsp").forward(request, response);
        } else {
            response.sendRedirect("error.jsp");
        }
    }
	*/

    private void handleView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int boardId = Integer.parseInt(request.getParameter("boardId"));

        WebProjectDTO.Board board = dao.getBoardById(boardId);
        List<WebProjectDTO.Comment> comments = dao.getCommentsByBoardId(boardId); // 댓글 목록 가져오기

        if (board != null) {
            dao.incrementViewCount(boardId);
            board.setContent(board.getContent().replaceAll("\r\n", "<br/>"));
            request.setAttribute("board", board);
            request.setAttribute("comments", comments); // 댓글 목록 추가
            request.getRequestDispatcher("board_view.jsp").forward(request, response);
        } else {
            response.sendRedirect("error.jsp");
        }
    }
    
    
}
