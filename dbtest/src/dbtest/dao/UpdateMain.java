package dbtest.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UpdateMain {
	private Connection conn;
	private PreparedStatement  pstmt;
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String name = "C##JAVA";
	private String passwd = "1234";
	
	public UpdateMain() {
		//driver loading
		try {
			Class.forName(driver);
			System.out.println(" good");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //Class 타입으로 생성ㄴ
		
	}
	
	
	
	public void getConnection()  {
	
		try {
			conn = DriverManager.getConnection(url, name, passwd);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
// select + update 문
//	public void UpdateArticles()  {
//		Scanner scan = new Scanner(System.in);
//		System.out.print("검색할 이름 입력 : ");
//		String name = scan.next();
//		
//		this.getConnection();
//		
//		String sql_1 = "select age, height from dbtest where name like '%"  + name + "%'";
//		String sql_2 = "update dbtest set age = ?, height =? where name like '%"  + name + "%'";
//		
//		try {
//
//			pstmt = conn.prepareStatement(sql_1);
//			ResultSet rs = pstmt.executeQuery();
//			
//			int su = 0;
//			
//			while(rs.next()){
//				int age = rs.getInt("age");
//				double height = rs.getDouble("height");
//				
//				pstmt = conn.prepareStatement(sql_2);
//				pstmt.setInt(1, age+1);
//				pstmt.setDouble(2, height+1);
//				su = pstmt.executeUpdate();
//			}
//			
//
//			System.out.println(su + "행 이(가) 갱신되었습니다.");
//			
//		} catch (SQLException e) {
//			
//			e.printStackTrace();
//			
//		} finally {
//			
//			try {
//				
//				if (pstmt != null) pstmt.close();
//				if (conn != null) conn.close();
//				
//			} catch (SQLException e) {
//				
//				e.printStackTrace();
//			}
//		}
//		
//		
//	}
//	
	
	public void UpdateArticles()  {
		Scanner scan = new Scanner(System.in);
		System.out.print("검색할 이름 입력 : ");
		String name = scan.next();
		
		this.getConnection();
		
	
		String sql = "update dbtest set age = age+1, height = height+1 where name like ?";
		
		try {

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + name + "%");
			
			
			int su = pstmt.executeUpdate();
			
			

			System.out.println(su + "행 이(가) 갱신되었습니다.");
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			try {
				
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		
		
	}
	
	public static void main(String[] args) {
		UpdateMain um = new UpdateMain();
		um.UpdateArticles();
		
	}
}

