package model2.mvcboard;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import common.DBConnPool;

public class MVCBoardDAO extends DBConnPool {

	// 기본 생성자를 통해, parent class 생성자 호출
	// 생략 가능
	public MVCBoardDAO() {
        super();
    }

	// 게시물의 갯수 count
    public int selectCount(Map<String, Object> map) {
        int totalCount = 0;
        // query : count all
        String query = "SELECT COUNT(*) FROM mvcboard";
        // 매개변수로 전달된 검색어 동적 추가
        if (map.get("searchWord") != null) {
            query += " WHERE " + map.get("searchField") + " "
                   + "LIKE '%" + map.get("searchWord") + "%'";
        }
        try {
            // Statement instance (정적query)
        	stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            // count() 함수는 항상 결과 출력, if 필요 없음.
            rs.next();
            totalCount = rs.getInt(1);
        } 
        catch (Exception e) {
            System.out.println("게시물 카운트 중 예외 발생");
            e.printStackTrace();
        }
        return totalCount;
    }

    // 게시물 레코드를 DTO에 저장하는 method
    public List<MVCBoardDTO> selectList(Map<String, Object> map) {
        // List collection : 숫서 필수
    	List<MVCBoardDTO> board = new Vector<MVCBoardDTO>();
        String query = "SELECT * FROM mvcboard";
        if (map.get("searchWord") != null) {
            query += " WHERE " + map.get("searchField")
                   + " LIKE '%" + map.get("searchWord") + "%' ";
        }
        // idx(sequence) 내림차순 정렬 (최근 게시물 최상단 위치)
        query += " ORDER BY idx DESC";

        try {
            psmt = con.prepareStatement(query);
            rs = psmt.executeQuery();

            while (rs.next()) {
                // DTO instance 생성 (1개의 record 저장 用)
            	MVCBoardDTO dto = new MVCBoardDTO();
                
                // 멤버변수의 type에 따라 getString(), getInt(), getDate()로 구분
            	// dto setter 이용 저장
            	dto.setIdx(rs.getString(1));
                dto.setId(rs.getString(2));
                dto.setTitle(rs.getString(3));
                dto.setContent(rs.getString(4));
                dto.setPostdate(rs.getDate(5));
                dto.setOfile(rs.getString(6));
                dto.setSfile(rs.getString(7));
                dto.setDowncount(rs.getInt(8));
                dto.setVisitcount(rs.getInt(9));

                // board(List collection)에 dto(1 record) 저장
                board.add(dto);
            }

        } 
        catch (Exception e) {
            System.out.println("게시물 조회 중 예외 발생");
            e.printStackTrace();
        }

        return board;
    
    }

    // 글 쓰기 처리를 위한 query
    public int insertWrite(MVCBoardDTO dto) {
        int result = 0;
        try {
            /* default 값이 없는 column만 지정
               idx는 sequence로 지정 */
        	String query = 
                "INSERT INTO mvcboard ( "
                + "idx, id, title, content, ofile, sfile) "
                + "VALUES ( "
                + "seq_board_num.NEXTVAL, ?, ?, ?, ?, ?)";
            
        	// query 실행 instance 생성
            psmt = con.prepareStatement(query);
            // in-parameter 설정
            psmt.setString(1, dto.getId());
            psmt.setString(2, dto.getTitle());
            psmt.setString(3, dto.getContent());
            psmt.setString(4, dto.getOfile());
            psmt.setString(5, dto.getSfile());
            // result에 행의 갯수가 반환됨
            result = psmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("게시물 입력 중 예외 발생");
            e.printStackTrace();
        }
        return result;
    }

    public MVCBoardDTO selectView(String idx) {
        // 1 record -> dto에 저장하기 위해 dto 생성
    	MVCBoardDTO dto = new MVCBoardDTO();    // Create a new DTO object
        // inner join (mvcboard-member(id))
    	String query = "SELECT Bo.*, Me.name FROM mvcboard Bo "
                     + " INNER JOIN member Me ON Bo.id=Me.id"
                     + " WHERE idx=?";          // Prepare the SQL query template

        try {
            psmt = con.prepareStatement(query); // Prepare the SQL statement
            psmt.setString(1, idx);             // Set the parameter
            rs = psmt.executeQuery();           // Execute the query

            // 게시물 유무 확인 후, dto 저장
            if (rs.next()) {                    // If result exists, store in DTO object
                dto.setIdx(rs.getString(1));    // Set idx
                dto.setId(rs.getString(2));     // Set id
                dto.setTitle(rs.getString(3));  // Set title
                dto.setContent(rs.getString(4)); // Set content
                dto.setPostdate(rs.getDate(5)); // Set post date
                dto.setOfile(rs.getString(6));  // Set ofile
                dto.setSfile(rs.getString(7));  // Set sfile
                dto.setDowncount(rs.getInt(8)); // Set downcount
                dto.setVisitcount(rs.getInt(9));// Set visitcount
                dto.setName(rs.getString(10));  // Set name
            }
        }
        catch (Exception e) {
            System.out.println("Exception occurred while viewing post details"); // Print exception message
            e.printStackTrace();                                                  // Print stack trace
        }
        return dto;    // Return the DTO object
    }
    
    public void updateVisitCount(String idx) {
        // visitcount 1 증감
    	String query = "UPDATE mvcboard SET "
                     + " visitcount=visitcount+1 "
                     + " WHERE idx=?";

        try {
            psmt = con.prepareStatement(query);
            psmt.setString(1, idx);
            /*
             	execute query
             		1. executeQuery() : SELECT, resultSet return
             		2. executeUpdate() : INSERT/UPDATE/DELETE, int return
             */
            //psmt.executeQuery();
            int result = psmt.executeUpdate();
        }
        catch (Exception e) {
            System.out.println("게시물 조회수 증가 중 예외 발생");
            e.printStackTrace();
        }
    }

    public void downCountPlus(String idx) {
        String sql = "UPDATE mvcboard SET "
                   + " downcount=downcount+1 "
                   + " WHERE idx=? ";

        try {
            psmt = con.prepareStatement(sql);
            psmt.setString(1, idx);
            psmt.executeUpdate();
        } 
        catch (Exception e) {}
    }
    
    public int deletePost(String idx) {
        int result = 0;
        try {
            String query = "DELETE FROM mvcboard WHERE idx=?";
            psmt = con.prepareStatement(query);
            psmt.setString(1, idx);
            result = psmt.executeUpdate();
        } 
        catch (Exception e) {
            System.out.println("게시물 삭제 중 예외 발생");
            e.printStackTrace();
        }
        return result;
    }

    // DB 내용 갱신 (file upload 지원)
    public int updatePost(MVCBoardDTO dto) {
        int result = 0;
        try {
            // 쿼리문 템플릿 준비 : 회원제로 idx & id 조건 추가
            String query = "UPDATE mvcboard"
                         + " SET title=?, content=?, ofile=?, sfile=? "
                         + " WHERE idx=? and id=?";

            // 쿼리문 준비, in-parameter 설정
            psmt = con.prepareStatement(query);
            psmt.setString(1, dto.getTitle());
            psmt.setString(2, dto.getContent());
            psmt.setString(3, dto.getOfile());
            psmt.setString(4, dto.getSfile());
            psmt.setString(5, dto.getIdx());
            psmt.setString(6, dto.getId());

            // 쿼리문 실행
            result = psmt.executeUpdate();
        } 
        catch (Exception e) {
            System.out.println("게시물 수정 중 예외 발생");
            e.printStackTrace();
        }
        return result;
    }

    public List<MVCBoardDTO> selectListPage(
    	    Map<String,Object> map) {
    	    List<MVCBoardDTO> board = new Vector<MVCBoardDTO>();
    	    String query =
    	        " SELECT * FROM ( "
    	        + " SELECT Tb.*, ROWNUM RNum FROM ( "
    	        + " SELECT * FROM mvcboard ";
    	    if (map.get("searchWord") != null) {
    	        query += " WHERE " + map.get("searchField")
    	            + " LIKE '%" + map.get("searchWord") + "%' ";
    	    }
    	    query += " ORDER BY idx DESC "
    	        + " ) Tb "
    	        + " ) "
    	        + " WHERE RNum BETWEEN ? AND ?";

    	    try {
    	        psmt = con.prepareStatement(query);
    	        psmt.setString(1, map.get("start").toString());
    	        psmt.setString(2, map.get("end").toString());
    	        rs = psmt.executeQuery();

    	        while (rs.next()) {
    	            MVCBoardDTO dto = new MVCBoardDTO();

    	            dto.setIdx(rs.getString(1));
    	            dto.setId(rs.getString(2));
    	            dto.setTitle(rs.getString(3));
    	            dto.setContent(rs.getString(4));
    	            dto.setPostdate(rs.getDate(5));
    	            dto.setOfile(rs.getString(6));
    	            dto.setSfile(rs.getString(7));
    	            dto.setDowncount(rs.getInt(8));
    	            dto.setVisitcount(rs.getInt(9));

    	            board.add(dto);
    	        }
    	    }
    	    catch (Exception e) {
    	        System.out.println("게시물 조회 중 예외 발생");
    	        e.printStackTrace();
    	    }
    	    return board;
    	}

    
    
    
    
}
