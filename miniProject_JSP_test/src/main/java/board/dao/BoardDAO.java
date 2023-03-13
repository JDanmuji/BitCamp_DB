package board.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import board.bean.BoardDTO;
import board.dao.BoardDAO;

public class BoardDAO {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	private DataSource ds;
	
	private static BoardDAO boardDAO = new BoardDAO();
	
	public static BoardDAO getInstance() {
		return boardDAO;
	}
	
	public static void close(Connection conn, PreparedStatement pstmt) { // 구현
		try {
			if(pstmt != null) pstmt.close(); // 확인사살
			if(conn != null) conn.close(); // db는 항상 마지막을 close()로 끝내야 한다.
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close(); // 확인사살
			if(conn != null) conn.close(); // db는 항상 마지막을 close()로 끝내야 한다.
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
	public BoardDAO() {
		try {
			Context ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");
		
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
//	
//	public void getConnection() {
//		
//	}
//	
	public void boardWrite(Map<String, String> map) {
		String sql = "insert into board(seq, id, name, email, subject, content, ref) "
					+ "values(seq_board.nextval, ?, ?, ?, ?, ?, seq_board.nextval)";
		
		try {
			conn = ds.getConnection();
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, map.get("id"));
			pstmt.setString(2, map.get("name"));
			pstmt.setString(3, map.get("email"));
			pstmt.setString(4, map.get("subject"));
			pstmt.setString(5, map.get("content"));
	
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			BoardDAO.close(conn, pstmt);
		}
	
	}
	
	public List<BoardDTO> boardList(Map<String, Integer> map) {
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		
		String sql = "SELECT * FROM "
				+ "(SELECT ROWNUM RN, TT.* FROM "
				+ "(SELECT * FROM BOARD ORDER BY REF DESC, STEP ASC) TT "
				+ ") WHERE RN>=? AND RN<=?";
	
		try {
			conn = ds.getConnection();
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,  map.get("startNum"));
			pstmt.setInt(2, map.get("endNum"));
			rs = pstmt.executeQuery();

			while(rs.next()) {
				BoardDTO boardDTO = new BoardDTO();
				boardDTO.setSeq(rs.getInt("seq"));
				boardDTO.setId(rs.getString("id"));
				boardDTO.setName(rs.getString("name"));
				boardDTO.setEmail(rs.getString("email"));
				boardDTO.setSubject(rs.getString("subject"));
				boardDTO.setContent(rs.getString("content"));
				boardDTO.setRef(rs.getInt("ref"));
				boardDTO.setLev(rs.getInt("lev"));
				boardDTO.setStep(rs.getInt("step"));
				boardDTO.setPseq(rs.getInt("pseq"));
				boardDTO.setReply(rs.getInt("reply"));
				boardDTO.setHit(rs.getInt("hit"));
				boardDTO.setLogtime(rs.getDate("logtime"));
			
				list.add(boardDTO);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			list = null;
		} finally {
			BoardDAO.close(conn, pstmt, rs);
		}
		
		return list;
	}
	
	public int getTotalA() {
		int totalA = 0;
		String sql = "SELECT COUNT(*) as cnt FROM BOARD";
		
		try { 
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				totalA = Integer.parseInt(rs.getString("cnt")); // 현재 개수를 꺼내와라.	
			}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			BoardDAO.close(conn, pstmt, rs);
		}
		
		return totalA;
	}
	
	public BoardDTO getBoard(int seq) {
		BoardDTO boardDTO = new BoardDTO();
		System.out.println("11111111111111111111");
		String sql = "SELECT seq, id, name, email, subject, content, ref, lev, step, pseq, reply,hit, logtime from BOARD WHERE SEQ=?";
		int num = 0;
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, seq);
			System.out.println("22222222222222");
			
			rs = pstmt.executeQuery();
			
			System.out.println("rs = " + rs.getRow());		
			if(rs.next()) { 
				System.out.println("3333333333333");
				num++;
				// 
	            boardDTO.setSeq(rs.getInt("seq"));
	            boardDTO.setId(rs.getString("id"));
	            boardDTO.setName(rs.getString("name"));
	            boardDTO.setEmail(rs.getString("email"));
	            boardDTO.setSubject(rs.getString("subject"));
	            boardDTO.setContent(rs.getString("content"));
	            boardDTO.setRef(rs.getInt("ref"));
	            boardDTO.setLev(rs.getInt("lev"));
	            boardDTO.setStep(rs.getInt("step"));
	            boardDTO.setPseq(rs.getInt("pseq"));
	            boardDTO.setReply(rs.getInt("reply"));
	            int hit = rs.getInt("hit"); // 조회수 hit 변수 생성하고 get한다.
	            boardDTO.setHit(hit); // setHit에 get한 hit 변수 대입
	            //hit++; // +1 증가
	            hitUpdate(hit, seq);
	            boardDTO.setLogtime(rs.getDate("logtime"));
	            System.out.println(num);
			}
					
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			BoardDAO.close(conn, pstmt, rs);
		}
		return boardDTO;
	}
	
	public void hitUpdate(int hit, int seq) {
		String sql = "UPDATE BOARD SET hit=? where seq=?";
		int result = 0;
		try {
		//	conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, hit);
			pstmt.setInt(2, seq); 
			//쿼리 카운트
			pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		
		} 
		
	}
	
}
