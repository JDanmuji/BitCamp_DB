package dbtest.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class SelectMain {

	private Connection conn;
	private PreparedStatement  pstmt;
	private ResultSet  rs;
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String name = "C##JAVA";
	private String passwd = "1234";
	
	public SelectMain() {
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
	
	public void SelectArticles()  {
//		Scanner scan = new Scanner(System.in);
//		System.out.print("이름 입력 : ");
//		String name = scan.next();
//		System.out.print("나이 입력 : ");
//		int age = scan.nextInt();
//		System.out.print("키 입력 : ");
//		double height = scan.nextDouble();
		
		this.getConnection();
		
		String sql = "select * from dbtest";
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery(); //테이블처럼 return;

			while(rs.next()){ //레코두 있으면 true, 없으면 false
			
				System.out.println(rs.getString("name") + "\t"
										+ rs.getInt("age")+ "\t"
										+ rs.getDouble("height")+ "\t"
										+ rs.getString("logtime")+ "\t"
									); 
				
			
			
			}
					
			int su = pstmt.executeUpdate();
			
			System.out.println(su + "행 이(가) 조회되었습니다.");
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			try {
				
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
				if (rs != null) rs.close();
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		
		
	}
	
	
	
	public static void main(String[] args) {
		SelectMain sm = new SelectMain();
		sm.SelectArticles();
	}
}
