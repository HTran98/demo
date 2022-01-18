package com.yellow.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.yellow.model.Answer;



public class AnswerDAO {
	public static void main(String[] args) {
		//System.out.println(getUserId());
	}
	
	public static List<Answer> getListAnswer(String questionId){
		List<Answer> list = new ArrayList<Answer>();
		Answer answer = null;
		String query = "SELECT [answerId] " + 
				"      ,[answerDetail] " + 
				"      ,[answerStatus] " + 
				"      ,[answerDeleteStatus] " + 
				"      ,[questionId] " + 
				"  FROM [dbo].[Answers] WHERE [questionId] = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, questionId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				answer = new Answer();
				answer.setAnswerId(rs.getString("answerId"));
				answer.setAnswerDetail(rs.getString("answerDetail"));
				answer.setAnswerStatus(rs.getInt("answerStatus"));
				answer.setAnswerDeleteStatus(rs.getInt("answerDeleteStatus"));
				answer.setQuestionId(rs.getString("questionId"));
				list.add(answer);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public static Answer  getAnswer(String answerId) {
		Answer answer = null;
		String query = "SELECT [categoryId] " + 
				"      ,[categoryName] " + 
				"      ,[categoryDeleteStatus] " + 
				"  FROM [dbo].[Categories] WHERE [categoryId] = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, answerId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
//				answer = new Answer();
//				answer.setCategoryId(rs.getString("categoryId"));
//				answer.setCategoryName(rs.getString("categoryName"));
//				answer.setCategoryDeleteStatus(rs.getInt("categoryDeleteStatus"));
				return answer;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return answer;
	}
	public static String getAnswerId() {
		String query = "SELECT [answerId] " + 
				"      ,[answerDetail] " + 
				"      ,[answerStatus] " + 
				"      ,[answerDeleteStatus] " + 
				"      ,[questionId] " + 
				"  FROM [dbo].[Answers] ORDER BY [answerId] DESC";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return convertAnswerId(rs.getString("answerId"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "A0000001";
	}
	public static String convertAnswerId(String answer) {
		int intAnswerId = Integer.parseInt(answer.substring(1,answer.length()));
		//xrSystem.out.println(intUserId);
		intAnswerId++;
		String strAnswerId = String.valueOf(intAnswerId);
		while(strAnswerId.length()<7) {
			strAnswerId = "0"+strAnswerId;
			//System.out.println(strUserId);
		}
		return "A"+strAnswerId;
	}
	public static boolean InsertAnswer(Answer answer) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int countInsert = 0;
		String query = "INSERT INTO [dbo].[Answers] " + 
				"           ([answerId] " + 
				"           ,[answerDetail] " + 
				"           ,[answerStatus] " + 
				"           ,[answerDeleteStatus] " + 
				"           ,[questionId]) " + 
				"     VALUES " + 
				"           (? " + 
				"           ,REPLACE(?,CHAR(13)+CHAR(10),' ') " + 
				"           ,? " + 
				"           ,? " + 
				"           ,?)";
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, answer.getAnswerId());
			pstmt.setString(2, answer.getAnswerDetail());
			pstmt.setInt(3, answer.getAnswerStatus());
			pstmt.setInt(4, answer.getAnswerDeleteStatus());
			pstmt.setString(5, answer.getQuestionId());
			countInsert = pstmt.executeUpdate();
			if (countInsert == 1) {
				System.out.println("Insert Answer thanh cong.");
				return true;
			}
		} catch (SQLException sqle) {
			// TODO Auto-generated catch block
			sqle.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean EditAnswer(Answer answer) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int countUpdate = 0;
		String query = "UPDATE [dbo].[Answers] " + 
				"   SET [answerDetail] = ? " + 
				"      ,[answerStatus] = ? " + 
				"      ,[answerDeleteStatus] = ? " + 
				"      ,[questionId] = ? " + 
				" WHERE [answerId] = ?";
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, answer.getAnswerDetail());
			pstmt.setInt(2, answer.getAnswerStatus());
			pstmt.setInt(3, answer.getAnswerDeleteStatus());
			pstmt.setString(4, answer.getQuestionId());
			pstmt.setString(5, answer.getAnswerId());
			countUpdate = pstmt.executeUpdate();
			if (countUpdate == 1) {
				System.out.println("Edit answer thanh cong.");
				return true;
			}
		} catch (SQLException sqle) {
			// TODO Auto-generated catch block
			sqle.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	public static boolean DeleteAnswer(String answerId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int countDel = 0;
		String query = "UPDATE [dbo].[Answers] " + 
				"   SET [answerDeleteStatus] = 1 " + 
				"      WHERE [answerId] = ?";
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, answerId);
			countDel = pstmt.executeUpdate();
			if (countDel == 1) {
				System.out.println("Delete answer thanh cong.");
				return true;
			}
		} catch (SQLException sqle) {
			// TODO Auto-generated catch block
			sqle.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

}
