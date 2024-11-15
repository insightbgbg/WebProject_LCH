package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 web.xml mapping 대신 @WebServlet annotation 사용
 요청에 대한 매핑
 */
@WebServlet("/12Servlet/AnnoMapping.do")
public class AnnoMapping extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setAttribute("message", "@webServlet으로 mapping");
		req.getRequestDispatcher("/12Servlet/AnnoMapping.jsp").forward(req, resp);
		
	}
	
}
