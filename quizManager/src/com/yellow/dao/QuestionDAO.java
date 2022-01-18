package com.yellow.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.yellow.model.Answer;
import com.yellow.model.Question;

public class QuestionDAO {

	public static String getQuestionId() {
		String query = "SELECT TOP 1 [questionId] " + 
				"      ,[questionDetail] " + 
				"      ,[questionType] " + 
				"      ,[questionStatus] " + 
				"      ,[questionDeleteStatus] " + 
				"      ,[categoryId] " + 
				"  FROM [dbo].[Questions] ORDER BY [questionId] DESC";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return convertQuestionId(rs.getString("questionId"));
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
		return "Q0000001";
	}
	public static String convertQuestionId(String questionId) {
		int intQuestionId = Integer.parseInt(questionId.substring(1,questionId.length()));
		//xrSystem.out.println(intUserId);
		intQuestionId++;
		String strQuestionId = String.valueOf(intQuestionId);
		while(strQuestionId.length()<7) {
			strQuestionId = "0"+strQuestionId;
			//System.out.println(strUserId);
		}
		return "Q"+strQuestionId;
	}

	public static Question getQuestion(String questionId) {
		Question question = null;
		String query = "SELECT [questionId] " + 
				"      ,[questionDetail] " + 
				"      ,[questionType] " + 
				"      ,[questionStatus] " + 
				"      ,[questionDeleteStatus] " + 
				"      ,[categoryId] " + 
				"  FROM [dbo].[Questions] WHERE [questionId] = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, questionId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				question = new Question();
				question.setQuestionId(rs.getString("questionId"));
				question.setQuestionDetail(rs.getString("questionDetail"));
				question.setQuestionType(rs.getInt("questionType"));
				question.setQuestionStatus(rs.getInt("questionStatus"));
				question.setQuestionDeleteStatus(rs.getInt("questionDeleteStatus"));
				question.setCategoryId(rs.getString("categoryId"));
				return question;
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
		return question;
	}

	public static String InsertQuestion(Question question) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int countInsert = 0;
		String query = "INSERT INTO [dbo].[Questions] " + 
				"           ([questionId] " + 
				"           ,[questionDetail] " + 
				"           ,[questionType] " + 
				"           ,[questionStatus] " + 
				"           ,[questionDeleteStatus] " + 
				"           ,[categoryId]) " + 
				"     VALUES " + 
				"           (? " + 
				"           ,REPLACE(?,CHAR(13)+CHAR(10),' ')" + 
				"           ,? " + 
				"           ,? " + 
				"           ,? " + 
				"           ,?)";
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, question.getQuestionId());
			pstmt.setString(2, question.getQuestionDetail());
			pstmt.setInt(3, question.getQuestionType());
			pstmt.setInt(4, question.getQuestionStatus());
			pstmt.setInt(5, question.getQuestionDeleteStatus());
			pstmt.setString(6, question.getCategoryId());
			countInsert = pstmt.executeUpdate();
			if (countInsert == 1) {
				System.out.println("Insert Question thanh cong.");
				return question.getQuestionId();
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
		// TODO Auto-generated method stub
		return null;
	}

	public static boolean EditQuestion(Question question) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int countInsert = 0;
		String query = "UPDATE [dbo].[Questions] " + 
				"   SET [questionDetail] = ? " + 
				"      ,[questionType] = ? " + 
				"      ,[questionStatus] = ? " + 
				"      ,[questionDeleteStatus] = ? " + 
				"      ,[categoryId] = ? " + 
				" WHERE [questionId] = ?";
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, question.getQuestionDetail());
			pstmt.setInt(2, question.getQuestionType());
			pstmt.setInt(3, question.getQuestionStatus());
			pstmt.setInt(4, question.getQuestionDeleteStatus());
			pstmt.setString(5, question.getCategoryId());
			pstmt.setString(6, question.getQuestionId());
			countInsert = pstmt.executeUpdate();
			if (countInsert == 1) {
				System.out.println("Edit Question thanh cong.");
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

	public static boolean DeleteQuestion(String questionId) {
		List<Answer> aList = AnswerDAO.getListAnswer(questionId);
		for (Answer answer : aList) {
			AnswerDAO.DeleteAnswer(answer.getAnswerId());
		}
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement pstmt = null;
		int countInsert = 0;
		String query = "UPDATE [dbo].[Questions] " + 
				"   SET [questionDeleteStatus] = 1 " + 
				"      WHERE [questionId] = ?";
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, questionId);
			countInsert = pstmt.executeUpdate();
			if (countInsert == 1) {
				System.out.println("Delete Question thanh cong.");
				
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
	public static List<Question> getList20Question(String categoryId){
		List<Question> list = new ArrayList<Question>();
		Question question = null;
		String query = "SELECT TOP 20 [questionId] " + 
				"      ,[questionDetail] " + 
				"      ,[questionType] " + 
				"      ,[questionStatus] " + 
				"      ,[questionDeleteStatus] " + 
				"      ,[categoryId] " + 
				"  FROM [dbo].[Questions] WHERE [categoryId] = ? ORDER BY NEWID()";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, categoryId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				question = new Question();
				question.setQuestionId(rs.getString("questionId"));
				question.setQuestionDetail(rs.getString("questionDetail"));
				question.setQuestionType(rs.getInt("questionType"));
				question.setQuestionStatus(rs.getInt("questionStatus"));
				question.setQuestionDeleteStatus(rs.getInt("questionDeleteStatus"));
				question.setCategoryId(rs.getString("categoryId"));
				list.add(question);
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
	public static List<Question> getList10Question(String categoryId){
		List<Question> list = new ArrayList<Question>();
		Question question = null;
		String query = "SELECT TOP 10 [questionId] " + 
				"      ,[questionDetail] " + 
				"      ,[questionType] " + 
				"      ,[questionStatus] " + 
				"      ,[questionDeleteStatus] " + 
				"      ,[categoryId] " + 
				"  FROM [dbo].[Questions] WHERE [categoryId] = ? ORDER BY NEWID()";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, categoryId);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				question = new Question();
				question.setQuestionId(rs.getString("questionId"));
				question.setQuestionDetail(rs.getString("questionDetail"));
				question.setQuestionType(rs.getInt("questionType"));
				question.setQuestionStatus(rs.getInt("questionStatus"));
				question.setQuestionDeleteStatus(rs.getInt("questionDeleteStatus"));
				question.setCategoryId(rs.getString("categoryId"));
				list.add(question);
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
	public static List<Question> getAll() {
        List<Question> list = new ArrayList<Question>();
        Question question = null;
        String query = "SELECT [questionId] " + "      ,[questionDetail] " + "      ,[questionType] "
            + "      ,[questionStatus] " + "      ,[questionDeleteStatus] " + "      ,[categoryId] "
            + "  FROM [dbo].[Questions] WHERE [questionDeleteStatus] = 0 ";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnect.getConnect();
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                question = new Question();
                question.setQuestionId(rs.getString("questionId"));
                question.setQuestionDetail(rs.getString("questionDetail"));
                question.setQuestionType(rs.getInt("questionType"));
                question.setQuestionStatus(rs.getInt("questionStatus"));
                question.setQuestionDeleteStatus(rs.getInt("questionDeleteStatus"));
                question.setCategoryId(rs.getString("categoryId"));
                list.add(question);
            }
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            }
            catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return list;
    }
	public static List<Question> searchQuestion(String keyWords) {
		Connection conn = DBConnect.getConnect();
		List<Question> lists = new ArrayList<Question>();
		CallableStatement cstmt = null;
		ResultSet rs = null;
		String query = "{call searchQuestion(?) }";
		try {
			cstmt = conn.prepareCall(query);
			cstmt.setString(1, keyWords);
			rs = cstmt.executeQuery();
			while (rs.next())

			{
				Question question = new Question();
				question.setQuestionId(rs.getString(1));
				question.setQuestionDetail(rs.getString(2));
				question.setCategoryId(rs.getString(3));
				question.setQuestionType(rs.getInt(4));
				lists.add(question);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return lists;
	}
	public static void main(String[] args) {
		System.out.println(getAll().get(0).getQuestionType());
		System.out.println(searchQuestion("Q000").get(0).getCategoryId());
	}
}
