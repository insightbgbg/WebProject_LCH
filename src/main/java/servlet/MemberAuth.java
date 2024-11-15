package servlet;

import java.io.IOException;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import membership.MemberDAO;
import membership.MemberDTO;

public class MemberAuth extends HttpServlet{

	// member var 선언
	MemberDAO dao;
	
	/*
	 	init() : client가 최초 요청 시, 1번만 호출
	 			DB connection 담당
	 */
	@Override
	public void init() throws ServletException {

		/*
		 모델1 : 매개 변수로 내장 객체 전달 가능
		 모델2 : 매개 변수로 내장 객체 전달 불가
		 각 내장 객체를 얻어올 수 있는 method 존재
		 servlet 내에서 application 내장 객체를 얻어옴
		 */
		ServletContext application = this.getServletContext();
		
		String driver = application.getInitParameter("OracleDriver");
		String connectUrl = application.getInitParameter("OracleURL");
		String oId = application.getInitParameter("OracleId");
		String oPass = application.getInitParameter("OraclePwd");
		
		// Oracle connection
		dao = new MemberDAO(driver, connectUrl, oId, oPass);
		
	}

	/*
	 	service() : get/post method 모두 처리 가능
	 */
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		/*
		 	servlet mapping 시 <init-param>에 등록된 servlet initial parameter
		 	해당 servlet만 사용 가능
		 */
		String admin_id = this.getInitParameter("admin_id");
		
		// querystring으로 전달된 parameter
		String id=req.getParameter("id");
		String pass=req.getParameter("pass");
		
		/*
		 	회원 인증을 위해 DAO method call
		 	DB에서 name 가져옴.
		 */
		MemberDTO memberDTO=dao.getMemberDTO(id, pass);
		
		String memberName = memberDTO.getName();
		if (memberName!=null) {
			// DTO에 회원 이름이 있다면 인증 성공
			req.setAttribute("authMessage", memberName + "Welcome !");
		}
		else {
			// 인증 실패 시 관리자 여부 확인			
			if (admin_id.equals(id))
				req.setAttribute("authMessage", admin_id + "are supervisor.");
			// 관리자 아니면 비회원
			else
				req.setAttribute("authMessage", "You are not allowed.");
		}

		/*
		 	인증 정보 request 저장
		 	JSP forward
		 */
		req.getRequestDispatcher("/12Servlet/MemberAuth.jsp").forward(req, resp);
		
	}
	
	// servlet 종료, DAO 자원 해제
	@Override
	public void destroy() {

		dao.close();
		System.out.println("destroy() method call");
	}
	
}
