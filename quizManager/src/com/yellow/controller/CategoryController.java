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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yellow.dao.CategoryDAO;
import com.yellow.model.Categories;

@Controller
public class CategoryController {
	public static boolean checkSession(HttpSession session) {
		if (session.getAttribute("user") == null) {
			return true;
		} else {
			return false;
		}
	}
	@RequestMapping(value = "admin/category")
	public String showCategory(HttpSession session,HttpServletRequest request) {
		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		request.setAttribute("listCategoy", CategoryDAO.getListCategory());
		return "admin/categoryManage";
	}
	@RequestMapping(value = "admin/add-category")
	public String AddCatogory(HttpServletRequest request, HttpSession session) {
		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		request.setAttribute("categoryId", CategoryDAO.getCategoryId());
		return "admin/add-category";
	}

	@RequestMapping(value = "admin/update-category", params = { "categoryId" })
	public String UpdateCatogory(HttpServletRequest request, HttpSession session,
			@RequestParam(value = "categoryId") String categoryId) {
		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		request.setAttribute("category", CategoryDAO.getCategory(categoryId));
		return "admin/update-category";
	}

	@RequestMapping(value = "admin/save-category", method = RequestMethod.POST, params = "add")
	private String addCatogory(@Validated Categories category, HttpSession session,RedirectAttributes redirectAttributes) {
		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		if (CategoryDAO.InsertCategory(category)) {
			System.out.println("ok");
		} else {
			redirectAttributes.addFlashAttribute("error", "<script>document.getElementsByClassName(\"errorDialog\")[1].style.display = \"block\";</script>");
			return "redirect:/admin/add-category.htm";
		}
		// TODO Auto-generated method stub
		return "redirect:/admin/category.htm";
	}
	@RequestMapping(value = "admin/save-category", method = RequestMethod.POST, params = "cancel")
	private String cancelCatogory(@Validated Categories category, HttpSession session,RedirectAttributes redirectAttributes) {
		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		
			
			
		// TODO Auto-generated method stub
		return "redirect:/admin/category.htm";
	}
	@RequestMapping(value = "admin/save-category", method = RequestMethod.POST, params = "update")
	private String updateCatogory(@Validated Categories category, HttpSession session,RedirectAttributes redirectAttributes) {
		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		if (CategoryDAO.EditCategory(category)) {
			System.out.println("ok");
		}  else {
			redirectAttributes.addFlashAttribute("error", "<script>document.getElementsByClassName(\"errorDialog\")[1].style.display = \"block\";</script>");
			return "redirect:/admin/add-category.htm";
		}
		// TODO Auto-generated method stub
		return "redirect:/admin/category.htm";
	}

	@RequestMapping(value = "admin/save-category", params = "delete")
	private String deleteCatogory(@Validated Categories category, HttpSession session) {
		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		if (CategoryDAO.DeleteCategory(category.getCategoryId())) {
			System.out.println("ok");
		}
		// TODO Auto-generated method stub
		return "redirect:/admin/category.htm";
	}
	@RequestMapping(value = "admin/delete-category")
	private String deleteCatogory2(@Validated Categories category, HttpSession session) {
		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		if (CategoryDAO.DeleteCategory(category.getCategoryId())) {
			System.out.println("ok");
		}
		// TODO Auto-generated method stub
		return "redirect:/admin/category.htm";
	}
	@RequestMapping(value = "admin/category", method = RequestMethod.POST)
	public String categorySearch(HttpSession session, Model model, @RequestParam("cKeyWords") String keyWords,
			HttpServletRequest request, HttpServletResponse reponse) {
		if (checkSession(session)) {
			return "redirect:/admin/login.htm";
		}
		List<Categories> lists = CategoryDAO.searchCategory(keyWords);

		model.addAttribute("listCategoy", lists);

		return "admin/categoryManage";
	}
}
