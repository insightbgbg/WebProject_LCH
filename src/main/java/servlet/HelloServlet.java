package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/*
 	survlet class process
 	1. HttpServlet 상속
 	2. doGet() / doPost() method overriding
 	3. 필수 package import, 예외 처리는 자동 완성
 	4. request, response 내장 객체 이미 포함 
 */

public class HelloServlet extends HttpServlet {

	/*
	  warning 차단 위해 추가.
	  추가하지 않아도 실행에는 영향 없음.
	 */
	private static final long serialVersionUID = 1L;

	/*
 		get 요청을 처리하기 위해 doGet() overriding
 		method가 없다면 405 error
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
					throws ServletException, IOException {

		// request 속성 저장
		req.setAttribute("message", "Hello Servlet !");
		// View에 해당하는 JSP로 forward
		req.getRequestDispatcher("/12Servlet/HelloServlet.jsp").forward(req, resp);
		/*
		 	request 속성 JSP에서 사용 가능
		 */
	}
}
