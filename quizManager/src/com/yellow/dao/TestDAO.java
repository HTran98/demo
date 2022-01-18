/**
 * hhqit97
 */
package com.yellow.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.yellow.model.Answer;
import com.yellow.model.Test;

/**
 * @author qhoang2
 *
 */
public class TestDAO {
	
	public static List<Test> getListTest(String categoryId){
		List<Test> list = new ArrayList<Test>();
		Test question = null;
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
				question = new Test();
				question.setQuestionId(rs.getString("questionId"));
				question.setQuestionDetail(rs.getString("questionDetail"));
				question.setQuestionType(rs.getInt("questionType"));
				question.setQuestionStatus(rs.getInt("questionStatus"));
				question.setQuestionDeleteStatus(rs.getInt("questionDeleteStatus"));
				question.setCategoryId(rs.getString("categoryId"));
				question.setListAnwser(AnswerDAO.getListAnswer(question.getQuestionId()));
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
		query = "SELECT TOP 10 [questionId] " + 
				"      ,[questionDetail] " + 
				"      ,[questionType] " + 
				"      ,[questionStatus] " + 
				"      ,[questionDeleteStatus] " + 
				"      ,[categoryId] " + 
				"  FROM [dbo].[Questions] WHERE [categoryId] = ? ORDER BY NEWID()";
		conn = null;
		pstmt = null;
		rs = null;
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, "C0000005");
			rs = pstmt.executeQuery();
			while(rs.next()) {
				question = new Test();
				question.setQuestionId(rs.getString("questionId"));
				question.setQuestionDetail(rs.getString("questionDetail"));
				question.setQuestionType(rs.getInt("questionType"));
				question.setQuestionStatus(rs.getInt("questionStatus"));
				question.setQuestionDeleteStatus(rs.getInt("questionDeleteStatus"));
				question.setCategoryId(rs.getString("categoryId"));
				question.setListAnwser(AnswerDAO.getListAnswer(question.getQuestionId()));
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
	public static String convertStr(String str) {
		str = str.toString().replaceAll("\"","&quot;");
		str = str.toString().replaceAll("Åf","&quot;");
		str = str.toString().replaceAll("","");
		return str;
	}
	public static String toString(List<Test> list) {
		StringBuilder strList  = new StringBuilder();
		//System.out.println(list.size());
		strList.append("[");
		String[] answerArr = { "A", "B", "C", "D", "E", "F", "G", "H",
							"I", "K", "L", "M" };
		for(int j=0;j<list.size();j++) {
			Test test = list.get(j);
			strList.append("{ ");
			strList.append(" categoryName:\""+convertStr(CategoryDAO.getCategory(test.getCategoryId()).getCategoryName())+"\", ");
			strList.append(" questionId:\""+test.getQuestionId()+"\", ");
			strList.append(" questionDetail:\""+convertStr(test.getQuestionDetail())+"\", ");
			strList.append(" questionType:\""+test.getQuestionType()+"\", ");
			strList.append(" answers: {");
			int dem = 0;
			for(int i=0;i<test.getListAnwser().size();i++) {
				Answer answer = test.getListAnwser().get(i);
				if(i<test.getListAnwser().size()-1) strList.append(answerArr[dem]+" : \""+convertStr(answer.answerDetail)+"\", ");
				else strList.append(answerArr[dem]+" : \""+convertStr(answer.answerDetail)+"\"");
				dem++;
			}
			strList.append(" }");
			if(j<list.size()-1) strList.append(" },");
			else strList.append(" }");
		}
		strList.append("]");
		
		return strList.toString();
	}
}
