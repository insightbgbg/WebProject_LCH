package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WebProjectDAO {

/*
************************************************
    DB 연결
************************************************
*/

	private Connection getConnection() throws SQLException {
	    String dbUrl = "jdbc:oracle:thin:@localhost:1521:xe";
	    String dbUser = "webproject_db";
	    String dbPassword = "1234";
	    
	    try {
	        // Oracle JDBC Driver 로드 (필요할 경우)
	        Class.forName("oracle.jdbc.OracleDriver");
	        // DB 연결
	        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
	    } catch (ClassNotFoundException e) {
	        throw new SQLException("JDBC 드라이버를 로드할 수 없습니다: " + e.getMessage());
	    } catch (SQLException e) {
	        throw new SQLException("DB 연결에 실패했습니다: " + e.getMessage());
	    }
	}

/*
************************************************
   댓글 관리
************************************************
*/

	// 댓글 추가	
	public void addComment(WebProjectDTO.Comment comment) {

		String sql = "INSERT INTO comments (comment_id, board_id, member_id, content, created_at, like_count, is_deleted) " +
	                 "VALUES (comment_seq.NEXTVAL, ?, ?, ?, SYSDATE, 0, 'N')";

		try (Connection conn = getConnection();

			PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, comment.getBoardId());
	        stmt.setString(2, comment.getMemberId());
	        stmt.setString(3, comment.getContent());
	        
	         // 디버깅 (댓글 추가)
            System.out.println("======= comment DAO add : sql =================" + sql);
            System.out.println("======= comment DAO add : BoardId =================" + comment.getBoardId());
            System.out.println("======= comment DAO add : MemberId =================" + comment.getMemberId());
            System.out.println("======= comment DAO add : Content =================" + comment.getContent());
	        
	        stmt.executeUpdate();

		} catch (SQLException e) {

			e.printStackTrace();
	    }
	}

	// 댓글 조회	(comment_id)
	public WebProjectDTO.Comment getCommentById(int commentId) {
	    String sql = "SELECT * FROM comments WHERE comment_id = ? AND is_deleted = 'N'";
	    WebProjectDTO.Comment comment = null;

	    try (Connection conn = getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, commentId);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                comment = new WebProjectDTO.Comment();
	                comment.setCommentId(rs.getInt("comment_id"));
	                comment.setBoardId(rs.getInt("board_id"));
	                comment.setMemberId(rs.getString("member_id"));
	                comment.setContent(rs.getString("content"));
	                comment.setCreatedAt(rs.getDate("created_at"));
	                comment.setUpdatedAt(rs.getDate("updated_at"));
	                comment.setLikeCount(rs.getInt("like_count"));
	                comment.setIsDeleted(rs.getString("is_deleted"));
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return comment;
	}
	
	
	// 댓글 조회	(board_id)
	public List<WebProjectDTO.Comment> getCommentsByBoardId(int boardId) {
	    List<WebProjectDTO.Comment> comments = new ArrayList<>();
	    String sql = "SELECT * FROM comments WHERE board_id = ? AND is_deleted = 'N' ORDER BY created_at ASC";
	    try (Connection conn = getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, boardId);
	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                WebProjectDTO.Comment comment = new WebProjectDTO.Comment();
	                comment.setCommentId(rs.getInt("comment_id"));
	                comment.setBoardId(rs.getInt("board_id"));
	                comment.setMemberId(rs.getString("member_id"));
	                comment.setContent(rs.getString("content"));
	                comment.setCreatedAt(rs.getDate("created_at"));
	                comment.setUpdatedAt(rs.getDate("updated_at"));
	                comment.setLikeCount(rs.getInt("like_count"));
	                comment.setIsDeleted(rs.getString("is_deleted"));
	                comments.add(comment);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return comments;
	}

	// 댓글 삭제	
	public void deleteComment(int commentId) {
	    String sql = "UPDATE comments SET is_deleted = 'Y' WHERE comment_id = ?";
	    try (Connection conn = getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, commentId);
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	// 댓글 수정	
	public void updateComment(WebProjectDTO.Comment comment) {
	    String sql = "UPDATE comments SET content = ?, updated_at = SYSDATE WHERE comment_id = ?";
	    try (Connection conn = getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, comment.getContent());
	        stmt.setInt(2, comment.getCommentId());
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
/*
************************************************
   회원 관리
************************************************
*/

	// 회원 추가
/*    public void addMember(WebProjectDTO.Member member) {
        String sql = "INSERT INTO member (member_id, password, name, email, phone, join_date, status, role) VALUES (?, ?, ?, ?, ?, SYSDATE, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, member.getMemberId());
            stmt.setString(2, member.getPassword());
            stmt.setString(3, member.getName());
            stmt.setString(4, member.getEmail());
            stmt.setString(5, member.getPhone());
            stmt.setString(6, member.getStatus());
            stmt.setString(7, member.getRole());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
*/

// 회원 추가	

	public void addMember(WebProjectDTO.Member member) {

		String sql = "INSERT INTO member (member_id, password, name, email, phone, join_date, status, role) VALUES (?, ?, ?, ?, ?, SYSDATE, 'A', 'USER')";

		try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, member.getMemberId());
            stmt.setString(2, member.getPassword());
            stmt.setString(3, member.getName());
            stmt.setString(4, member.getEmail());
            stmt.setString(5, member.getPhone());
            
            int rows = stmt.executeUpdate();
            //System.out.println("Rows inserted: " + rows); // 디버깅용 출력

		} catch (SQLException e) {
            e.printStackTrace();
            //System.out.println("SQL error: " + e.getMessage()); // 에러 디버깅
        }
    }
    
    
// 회원 조회 (ID로 조회)

	public WebProjectDTO.Member getMemberById(String memberId) {

		String sql = "SELECT * FROM member WHERE member_id = ?";

		WebProjectDTO.Member member = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, memberId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    member = new WebProjectDTO.Member();
                    member.setMemberId(rs.getString("member_id"));
                    member.setPassword(rs.getString("password"));
                    member.setName(rs.getString("name"));
                    member.setEmail(rs.getString("email"));
                    member.setPhone(rs.getString("phone"));
                    member.setJoinDate(rs.getDate("join_date"));
                    member.setLastLogin(rs.getDate("last_login"));
                    member.setStatus(rs.getString("status"));
                    member.setRole(rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return member;
    }

// 회원 수정 (전체 columns)

	public void updateMember(WebProjectDTO.Member member) {

		String sql = "UPDATE member SET password = ?, name = ?, email = ?, phone = ?, status = ?, role = ? WHERE member_id = ?";

		try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, member.getPassword());
            stmt.setString(2, member.getName());
            stmt.setString(3, member.getEmail());
            stmt.setString(4, member.getPhone());
            stmt.setString(5, member.getStatus());
            stmt.setString(6, member.getRole());
            stmt.setString(7, member.getMemberId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

// 회원 조회 (ID, email로 조회) : forgot_password 활용 

	public WebProjectDTO.Member getMemberByIdAndEmail(String memberId, String email) {

		String sql = "SELECT * FROM member WHERE member_id = ? AND email = ?";

		WebProjectDTO.Member member = null;

	    try (Connection conn = getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, memberId);
	        stmt.setString(2, email);

	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                member = new WebProjectDTO.Member();
	                member.setMemberId(rs.getString("member_id"));
	                member.setName(rs.getString("name"));
	                member.setEmail(rs.getString("email"));
	                // Set other fields if necessary
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return member;
	}

	
// 회원 수정 (email) : forgot_password 활용    

	// Update member password
	public void updatePassword(String memberId, String newPassword) {
	    String sql = "UPDATE member SET password = ? WHERE member_id = ?";
	    try (Connection conn = getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, newPassword);
	        stmt.setString(2, memberId);
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
        
    
// 회원 삭제
    public void deleteMember(String memberId) {
        String sql = "DELETE FROM member WHERE member_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, memberId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

/*
************************************************
    게시판 관리
************************************************
*/

 // 게시글 수정 메서드
    public void updateBoard(WebProjectDTO.Board board) {
        String sql = "UPDATE board SET title = ?, content = ?, updated_at = SYSDATE WHERE board_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, board.getTitle());
            stmt.setString(2, board.getContent());
            stmt.setInt(3, board.getBoardId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

 // 게시글 삭제 메서드 (논리적 삭제)
    public void deleteBoard(int boardId) {
        String sql = "UPDATE board SET is_deleted = 'Y' WHERE board_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, boardId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
/*    
 // 특정 게시물 조회
    public WebProjectDTO.Board getBoardById(int boardId) {
        String sql = "SELECT * FROM board WHERE board_id = ?";
        WebProjectDTO.Board board = null;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, boardId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    board = new WebProjectDTO.Board();
                    board.setBoardId(rs.getInt("board_id"));
                    board.setMemberId(rs.getString("member_id"));
                    board.setBoardType(rs.getString("board_type"));
                    board.setTitle(rs.getString("title"));
                    board.setContent(rs.getString("content"));
                    board.setCreatedAt(rs.getDate("created_at"));
                    board.setViewCount(rs.getInt("view_count"));
                    board.setLikeCount(rs.getInt("like_count"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return board;
    }
*/
    
 // 단일 게시글 조회
    public WebProjectDTO.Board getBoardById(int boardId) {
        WebProjectDTO.Board board = null;
        String sql = "SELECT * FROM board WHERE board_id = ? AND is_deleted = 'N'";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, boardId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    board = new WebProjectDTO.Board();
                    board.setBoardId(rs.getInt("board_id"));
                    board.setMemberId(rs.getString("member_id"));
                    board.setBoardType(rs.getString("board_type"));
                    board.setTitle(rs.getString("title"));
                    board.setContent(rs.getString("content"));
                    board.setOriginalFilename(rs.getString("original_filename")); // 추가
                    board.setStoredFilename(rs.getString("stored_filename"));     // 추가
                    board.setFilePath(rs.getString("file_path"));                 // 추가
                    board.setCreatedAt(rs.getDate("created_at"));
                    board.setUpdatedAt(rs.getDate("updated_at"));
                    board.setViewCount(rs.getInt("view_count"));
                    board.setLikeCount(rs.getInt("like_count"));
                    board.setIsDeleted(rs.getString("is_deleted"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return board;
    }
    
    
    
    
 // 조회수 증가
    public void incrementViewCount(int boardId) {
        String sql = "UPDATE board SET view_count = view_count + 1 WHERE board_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, boardId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

 // 좋아요 수 증가
    public void incrementLikeCount(int boardId) {
        String sql = "UPDATE board SET like_count = like_count + 1 WHERE board_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, boardId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
/*    
// 게시물 추가
    public void addBoard(WebProjectDTO.Board board) {
        String sql = "INSERT INTO board (board_id, member_id, board_type, title, content, created_at, view_count, like_count, is_deleted) "
        						+ "VALUES (board_seq.NEXTVAL, ?, ?, ?, ?, SYSDATE, 0, 0, 'N')";

// 디버깅
System.out.println("================== DAO add 진입 ====================");
System.out.println("================== getBoardId : " +board.getBoardId());
System.out.println("================== getMemberId : " +board.getMemberId());
System.out.println("================== getBoardType : " +board.getBoardType());
System.out.println("================== getTitle : " +board.getTitle());
System.out.println("================== getContent : " +board.getContent());

        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, board.getBoardId());
            stmt.setString(2, board.getMemberId());
            stmt.setString(3, board.getBoardType());
            stmt.setString(4, board.getTitle());
            stmt.setString(5, board.getContent());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
*/

    public void addBoardWithFile(WebProjectDTO.Board board) {

    	String sql = "INSERT INTO board (board_id, member_id, board_type, title, content, original_filename, stored_filename, file_path, created_at, view_count, like_count, is_deleted) " +
                     "VALUES (board_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, SYSDATE, 0, 0, 'N')";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, board.getMemberId());
            pstmt.setString(2, board.getBoardType());
            pstmt.setString(3, board.getTitle());
            pstmt.setString(4, board.getContent());
            pstmt.setString(5, board.getOriginalFilename());
            pstmt.setString(6, board.getStoredFilename());
            pstmt.setString(7, board.getFilePath());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    
// 게시물 추가    
    public void addBoard(WebProjectDTO.Board board) {
        
    	String query = "INSERT INTO board (board_id, member_id, board_type, title, content, created_at, view_count, like_count, is_deleted) " +
                       "VALUES (board_seq.NEXTVAL, ?, ?, ?, ?, SYSDATE, 0, 0, 'N')";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // 매개변수 설정
            pstmt.setString(1, board.getMemberId());
            pstmt.setString(2, board.getBoardType());
            pstmt.setString(3, board.getTitle());
            pstmt.setString(4, board.getContent());

        	// 디버깅
        	//System.out.println("================== DAO addBoard query ====================" + query);
            
            
            // 쿼리 실행
            int result = pstmt.executeUpdate();
            // System.out.println("Rows inserted: " + result);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
// 페이징 처리된 게시물 목록 조회
/*    public List<WebProjectDTO.Board> getBoardListWithPaging(Map<String, Object> map) {
        List<WebProjectDTO.Board> boards = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM ( ");
        sql.append("    SELECT ROWNUM AS rnum, A.* FROM ( ");
        sql.append("        SELECT * FROM board WHERE is_deleted = 'N' ");

        if (map.containsKey("searchField") && map.containsKey("searchWord")) {
            sql.append(" AND ").append(map.get("searchField")).append(" LIKE ? ");
        }

        sql.append("        ORDER BY board_id DESC ");
        sql.append("    ) A ");
        sql.append("    WHERE ROWNUM <= ? ");
        sql.append(") ");
        sql.append("WHERE rnum >= ?");

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            if (map.containsKey("searchField") && map.containsKey("searchWord")) {
                pstmt.setString(paramIndex++, "%" + map.get("searchWord") + "%");
            }

            pstmt.setInt(paramIndex++, (int) map.get("end"));
            pstmt.setInt(paramIndex, (int) map.get("start"));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    WebProjectDTO.Board board = new WebProjectDTO.Board();
                    board.setBoardId(rs.getInt("board_id"));
                    board.setMemberId(rs.getString("member_id"));
                    board.setBoardType(rs.getString("board_type"));
                    board.setTitle(rs.getString("title"));
                    board.setContent(rs.getString("content"));
                    board.setCreatedAt(rs.getDate("created_at"));
                    board.setUpdatedAt(rs.getDate("updated_at"));
                    board.setViewCount(rs.getInt("view_count"));
                    board.setLikeCount(rs.getInt("like_count"));
                    board.setIsDeleted(rs.getString("is_deleted"));
                    boards.add(board);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boards;
    } */
    
/* // 페이징 처리된 게시물 목록 조회 (boardType 필터링 추가)
    public List<WebProjectDTO.Board> getBoardListWithPaging(Map<String, Object> map) {
        List<WebProjectDTO.Board> boards = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM ( ");
        sql.append("    SELECT ROWNUM AS rnum, A.* FROM ( ");
        sql.append("        SELECT * FROM board WHERE is_deleted = 'N' ");

        if (map.containsKey("boardType")) {
            sql.append(" AND board_type = ? ");
        }
        if (map.containsKey("searchField") && map.containsKey("searchWord")) {
            sql.append(" AND ").append(map.get("searchField")).append(" LIKE ? ");
        }

        sql.append("        ORDER BY board_id DESC ");
        sql.append("    ) A ");
        sql.append("    WHERE ROWNUM <= ? ");
        sql.append(") ");
        sql.append("WHERE rnum >= ?");

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            if (map.containsKey("boardType")) {
                pstmt.setString(paramIndex++, (String) map.get("boardType"));
            }
            if (map.containsKey("searchField") && map.containsKey("searchWord")) {
                pstmt.setString(paramIndex++, "%" + map.get("searchWord") + "%");
            }

            pstmt.setInt(paramIndex++, (int) map.get("end"));
            pstmt.setInt(paramIndex, (int) map.get("start"));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    WebProjectDTO.Board board = new WebProjectDTO.Board();
                    board.setBoardId(rs.getInt("board_id"));
                    board.setMemberId(rs.getString("member_id"));
                    board.setBoardType(rs.getString("board_type"));
                    board.setTitle(rs.getString("title"));
                    board.setContent(rs.getString("content"));
                    board.setCreatedAt(rs.getDate("created_at"));
                    board.setUpdatedAt(rs.getDate("updated_at"));
                    board.setViewCount(rs.getInt("view_count"));
                    board.setLikeCount(rs.getInt("like_count"));
                    board.setIsDeleted(rs.getString("is_deleted"));
                    boards.add(board);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boards;
    }    */
    
 // 페이징 처리된 게시물 목록 조회 (boardType 필터링 추가)
    public List<WebProjectDTO.Board> getBoardListWithPaging(Map<String, Object> map) {
        List<WebProjectDTO.Board> boards = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT * FROM ( " +
            "    SELECT ROWNUM AS rnum, A.* FROM ( " +
            "        SELECT * FROM board WHERE is_deleted = 'N' "
        );

        // boardType 조건 추가
        if (map.containsKey("boardType") && map.get("boardType") != null) {
            sql.append(" AND board_type = ? ");
        }

        // 검색 조건 추가
        if (map.containsKey("searchField") && map.containsKey("searchWord")
            && map.get("searchField") != null && map.get("searchWord") != null) {
            sql.append(" AND ").append(map.get("searchField")).append(" LIKE ? ");
        }

        sql.append(
            "        ORDER BY board_id DESC " +
            "    ) A " +
            "    WHERE ROWNUM <= ? " +
            ") WHERE rnum >= ?"
        );

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            // boardType 매개변수 설정
            if (map.containsKey("boardType") && map.get("boardType") != null) {
                pstmt.setString(paramIndex++, (String) map.get("boardType"));
            }

            // 검색 조건 매개변수 설정
            if (map.containsKey("searchField") && map.containsKey("searchWord")
                && map.get("searchField") != null && map.get("searchWord") != null) {
                pstmt.setString(paramIndex++, "%" + map.get("searchWord") + "%");
            }

            // 페이징 매개변수 설정
            pstmt.setInt(paramIndex++, (int) map.get("end"));
            pstmt.setInt(paramIndex, (int) map.get("start"));

            // SQL 및 매개변수 디버깅 출력
            /*System.out.println("Query: " + sql);
            System.out.println("Parameters: ");
            System.out.println("boardType: " + map.get("boardType"));
            System.out.println("searchField: " + map.get("searchField"));
            System.out.println("searchWord: " + map.get("searchWord"));
            System.out.println("start: " + map.get("start"));
            System.out.println("end: " + map.get("end"));*/

            // SQL 실행
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    WebProjectDTO.Board board = new WebProjectDTO.Board();
                    board.setBoardId(rs.getInt("board_id"));
                    board.setMemberId(rs.getString("member_id"));
                    board.setBoardType(rs.getString("board_type"));
                    board.setTitle(rs.getString("title"));
                    board.setContent(rs.getString("content"));
                    board.setCreatedAt(rs.getDate("created_at"));
                    board.setUpdatedAt(rs.getDate("updated_at"));
                    board.setViewCount(rs.getInt("view_count"));
                    board.setLikeCount(rs.getInt("like_count"));
                    board.setIsDeleted(rs.getString("is_deleted"));

                    // 디버깅 (board)
                    /*System.out.println("board_id: " + board.getBoardId());
                    System.out.println("member_id: " + board.getMemberId());
                    System.out.println("board_type: " + board.getBoardType());
                    System.out.println("title: " + board.getTitle());
                    System.out.println("content: " + board.getContent());*/
                    
                    boards.add(board);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boards;
    }

    

/*    
// 게시물 수 조회
    public int selectBoardCount(Map<String, Object> map) {
        int totalCount = 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM board WHERE is_deleted = 'N'");

        if (map.containsKey("searchField") && map.containsKey("searchWord")) {
            sql.append(" AND ").append(map.get("searchField")).append(" LIKE ?");
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            if (map.containsKey("searchField") && map.containsKey("searchWord")) {
                pstmt.setString(1, "%" + map.get("searchWord") + "%");
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    totalCount = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalCount;
    }
}
*/
    
 // 게시물 수 조회 (boardType 필터링 추가)
    public int selectBoardCount(Map<String, Object> map) {
        int totalCount = 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM board WHERE is_deleted = 'N'");

        if (map.containsKey("boardType")) {
            sql.append(" AND board_type = ?");
        }
        if (map.containsKey("searchField") && map.containsKey("searchWord")) {
            sql.append(" AND ").append(map.get("searchField")).append(" LIKE ?");
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            if (map.containsKey("boardType")) {
                pstmt.setString(paramIndex++, (String) map.get("boardType"));
            }
            if (map.containsKey("searchField") && map.containsKey("searchWord")) {
                pstmt.setString(paramIndex++, "%" + map.get("searchWord") + "%");
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    totalCount = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalCount;
    }
}


/*
************************************************
    코멘트 관리
************************************************
*/

    