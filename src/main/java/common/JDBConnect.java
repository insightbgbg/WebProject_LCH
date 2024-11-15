package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import jakarta.servlet.ServletContext;

public class JDBConnect {

// 멤버 변수 : DB 연결, 정적 쿼리 실행, 동적 쿼리 실행, select 결과 반환	
// 정적 쿼리 : 수정 불가(=값 입력 불가), 동적 쿼리 : 값 입력 가능	
	public Connection con;
	public Statement stmt;
	public PreparedStatement psmt;
	public ResultSet rs;
	
	// 기본 생성자 : 매개 변수가 없는 생성자
	public JDBConnect() {
		
		try {
			// 오라클 드라이버를 메모리에 로드
			Class.forName("oracle.jdbc.OracleDriver");
			// 컨넥션URL 생성
			String url ="jdbc:oracle:thin:@localhost:1521:xe";
			String id = "education";
			String pwd = "1234";
			// DB connection
			con= DriverManager.getConnection(url,id,pwd);
			// 커넥션 인스턴스 반환
			System.out.println("DB connection success(기본 생성자)");
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	// 인자 생성자 1
	public JDBConnect(String driver, String url, String id, String pwd) {
		
		try {
			// 오라클 드라이버를 메모리에 로드
			Class.forName(driver);
			// DB connection
			con= DriverManager.getConnection(url,id,pwd);
			// 커넥션 인스턴스 반환
			System.out.println("DB connection success(인자 생성자1)");
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 인자 생성자 2 : application 내장 객체의 원본 타입을 매개 변수로 선언
	public JDBConnect(ServletContext application) {
		
		try {
			
/*
 			내장 객체를 매개 변수를 통해 전달받았으므로,
 			Java class 내에서 web.xml에 접근 가능
 */
			String driver=application.getInitParameter("OracleDriver");
			Class.forName(driver);
			
			String url=application.getInitParameter("OracleURL");
			String id=application.getInitParameter("OracleId");
			String pwd=application.getInitParameter("OraclePwd");

			System.out.println(driver+"="+url+"="+id+"="+pwd);			
			
			// 오라클 드라이버를 메모리에 로드
			Class.forName(driver);
			// DB connection
			con= DriverManager.getConnection(url,id,pwd);
			
			System.out.println("DB connection success(인수 생성자2)");
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void close() {
		
		try {
			if (rs !=null) rs.close();
			if (stmt !=null) stmt.close();
			if (psmt !=null) psmt.close();
			if (con !=null) con.close();
			
			System.out.println("JDBC 자원 해제");
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}
