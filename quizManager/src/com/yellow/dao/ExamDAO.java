package com.yellow.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import com.yellow.model.Answers;
import com.yellow.model.Exam;

public class ExamDAO {

	public static List<Exam> getExam() {
		Connection conn = DBConnect.getConnect();
		List<Exam> lists = new ArrayList<Exam>();
		CallableStatement cstmt = null;
		ResultSet rs = null;
		String query = "{call getListExam }";
		try {
			cstmt = conn.prepareCall(query);
			rs = cstmt.executeQuery();
			while (rs.next())

			{
				
				Exam exam = new Exam();
				exam.setExamId(rs.getString(1));
				exam.setFullName(rs.getNString(2));
				exam.setEmail(rs.getString(3));
				exam.setPhoneNumber(rs.getString(4));
				exam.setExamDate(rs.getDate(5).toString());
				
                if(ExamDetailDAO.CalculatePoint(AnswersDAO.getAnswers(rs.getString(1)))>=15)
                {
                	exam.setExamResult("Passed");
                }
                else
                {
                	exam.setExamResult("Failed");
                }
				lists.add(exam);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return lists;
	}

	public static int result(String examId)
   {
	    int result=0;
	   List<Answers> lists = AnswersDAO.findResult(examId);
		for(int i=0; i < lists.size();i++)
		{    String ansSelect = lists.get(0).getAnswerSelected();
		     String ansRight = lists.get(0).getAnswerDetail();
			if(ansSelect.equals(ansRight))
			{
				result++;
			}
		}
	   return result;
   }

	public static List<Exam> searExam(String keyWords) {
		Connection conn = DBConnect.getConnect();
		List<Exam> lists = new ArrayList<Exam>();
		CallableStatement cstmt = null;
		ResultSet rs = null;
		String query = "{call searchExam(?) }";
		try {
			cstmt = conn.prepareCall(query);
			cstmt.setString(1, keyWords);
			rs = cstmt.executeQuery();
			while (rs.next())

			{
				Exam exam = new Exam();
				exam.setExamId(rs.getString(1));
				exam.setFullName(rs.getNString(2));
				exam.setEmail(rs.getString(3));
				exam.setPhoneNumber(rs.getString(4));
				exam.setExamDate(rs.getDate(5).toString());
				lists.add(exam);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return lists;
	}

	public static Exam findExamById(String id) throws SQLException {
		Connection conn = DBConnect.getConnect();
		Exam exam = new Exam();
		CallableStatement cstmt = null;
		ResultSet rs = null;
		String query = "{call  findById(?) }";
		try {
			cstmt = conn.prepareCall(query);
			cstmt.setString(1, id);
			rs = cstmt.executeQuery();
			while (rs.next())

			{

				exam.setExamId(rs.getString(1));
				exam.setFullName(rs.getNString(2));
				exam.setEmail(rs.getString(3));
				exam.setPhoneNumber(rs.getString(4));
				exam.setExamDate(rs.getDate(5).toString());
				exam.setCategoryName(rs.getString(7));
				 if(result(rs.getString(1))>=15)
	                {
	                	exam.setExamResult("Passed");
	                }
	                else
	                {
	                	exam.setExamResult("Failed");
	                }

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return exam;
	}

	public static int deleteExamByID(String examId) throws SQLException {
		int record = 0;
		Connection conn = DBConnect.getConnect();

		CallableStatement cstmt = null;

		String query = "{call  deleteExam(?) }";
		cstmt = conn.prepareCall(query);
		cstmt.setString(1, examId);
		record = cstmt.executeUpdate();
		return record;
	}
	public static String getExamId() throws SQLException {

        String examId = null;
        String query = "select top 1 examId from Exams order by examId desc";
        Connection con = DBConnect.getConnect();
        ResultSet rs = null;
        Statement stt = con.createStatement();
        rs = stt.executeQuery(query);
        while (rs.next()) {
            examId = rs.getString(1);
        }
        return examId;
    }


	public static String convertExamId() throws SQLException {
        String examId = getExamId();

        String converId = null;
        if (examId==null) {
            examId = "E0000001";
            return examId;
        }
        else {
            String convert = examId.substring(1, 8);
            int id = Integer.parseInt(convert);
            id++;
            converId = String.valueOf(id);
            while (converId.length() < 7) {
                converId = "0" + converId;
            }
            String examId2 = "E" + converId;
            return examId2;
        }

        
    }

	public static int insertInfo(String id, String fullName, String email, String phone, String categoryId) {
        int record = 0;

        Connection conn = DBConnect.getConnect();
        CallableStatement cstm = null;
        String query = "{call insertExam(?,?,?,?,?)}";
        try {
            cstm = conn.prepareCall(query);
            cstm.setString(1, id);
            cstm.setString(2, fullName);
            cstm.setString(3, email);
            cstm.setString(4, phone);
            cstm.setString(5, categoryId);
            record = cstm.executeUpdate();
        }
        catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return record;
    }
	public static void main(String[] args) throws SQLException {
//		System.out.println(convertExamId());
//		int i=insertInfo(convertExamId(), "tran van hieu", "a", "bcacba", "C0000001");
//		System.out.println(i);
	}
}



