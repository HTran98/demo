package com.yellow.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.yellow.dao.AnswerDAO;
import com.yellow.dao.CategoryDAO;
import com.yellow.dao.QuestionDAO;
import com.yellow.model.Answer;
import com.yellow.model.Question;

@Controller
public class QuestionController {
	public static boolean checkSession(HttpSession session) {
		if (session.getAttribute("user") == null) {
			return true;
		} else {
			return false;
		}
	}
	@RequestMapping(value = "admin/delete-question", method = RequestMethod.GET)
	private String deleteQuetion2(@Validated Question question, HttpSession session) {

		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		if (QuestionDAO.DeleteQuestion(question.getQuestionId())) {
			System.out.println("ok");
		}
		// TODO Auto-generated method stub
		return "redirect:/admin/question.htm";
	}
	@RequestMapping(value = "admin/save-question", method = RequestMethod.POST, params = "delete")
	private String deleteQuetion(@Validated Question question, HttpSession session) {

		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		if (QuestionDAO.DeleteQuestion(question.getQuestionId())) {
			System.out.println("ok");
		}
		// TODO Auto-generated method stub
		return "redirect:/admin/question.htm";
	}
	@RequestMapping(value = "admin/save-question", method = RequestMethod.POST, params = "cancel")
	private String cancelQuestion(@Validated Question question, HttpSession session) {

		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		// TODO Auto-generated method stub
		return "redirect:/admin/question.htm";
	}
	@RequestMapping(value = "admin/add-question")
	public String AddQuestion(HttpServletRequest request, HttpSession session) {
		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		request.setAttribute("questionId", QuestionDAO.getQuestionId());
		request.setAttribute("listCategory", CategoryDAO.getListCategory());
		return "admin/add-question";
	}

	
	@RequestMapping(value = "admin/question")
	public String showQuestion(HttpServletRequest request, HttpSession session) {
		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		request.setAttribute("listQuestion", QuestionDAO.getAll());
		return "admin/questionManage";

	}

	@RequestMapping(value = "admin/question", method = RequestMethod.POST)
	public String questionSearch(HttpSession session, Model model, @RequestParam("qKeyWords") String keyWords,
			HttpServletRequest request, HttpServletResponse reponse) {
		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		List<Question> lists = QuestionDAO.searchQuestion(keyWords);

		model.addAttribute("listQuestion", lists);

		return "admin/questionManage";
	}
	@RequestMapping(value = "admin/update-question", params = { "questionId" })
	public String UpdateQuestion(HttpServletRequest request,HttpSession session, @RequestParam(value = "questionId") String questionId) {
		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		request.setAttribute("question", QuestionDAO.getQuestion(questionId));
		request.setAttribute("listCategory", CategoryDAO.getListCategory());
		request.setAttribute("listAnswer", AnswerDAO.getListAnswer(questionId));
		return "admin/update-question";
	}

	@RequestMapping(value = "admin/save-question", method = RequestMethod.POST, params = "add")
	private String saveQuestion(HttpServletRequest request,HttpSession session, @Validated Question question) {
		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		String questionId = QuestionDAO.InsertQuestion(question);
		if (questionId != null) {
			String answer1 = request.getParameter("answer1");
			String answer2 = request.getParameter("answer2");
			String answer3 = request.getParameter("answer3");
			String answer4 = request.getParameter("answer4");
			int answerStatus1 = Integer.valueOf(request.getParameter("statusAnswer1"));
			int answerStatus2 = Integer.valueOf(request.getParameter("statusAnswer2"));
			int answerStatus3 = Integer.valueOf(request.getParameter("statusAnswer3"));
			int answerStatus4 = Integer.valueOf(request.getParameter("statusAnswer4"));
			Answer a = new Answer();
			if (!answer1.equals("")) {
				a.setAnswerId(AnswerDAO.getAnswerId());
				a.setAnswerDetail(answer1);
				a.setAnswerStatus(answerStatus1);
				a.setAnswerDeleteStatus(0);
				a.setQuestionId(questionId);
				AnswerDAO.InsertAnswer(a);
			}
			if (!answer2.equals("")) {
				a.setAnswerId(AnswerDAO.getAnswerId());
				a.setAnswerDetail(answer2);
				a.setAnswerStatus(answerStatus2);
				a.setAnswerDeleteStatus(0);
				a.setQuestionId(questionId);
				AnswerDAO.InsertAnswer(a);
			}
			if (!answer3.equals("")) {
				a.setAnswerId(AnswerDAO.getAnswerId());
				a.setAnswerDetail(answer3);
				a.setAnswerStatus(answerStatus3);
				a.setAnswerDeleteStatus(0);
				a.setQuestionId(questionId);
				AnswerDAO.InsertAnswer(a);
			}
			if (!answer4.equals("")) {
				a.setAnswerId(AnswerDAO.getAnswerId());
				a.setAnswerDetail(answer4);
				a.setAnswerStatus(answerStatus4);
				a.setAnswerDeleteStatus(0);
				a.setQuestionId(questionId);
				AnswerDAO.InsertAnswer(a);
			}

			System.out.println("ok");
		}
		// TODO Auto-generated method stub
		return "redirect:/admin/question.htm";
	}

	@RequestMapping(value = "admin/save-question", method = RequestMethod.POST, params = "update")
	private String saveQuestion2(@Validated Question question,HttpSession session, HttpServletRequest request) {
		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		if (QuestionDAO.EditQuestion(question)) {
			String answer1 = request.getParameter("answer1");
			String answer2 = request.getParameter("answer2");
			String answer3 = request.getParameter("answer3");
			String answer4 = request.getParameter("answer4");
			String answerId1 = request.getParameter("answerId1");
			String answerId2 = request.getParameter("answerId2");
			String answerId3 = request.getParameter("answerId3");
			String answerId4 = request.getParameter("answerId4");
			int answerStatus1 = Integer.valueOf(request.getParameter("statusAnswer1"));
			int answerStatus2 = Integer.valueOf(request.getParameter("statusAnswer2"));
			int answerStatus3 = Integer.valueOf(request.getParameter("statusAnswer3"));
			int answerStatus4 = Integer.valueOf(request.getParameter("statusAnswer4"));
			Answer a = new Answer();
			if (!answer1.equals("")) {
				a.setAnswerId(answerId1);
				a.setAnswerDetail(answer1);
				a.setAnswerStatus(answerStatus1);
				a.setAnswerDeleteStatus(0);
				a.setQuestionId(question.getQuestionId());
				if (!AnswerDAO.EditAnswer(a)) {
					a.setAnswerId(AnswerDAO.getAnswerId());
					AnswerDAO.InsertAnswer(a);
				}
			} else {
				if (AnswerDAO.DeleteAnswer(answerId1)) {
					System.out.println("Delete answer thanh cong.");
				}
			}
			if (!answer2.equals("")) {
				a.setAnswerId(answerId2);
				a.setAnswerDetail(answer2);
				a.setAnswerStatus(answerStatus2);
				a.setAnswerDeleteStatus(0);
				a.setQuestionId(question.getQuestionId());
				if (!AnswerDAO.EditAnswer(a)) {
					a.setAnswerId(AnswerDAO.getAnswerId());
					AnswerDAO.InsertAnswer(a);
				}
			} else {
				if (AnswerDAO.DeleteAnswer(answerId2)) {
					System.out.println("Delete answer thanh cong.");
				}
			}
			if (!answer3.equals("")) {
				a.setAnswerId(answerId3);
				a.setAnswerDetail(answer3);
				a.setAnswerStatus(answerStatus3);
				a.setAnswerDeleteStatus(0);
				a.setQuestionId(question.getQuestionId());
				if (!AnswerDAO.EditAnswer(a)) {
					a.setAnswerId(AnswerDAO.getAnswerId());
					AnswerDAO.InsertAnswer(a);
				}
			} else {
				if (AnswerDAO.DeleteAnswer(answerId3)) {
					System.out.println("Delete answer thanh cong.");
				}
			}
			if (!answer4.equals("")) {
				a.setAnswerId(answerId4);
				a.setAnswerDetail(answer4);
				a.setAnswerStatus(answerStatus4);
				a.setAnswerDeleteStatus(0);
				a.setQuestionId(question.getQuestionId());
				if (!AnswerDAO.EditAnswer(a)) {
					a.setAnswerId(AnswerDAO.getAnswerId());
					AnswerDAO.InsertAnswer(a);
				}
			} else {
				if (AnswerDAO.DeleteAnswer(answerId4)) {
					System.out.println("Delete answer thanh cong.");
				}
			}

			System.out.println("ok");
		}
		// TODO Auto-generated method stub
		return "redirect:/admin/question.htm";
	}
}
