package com.yellow.controller;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yellow.dao.AnswersDAO;
import com.yellow.dao.ExamDAO;
import com.yellow.dao.ExamDetailDAO;
import com.yellow.model.Exam;

@Controller
public class ExamController {
	@RequestMapping(value = "admin/exam")
	public String showExam(HttpServletRequest request,HttpSession session) {
		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		request.setAttribute("listExams", ExamDAO.getExam());
		return "admin/examManage";
	}
	public static boolean checkSession(HttpSession session) {
		if (session.getAttribute("user") == null) {
			return true;
		} else {
			return false;
		}
	}
	@RequestMapping(value = "admin/exam", method = RequestMethod.POST)
	public String examSearch(HttpSession session,Model model, @RequestParam("keyWords") String keyWords, HttpServletRequest request,
			HttpServletResponse reponse) {
		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		List<Exam> lists = ExamDAO.searExam(keyWords);

		request.setAttribute("listExams", lists);
		return "admin/examManage";
	}

	@RequestMapping(value = "admin/exam/Detail", params = { "examId" })
	public String examDetail(@RequestParam(value = "examId") String examId, HttpServletRequest request,
			HttpServletResponse reponse,HttpSession session) throws SQLException {
		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		request.setAttribute("examDetail", ExamDAO.findExamById(examId));
		request.setAttribute("listAnswers", AnswersDAO.getAnswers(examId));
		request.setAttribute("resultAll", ExamDetailDAO.CalculatePoint(AnswersDAO.getAnswers(examId)));
		request.setAttribute("resultSQL", ExamDetailDAO.CalculatePointSQL(AnswersDAO.getAnswers(examId)));
		
		return "admin/examDetail";
	}

	@RequestMapping(value = "admin/exam/delete", params = { "examId" })
	public String examDelete(@RequestParam(value = "examId") String examId, HttpServletRequest request,
			HttpServletResponse reponse,HttpSession session) {
		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		try {
			if (ExamDAO.deleteExamByID(examId) != 0) {
				request.setAttribute("mess", "Xóa thành công");
			} else {
				request.setAttribute("mess", "Exam Id không tồn tại");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:/admin/examManage.htm";
	}

}
