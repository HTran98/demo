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

import com.yellow.dao.UserDAO;
import com.yellow.model.Users;

@Controller
public class UserController {
	
	public static boolean checkSession(HttpSession session) {
		if (session.getAttribute("user") == null) {
			return true;
		} else {
			return false;
		}
	}
	@RequestMapping(value = "admin/user")
	public String showUser(HttpServletRequest request,HttpSession session) {
		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		request.setAttribute("listUser", UserDAO.getAll());
		return "admin/userManage";
	}
	@RequestMapping(value = "admin/add-user")
	public String AddUser(HttpServletRequest request, HttpSession session) {
		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		request.setAttribute("userId", UserDAO.getUserId());
		return "admin/add-user";
	}

	@RequestMapping(value = "admin/update-user", params = { "userId" })
	public String UpdateUser(HttpServletRequest request, HttpSession session,
			@RequestParam(value = "userId") String userId) {
		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		request.setAttribute("user", UserDAO.getUser(userId));
		return "admin/update-user";

	}

	@RequestMapping(value = "admin/save-user", method = RequestMethod.POST, params = "add")
	private String saveUser(@Validated Users user, HttpSession session) {
		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		if (UserDAO.InsertUser(user)) {
			System.out.println("ok");
		}
		// TODO Auto-generated method stub
		return "redirect:/admin/user.htm";

	}
	@RequestMapping(value = "admin/save-user", method = RequestMethod.POST, params = "cancel")
	private String cancelUser(@Validated Users user, HttpSession session) {
		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		
		return "redirect:/admin/user.htm";

	}

	@RequestMapping(value = "admin/save-user", method = RequestMethod.POST, params = "update")
	private String update(@Validated Users user, HttpSession session) {

		if (UserDAO.EditUser(user)) {
			System.out.println("ok");
		}
		// TODO Auto-generated method stub
		return "redirect:/admin/user.htm";
	}

	@RequestMapping(value = "admin/save-user", method = RequestMethod.POST, params = "delete")
	private String deleteUser(@Validated Users user, HttpSession session) {

		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		if (UserDAO.DeleteUser(user.getUserId())) {
			System.out.println("ok");
		}
		// TODO Auto-generated method stub
		return "redirect:/admin/user.htm";
	}
	
	@RequestMapping(value = "admin/delete-user", method = RequestMethod.GET)
	private String deleteUser2(@Validated Users user, HttpSession session) {

		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		if (UserDAO.DeleteUser(user.getUserId())) {
			System.out.println("ok");
		}
		// TODO Auto-generated method stub
		return "redirect:/admin/user.htm";
	}
	@RequestMapping(value = "admin/user", method = RequestMethod.POST)
	public String userSearch(HttpSession session, Model model, @RequestParam("uKeyWords") String keyWords,
			HttpServletRequest request, HttpServletResponse reponse) {
		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		List<Users> lists = UserDAO.searchUser(keyWords);
		model.addAttribute("listUser", lists);

		return "admin/userManage";
	}
	
}
