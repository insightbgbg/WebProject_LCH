package membership;


import common.JDBConnect;
import jakarta.servlet.ServletContext;

/*
 DAO (Data Access Object)
 DB 접속, query 실행(CRUD), 결과 반환
*/

// JDBC class 상속, DB 연결
public class MemberDAO extends JDBConnect {

	// 생성자1 : 4개의 매개 변수로 정의
	public MemberDAO(String drv, String url, String id, String pw) {
		// super()를 통해 부모 클래스의 생성자 호출
		super(drv, url, id, pw);	
	}
	
	// 생성자2 : application 내장 객체를 매개 변수로 정의
	public MemberDAO(ServletContext application) {
		super(application);
	}
	
	/*
	 입력한 id, pw를 통해 회원 table select
	 존재할 경우 DTO 객체에 저장 후 반환
	 */
	
	public MemberDTO getMemberDTO(String uid, String upass) {

		// 회원 인증을 위한 query 실행, 회원 정보 저장 instance 생성
		MemberDTO dto = new MemberDTO();

		/* 
		 	로그인 폼에서 입력한 id, pw를 통해 인파라미터를 설정할 수 있도록
		 	query 작성
		 */
		String query = "SELECT * FROM member Where id=? AND pass =?";

		try {
			// 쿼리문 실행을 위한 prepared instance 생성
			psmt = con.prepareStatement(query);
			// 쿼리문 인파라미터 설정
			psmt.setString(1,uid);
			psmt.setString(2,upass);
			// 퀴리문 실행, 결과 값 반환
			rs=psmt.executeQuery();
			
			// 결과 값 있는지 확인
			if (rs.next()) {
				// 회원 정보가 있다면 DTO 객체에 저장 
				dto.setId(rs.getString("id"));
				dto.setPass(rs.getString("pass"));
				dto.setName(rs.getString(3));
				dto.setRegidate(rs.getString(4));
				
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// 회원정보 DTO 개체 반환
		return dto;
	}
}
