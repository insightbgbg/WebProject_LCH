package model2.mvcboard;

public class MVCBoardDTO {

	// 멤버 변수 선언 : mvcboard table과 동일하게 선언
	// type : String default
	//			단 연산이 필요할 경우에만 int, Date
    private String idx;
    private String id;
    private String title;
    private String content;
    private java.sql.Date postdate;
    private String ofile;
    private String sfile;
    private int downcount;
    private int visitcount;
    // member table join, 회원의 이름을 출력하기 위함.
    private String name;

    // 생성자는 필요한 경우에만 정의
    
    public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public java.sql.Date getPostdate() {
		return postdate;
	}
	public void setPostdate(java.sql.Date postdate) {
		this.postdate = postdate;
	}
	public String getOfile() {
		return ofile;
	}
	public void setOfile(String ofile) {
		this.ofile = ofile;
	}
	public String getSfile() {
		return sfile;
	}
	public void setSfile(String sfile) {
		this.sfile = sfile;
	}
	public int getDowncount() {
		return downcount;
	}
	public void setDowncount(int downcount) {
		this.downcount = downcount;
	}
	public int getVisitcount() {
		return visitcount;
	}
	public void setVisitcount(int visitcount) {
		this.visitcount = visitcount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}

