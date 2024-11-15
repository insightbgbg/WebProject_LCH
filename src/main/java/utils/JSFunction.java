package utils;

import java.io.PrintWriter;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.jsp.JspWriter;


public class JSFunction {

// 자주 사용하는 javascript 함수를 클래스로 정의
	/*
	  stitic method : instance 생성하지 않고, 바로 호출 가능
	 */
	
	public static void alertLocation(String msg,String url,
				JspWriter out) {
		/*
		 java 클래스에서는 jsp 내장객체를 즉시 사용할 수 없으므로,
		 매개 변수로 전달받아 사용해야 함.
		 */
		try {
			String script = ""
							+ "<script>"
							+ "       alert('"+msg+"');"
							+ "       location.href='"+url+"';"
							+"</script>";
			out.println(script);
			
		}
		catch (Exception e) {}
		
	}

	
	public static void alertBack(String msg,JspWriter out) {

		try {
			String script = ""
							+ "<script>"
							+ "       alert('"+msg+"');"
							+ "       history.back();"
							+"</script>";
			out.println(script);
			
		}
		catch (Exception e) {}
		
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 	servlet에서 javascript를 실행할 수 있도록 정의
	 	JSP로 forward하지 않고, 즉시 출력하기 위함
	 */
	public static void alertLocation(HttpServletResponse resp, String msg, String url) {
	    try {
	        // content type, jsp forward가 아닌 servlet 직접 실행을 위해 포함함.
	    	resp.setContentType("text/html;charset=UTF-8");
	        PrintWriter writer = resp.getWriter();
	        // alert message를 띄우고, 특정 page로 이동할 수 있는 javascript
	        String script = ""
	            + "<script>"
	            + "    alert('" + msg + "');"
	            + "    location.href='" + url + "';"
	            + "</script>";
	        writer.print(script);
	    } catch (Exception e) {
	        // 예외 처리 생략
	    }
	}

	public static void alertBack(HttpServletResponse resp, String msg) {
	    try {
	        resp.setContentType("text/html;charset=UTF-8");
	        PrintWriter writer = resp.getWriter();
	        String script = ""
	            + "<script>"
	            + "    alert('" + msg + "');"
	            + "    history.back();"
	            + "</script>";
	        writer.print(script);
	    } catch (Exception e) {
	        // 예외 처리 생략
	    }
	}
}

	
	