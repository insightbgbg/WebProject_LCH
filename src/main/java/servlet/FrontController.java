package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("*.one")
/*
 	annotation mapping
 	.one으로 끝나는 모든 URL 요청을 FrontController 서블릿이 처리하게 함.
 */
public class FrontController extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		/*  
			getRequestURI : JSP에서 요청된 URL 중에서 서버 부분을 제외한 부분
		 	uri : "/JSPServlet/12Servlet/regist.one"
		 	lastSlaah : 끝 "/" 위치
		  	substring : lastSlash 이후 문자
		 */
		String uri = req.getRequestURI();
		int lastSlash = uri.lastIndexOf("/");
		String commandStr = uri.substring(lastSlash);
		
		if (commandStr.equals("/regist.one"))
			registFunc(req);
		else if (commandStr.equals("/login.one"))
			loginFunc(req);
		else if (commandStr.equals("/freeboard.one"))
			freeboardFunc(req);
		
		req.setAttribute("uri", uri);
		req.setAttribute("commandStr", commandStr);
		req.getRequestDispatcher("/12Servlet/FrontController.jsp")
				.forward(req, resp);
		
	}

	// page별 처리 method 정의
	void registFunc(HttpServletRequest req) {
		req.setAttribute("resultValue", "<h4>register</h4>");
	}
	void loginFunc(HttpServletRequest req) {
		req.setAttribute("resultValue", "<h4>login</h4>");
	}
	void freeboardFunc(HttpServletRequest req) {
		req.setAttribute("resultValue", "<h4>freeboard</h4>");
	}
	
}
