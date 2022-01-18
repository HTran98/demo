/**
 * hhqit97
 */
package com.yellow.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yellow.dao.CategoryDAO;
import com.yellow.dao.ExamDAO;
import com.yellow.dao.ExamDetailDAO;
import com.yellow.dao.TestDAO;
import com.yellow.model.Exam;
import com.yellow.model.ExamDetail;

/**
 * @author qhoang2
 *
 */
@Controller
public class HomeController {
	@RequestMapping(value = "test.htm")
	public String test(HttpServletRequest request, @Validated Exam exam, HttpSession session) {
		request.setAttribute("categoryName", CategoryDAO.getCategory(exam.getCategoryId()).getCategoryName());
		request.setAttribute("strListQuestion", TestDAO.toString(TestDAO.getListTest(exam.getCategoryId())));
		String id = "";
		try {
			id = ExamDAO.convertExamId();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int record = 0;
		if (id != "")
			record = ExamDAO.insertInfo(id, exam.getFullName(), exam.getEmail(), exam.getPhoneNumber(),
					exam.getCategoryId());
		if (record != 0) {
			request.setAttribute("examId", id);
			session.setAttribute("fullName", exam.getFullName());
			return "test";
		} else {
			return "redirect:/home.htm";
		}
	}

	@RequestMapping(value = "home.htm")
	public String home(HttpServletRequest request, HttpSession session) {
		request.setAttribute("listCategory", CategoryDAO.getListCategory());
		return "home";
	}

	@RequestMapping(value = "save-exam-detail.htm", method = RequestMethod.POST)
	public String saveExamDetail(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String examId = request.getParameter("examId");
		for (int i = 0; i < 30; i++) {
			String questionId = request.getParameter("questionId" + i);
			String questionType = request.getParameter("questionType" + i);
			//System.out.println(questionId);
			String answer = "";
			if (questionId != null) {
				if (questionType != null) {
					if (questionType.equals("0")) {
						answer = request.getParameter("answer" + i);
						if(answer!=null) answer+=", ";
					} else {
						String as = "";
						String[] checkboxNamesList = request.getParameterValues("answer" + i);
						for (int j = 0; j < checkboxNamesList.length; j++) {
							as += checkboxNamesList[j] + ", ";
						}
						answer = as;
					}
				}
				if (answer == null)
					answer = "";
				ExamDetail ed = new ExamDetail();
				ed.setExamDetailId(ExamDetailDAO.getEDId());
				ed.setAnswerSeleted(answer);
				ed.setExamDetailDeleteStatus(0);
				ed.setExamId(examId);
				ed.setQuestionId(questionId);
				ExamDetailDAO.InsertED(ed);
				System.out.println(request.getParameter("questionId"+i)+" chon "+answer);
			}
		}
		redirectAttributes.addFlashAttribute("date", request.getParameter("date"));
		redirectAttributes.addFlashAttribute("time", request.getParameter("time"));
		return "redirect:/success.htm";
	}

	@RequestMapping(value = "success")
	public String success() {
		return "test-end";
	}

}
