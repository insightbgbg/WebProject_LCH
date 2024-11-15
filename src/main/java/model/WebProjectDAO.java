package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WebProjectDAO {
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe"; // 예시 URL
    private static final String DB_USER = "webproject_db";
    private static final String DB_PASSWORD = "1234";

    // 데이터베이스 연결 메서드
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // 회원 관리 기능

    // 회원 추가
    public void addMember(WebProjectDTO.Member member) {
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

    // 회원 수정
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

    // 게시판 관리 기능

    // 게시물 추가
    public void addBoard(WebProjectDTO.Board board) {
        String sql = "INSERT INTO board (board_id, member_id, board_type, title, content, created_at, view_count, like_count, is_deleted) VALUES (?, ?, ?, ?, ?, SYSDATE, 0, 0, 'N')";
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

    // 모든 게시물 조회
    public List<WebProjectDTO.Board> getAllBoards() {
        List<WebProjectDTO.Board> boards = new ArrayList<>();
        String sql = "SELECT * FROM board WHERE is_deleted = 'N' ORDER BY created_at DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boards;
    }

    // 특정 게시물 조회
    public WebProjectDTO.Board getBoardById(int boardId) {
        String sql = "SELECT * FROM board WHERE board_id = ?";
        WebProjectDTO.Board board = null;
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, boardId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    board = new WebProjectDTO.Board();
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
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return board;
    }

    // 게시물 수정
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

    // 게시물 삭제 (논리 삭제)
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

    // 게시물 조회수 증가
    public void incrementViewCount(int boardId) {
        String sql = "UPDATE board SET view_count = view_count + 1 WHERE board_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, boardId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 댓글 관리 기능

    // 댓글 추가
    public void addComment(WebProjectDTO.Comment comment) {
        String sql = "INSERT INTO comment_table (comment_id, board_id, member_id, content, created_at, like_count, is_deleted) VALUES (?, ?, ?, ?, SYSDATE, 0, 'N')";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, comment.getCommentId());
            stmt.setInt(2, comment.getBoardId());
            stmt.setString(3, comment.getMemberId());
            stmt.setString(4, comment.getContent());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 특정 게시물에 대한 댓글 목록 조회
    public List<WebProjectDTO.Comment> getCommentsByBoardId(int boardId) {
        String sql = "SELECT * FROM comment_table WHERE board_id = ? AND is_deleted = 'N' ORDER BY created_at ASC";
        List<WebProjectDTO.Comment> comments = new ArrayList<>();
        
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

    // 댓글 삭제 (논리 삭제)
    public void deleteComment(int commentId) {
        String sql = "UPDATE comment_table SET is_deleted = 'Y' WHERE comment_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, commentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
