package model2.mvcboard;

import java.io.IOException;

import fileupload.FileUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.JSFunction;

/*
 controller for writing,
 	doGet() : page 진입
 	doPost() : 쓰기 처리
 */
public class WriteController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // request get : click 진입
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        // login 확인을 위해 request를 통해 session 내장객체를 얻어 옴.
        HttpSession session = req.getSession();
        
        // session 회원 속성이 없다면, login page 이동 
        if (session.getAttribute("UserId") == null) {
            JSFunction.alertLocation(resp, "로그인 후 이용해 주세요.", "../06Session/LoginForm.jsp");
            return;
        }
        
        // session 회원 속성이 있다면(=로그인이 완료된 상태), forward to write.jsp 
        req.getRequestDispatcher("/14MVCBoard/Write.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        // 로그인 확인 : session은 일정 시간이 지나면 자동으로 해제, 꼭 확인 필요.
        HttpSession session = req.getSession();

        if (session.getAttribute("UserId") == null) {
            JSFunction.alertLocation(resp, "로그인 후 이용해 주세요.", "../06Session/LoginForm.jsp");
            return;
        }
        
        // 1. 파일 업로드 처리 ====================================
        // 업로드 디렉터리의 물리적 경로 확인
        String saveDirectory = req.getServletContext().getRealPath("/Uploads");

        // 파일 업로드
        String originalFileName = "";
        try {
        	originalFileName = FileUtil.uploadFile(req, saveDirectory);
        } catch (Exception e) {
            JSFunction.alertLocation(resp, "파일 업로드 오류입니다.", "../mvcboard/write.do");
            return;
        }
        
        // 2. 파일 업로드 외 처리 =================================
        // 폼값을 DTO에 저장
        MVCBoardDTO dto = new MVCBoardDTO();
        // UserId : session에 저장된 데이터
        dto.setId(session.getAttribute("UserId").toString());
        // Title, Content : 사용자가 전달된 값
        dto.setTitle(req.getParameter("title"));
        dto.setContent(req.getParameter("content"));
        
        // 원본 파일명과 저장된 파일 이름 설정
        if (!originalFileName.equals("")) {
            // 파일명 변경
            String savedFileName = FileUtil.renameFile(saveDirectory, originalFileName);
            
            dto.setOfile(originalFileName);  // 원래 파일 이름
            dto.setSfile(savedFileName);     // 서버에 저장된 파일 이름
        }
        
     // DAO를 통해 DB에 게시 내용 저장 (insert query 실행)
        MVCBoardDAO dao = new MVCBoardDAO();
                
        int result = dao.insertWrite(dto);

        /*********************************************/
        // dummy data 100개 입력하기
        /*for(int i=1;i<=100;i++) {
        	dto.setTitle(req.getParameter("title")+"-"+i);
        	dao.insertWrite(dto);
        }*/
        /*********************************************/
        
        dao.close();

        // 성공 or 실패?
        if (result == 1) { // 글쓰기 성공
            // 게시판 목록으로 이동
        	resp.sendRedirect("../mvcboard/listPage.do");
        } else {           // 글쓰기 실패
            // 글쓰기 page로 이동
        	JSFunction.alertLocation(resp, "글쓰기에 실패했습니다.", "../mvcboard/write.do");
        }
    }
}
    
    