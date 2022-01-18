package com.yellow.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.yellow.model.Answers;
import com.yellow.model.Result;

public class AnswersDAO {
	public static void main(String[] args) {
//		try {
//			List<Result> results = getAnswers("E0000021");
//			for (Result result : results) {
//				System.out.println(result.getAnswer()+" "+result.getCorrect());
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	public static List<Result> getAnswers(String examId) throws SQLException {
		Connection conn = DBConnect.getConnect();
		CallableStatement cstmt = null;
		ResultSet rs = null;
		String query = "{call  getResult(?) }";
		
		cstmt = conn.prepareCall(query);
		cstmt.setString(1, examId);
		rs = cstmt.executeQuery();
		List<Result> results = new ArrayList<Result>();
		Result result = null;
		while (rs.next()) {
			result = new Result();
			String correct = rs.getString("answerSelected");
			result.setQuestionId(rs.getString("questionId"));
			String answer = "";
			PreparedStatement pstmt = null;
			ResultSet reQues = null;
			String sql = "select * from Answers where questionId = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, result.getQuestionId());
			reQues = pstmt.executeQuery();
			String[] answerArr = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "K", "L", "M" };
			int dem = 0;
			while (reQues.next()) {
				if (reQues.getInt("answerStatus") == 1) {
					answer += answerArr[dem] + ", ";
				}
				dem++;
			}
			
			result.setCorrect(correct);
			result.setAnswer(answer);
			
			results.add(result);
		}
		return results;
	}

	public static int togetherResult(String examId) throws SQLException {
		int dem = 0;
		Connection conn = DBConnect.getConnect();

		CallableStatement cstmt = null;
		ResultSet rs = null;
		String query = "{call  getAnswers(?) }";
		cstmt = conn.prepareCall(query);
		cstmt.setString(1, examId);
		rs = cstmt.executeQuery();

		while (rs.next()) {

			if (resultAnswer(rs.getString(3), rs.getString(4)) != 0) {
				dem++;

			} else {

			}

		}
		return dem;
	}

	public static int togetherResultSql(String examId) throws SQLException {
		int dem = 0;
		Connection conn = DBConnect.getConnect();

		CallableStatement cstmt = null;
		ResultSet rs = null;
		String query = "{call  getAnswersSQL(?) }";
		cstmt = conn.prepareCall(query);
		cstmt.setString(1, examId);
		rs = cstmt.executeQuery();

		while (rs.next()) {

			if (resultAnswerSql(rs.getString(3)) != 0) {
				dem++;

			}

		}
		return dem;
	}

	public static List<Answers> getAnswersSQL(String examId) throws SQLException {
		List<Answers> list = new ArrayList<Answers>();
		Connection conn = DBConnect.getConnect();

		CallableStatement cstmt = null;
		ResultSet rs = null;
		String query = "{call  getAnswersSQL(?) }";
		cstmt = conn.prepareCall(query);
		cstmt.setString(1, examId);
		rs = cstmt.executeQuery();
		while (rs.next()) {
			Answers answers = new Answers();
			answers.setAnswerSelected(rs.getString(1));
			answers.setAnswerDetail(rs.getString(2));
			if (resultAnswerSql(rs.getString(3)) != 0) {
				answers.setAnswerResult("Passed");
			} else {
				answers.setAnswerResult("Failed");
			}

			list.add(answers);
		}
		return list;
	}

	public static List<Answers> findResult(String examId) {
		List<Answers> list = new ArrayList<Answers>();
		Answers answers = new Answers();
		Connection con = DBConnect.getConnect();
		CallableStatement cstmt = null;
		ResultSet rs = null;
		String query = "{call examResult(?)}";
		try {
			cstmt = con.prepareCall(query);

			cstmt.setString(1, examId);
			rs = cstmt.executeQuery();
			while (rs.next()) {
				answers.setAnswerDetail(rs.getString(1));
				answers.setAnswerSelected(rs.getString(2));
				list.add(answers);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	public static Answers findResultDetail(String examId, String questionId) {

		Answers answers = new Answers();
		Connection con = DBConnect.getConnect();
		CallableStatement cstmt = null;
		ResultSet rs = null;
		String query = "{call examResultDetail(?,?)}";
		try {
			cstmt = con.prepareCall(query);

			cstmt.setString(1, examId);
			cstmt.setString(2, questionId);
			rs = cstmt.executeQuery();
			while (rs.next()) {
				answers.setAnswerDetail(rs.getString(1));
				answers.setAnswerSelected(rs.getString(2));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return answers;
	}

	public static Answers findResultSql(String examId) {

		Answers answers = new Answers();
		Connection con = DBConnect.getConnect();
		CallableStatement cstmt = null;
		ResultSet rs = null;
		String query = "{call examResultDetailSql(?)}";
		try {
			cstmt = con.prepareCall(query);

			cstmt.setString(1, examId);
			rs = cstmt.executeQuery();
			while (rs.next()) {
				answers.setAnswerDetail(rs.getString(1));
				answers.setAnswerSelected(rs.getString(2));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return answers;
	}

	public static int resultAnswer(String examId, String questionId) {
		int result = 0;
		Answers answers = findResultDetail(examId, questionId);
		String ansSelect = answers.getAnswerSelected();
		String ansRight = answers.getAnswerDetail();
		if (ansSelect.equals(ansRight)) {
			result++;
		}
		return result;
	}

	public static int resultAnswerSql(String examId) {
		int result = 0;
		Answers answers = findResultSql(examId);
		String ansSelect = answers.getAnswerSelected();
		String ansRight = answers.getAnswerDetail();
		if (ansSelect.equals(ansRight)) {
			result++;
		}
		return result;
	}

}
