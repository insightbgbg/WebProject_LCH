package common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/* 
  	JNDI(Java Naming and Directory Interface)
  	디렉토리 서비스에서 제공하는 데이터 및 객체를 참조
  	외부에 있는 객체를 이름으로 찾아오는 기술

	DBCP (DataBase Connection Pool)
	DB와 연결된 connection instance를 미리 만들어 Pool에 저장한 후,
	필요 시 사용하고, 사용 후 반납하는 기술
	DB의 부하를 줄이고, 자원을 효율적으로 관리
*/

public class DBConnPool {

	// 멤버 변수
	public Connection con;
	public Statement stmt;
	public PreparedStatement psmt;
	public ResultSet rs;
	
	// 기본 생성자
	public DBConnPool() {
		
		try {
			// 1. Context instance 생성 = Tomcat webserver
			Context initCtx = new InitialContext();
			// 2. JNDI 서버스 구조의 초기 Root directory 가져 옴.
			Context ctx = (Context)initCtx.lookup("java:comp/env");
			/* 3. server.xml에 등록한 네이밍을 Lookup하여 Datasource를 가져 옴.
					DB 연결을 위한 정보 가지고 있음.
			*/
			DataSource source = (DataSource)ctx.lookup("dbcp_myoracle");
			// 4. Connection pool에 생성한 instance 사용
			con=source.getConnection();
			System.out.println("DB connection pool connection success");
		}
		
		catch (Exception e) {
			System.out.println("DB connection pool connection fail");
			e.printStackTrace();
		}
	}
	
	public void close() {
		
		try {
			
			if (rs !=null) rs.close();
			if (stmt !=null) stmt.close();
			if (psmt !=null) psmt.close();
			if (con !=null) con.close();
			
			System.out.println("DB connection pool connection return");
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
