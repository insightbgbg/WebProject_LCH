package membership;

/*
	DTO (Data Transfer Object) :
	JSP와 Java 파일 간 데이터 전달 객체

	자바빈스 (=java class) 규약 :
	1. 자바빈스는 기본 패키지 이외의 패키지에 소속
	2. 멤버변수의 접근 지정자는 private
	3. 기본 생성자 필수 
	4. getter, setter 필수
	5. getter, setter 접근 지정자 public
 */

public class MemberDTO {

	// 멤버 변수 : member table column과 동일하게 생성
	private String id;
	private String pass;
	private String name;
	private String regidate;

	/*
	 생성자는 필요한 경우만 생성
	 생성자를 정의하지 않을 경우 컴파일러가 기본 생성자 자동 추가
	 */
	
	/*
	  멤버 변수에 접근하기 위해서는 getter, setter 필요 
	 */
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRegidate() {
		return regidate;
	}
	public void setRegidate(String regidate) {
		this.regidate = regidate;
	}
	
}

