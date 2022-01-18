package com.yellow.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.yellow.model.ExamDetail;
import com.yellow.model.Result;

public class ExamDetailDAO {
	public static void main(String[] args) {
		System.out.println(getEDId());
		;
	}

	public static String convertED(String edId) {
		int intedID = Integer.parseInt(edId.substring(2, edId.length()));

		// System.out.println(intedID);
		intedID++;
		// System.out.println(intedID);
		String stredID = String.valueOf(intedID);
		while (stredID.length() < 6) {
			stredID = "0" + stredID;
			// System.out.println(strUserId);
		}
		return "ED" + stredID;
	}

	public static String getEDId() {
		String query = "SELECT TOP 1 * FROM [dbo].[ExamDetails] ORDER BY [examDetailId] DESC";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				// System.out.println(rs.getString("examDetailId"));
				return convertED(rs.getString("examDetailId"));
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
		return "ED000001";
	}

	public static boolean InsertED(ExamDetail examD) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int countInsert = 0;
		String query = "INSERT INTO [dbo].[ExamDetails] " + "           ([examDetailId] "
				+ "           ,[answerSelected] " + "           ,[examId] " + "           ,[examDetailDeleteStatus] "
				+ "           ,[questionId]) " + "     VALUES " + "           (?" + "           ,?" + "           ,?"
				+ "           ,?" + "           ,?)";
		try {
			conn = DBConnect.getConnect();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, examD.getExamDetailId());
			pstmt.setString(2, examD.getAnswerSeleted());
			pstmt.setString(3, examD.getExamId());
			pstmt.setInt(4, examD.getExamDetailDeleteStatus());
			pstmt.setString(5, examD.getQuestionId());
			countInsert = pstmt.executeUpdate();
			if (countInsert == 1) {
				System.out.println("Insert ed thanh cong.");
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

	public static int CalculatePoint(List<Result> results) {
		int diem = 0;

		if (results.size() > 29) {
			for (int i = 0; i < 30; i++) {
				if (results.get(i).getAnswer().equals(results.get(i).getCorrect()))
					diem++;
			}
		}

		return diem;
	}

	public static int CalculatePointSQL(List<Result> results) {
		int diem = 0;
		if (results.size() > 29) {
			for (int i = 20; i < 30; i++) {
				if (results.get(i).getAnswer().equals(results.get(i).getCorrect()))
					diem++;
			}
		}

		return diem;
	}
}
