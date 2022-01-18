package com.yellow.controller;

import java.sql.SQLException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yellow.dao.AnswerDAO;
import com.yellow.dao.AnswersDAO;
import com.yellow.dao.CategoryDAO;
import com.yellow.dao.ExamDAO;
import com.yellow.dao.ExamDetailDAO;
import com.yellow.dao.QuestionDAO;
import com.yellow.dao.UserDAO;
import com.yellow.model.Answer;
import com.yellow.model.Categories;
import com.yellow.model.Exam;
import com.yellow.model.Question;
import com.yellow.model.Users;

@Controller
@RequestMapping("/admin")
public class AdminController {
	// Login
	@RequestMapping(value = "/login")
	public String login(HttpSession session) {
		if (!checkSession(session)) {
			return "redirect:/admin/question.htm";
		} else 
		return "admin/login";
	}

	public static boolean checkSession(HttpSession session) {
		if (session.getAttribute("user") == null) {
			return true;
		} else {
			return false;
		}
	}

	@RequestMapping(value = "/logincheck", method = RequestMethod.POST)
	public String checkLogin(@Validated Users users, HttpSession session, RedirectAttributes redirectAttributes) {
		Users u = UserDAO.getUserByName(users.userName);
		if (u != null) {
			if (u.getPassWord().equals(users.getPassWord())) {
				Users users2 = UserDAO.getUserByName(users.getUserName());
				session.setAttribute("user", users2);
				return "redirect:/admin/question.htm";
			}
		}
		redirectAttributes.addFlashAttribute("error", "<script>document.getElementsByClassName(\"errorShow\")[1].style.display = \"block\";document.getElementsByClassName(\"errorShow\")[1].style.height = \"170px\";</script>");
		return "redirect:/admin/login.htm";
	}

	@RequestMapping(value = "/logout")
	public String logout(HttpSession session) {
		session.setAttribute("user", null);
		return "redirect:/admin/login.htm";
	}

}
