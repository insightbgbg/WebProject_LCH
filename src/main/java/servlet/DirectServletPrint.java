package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DirectServletPrint extends HelloServlet {

	/*
	 	post method -> doPost() overriding
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		/* set content-type for direct HTML print  */
		resp.setContentType("text/html;charset=UTF-8");
		// PrintWriter instance 생성
		PrintWriter writer = resp.getWriter();	
		
		writer.println("<html>");
		writer.println("<head><title>DirectServletPrint</title></head>");
		writer.println("<body>");
		writer.println("<h2>servlet direct print</h2>");
		writer.println("<p>not forward to jsp</p>");
		writer.println("</body>");
		writer.println("</html>");
		// close instance
		writer.close();
		/*
		  이 방식은 JSP 없이 servlet에서 직접 내용 출력 시 사용.
		  REST-API 제작 시 주로 사용
		 */
	}
	
}
