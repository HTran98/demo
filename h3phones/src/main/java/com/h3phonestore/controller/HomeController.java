package com.h3phonestore.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import com.h3phonestore.dto.BrandDto;
import com.h3phonestore.dto.CheckOutDto;
import com.h3phonestore.dto.CustomerContactDto;
import com.h3phonestore.dto.DataFilteDto;
import com.h3phonestore.dto.HeadPhoneDetailDto;
import com.h3phonestore.dto.InternalMemoryDto;
import com.h3phonestore.dto.InvoiceDto;
import com.h3phonestore.dto.ProductDetailDto;
import com.h3phonestore.dto.ProductOrderDto;
import com.h3phonestore.dto.RamDto;
import com.h3phonestore.dto.SearchProductDetailDto;
import com.h3phonestore.dto.UserDto;
import com.h3phonestore.entity.UserDetailCustom;
import com.h3phonestore.service.BrandService;
import com.h3phonestore.service.CustomerContactService;
import com.h3phonestore.service.HeadPhoneService;
import com.h3phonestore.service.InvoiceService;
import com.h3phonestore.service.ProductDetailService;
import com.h3phonestore.service.ProductOrderService;
import com.h3phonestore.service.UserService;
import com.h3phonestore.serviceImpl.OTPService;

@Controller
@RequestMapping("/")
public class HomeController {
	@Autowired
	private UserService userService;
	@Autowired
	ProductDetailService productDetailService;
	@Autowired
	BrandService brandService;
	@Autowired
	ProductOrderService productOrderService;
	@Autowired
	InvoiceService invoiceService;
	@Autowired
	public JavaMailSender emailSender;
	@Autowired
	CustomerContactService contactService;
	@Autowired
	HeadPhoneService headPhoneService;

	@Autowired
	public OTPService otpService;

	@GetMapping("h3phone")
	public String homePage(@AuthenticationPrincipal UserDetailCustom userInfo, Model model, HttpServletRequest request,
			RedirectAttributes redirect) {

		model.addAttribute("userInfo", userInfo);
		request.getSession().setAttribute("productDetailList", null);
		request.getSession().setAttribute("searchproductList", null);

		if (model.asMap().get("success") != null)
			redirect.addFlashAttribute("success", model.asMap().get("success").toString());
		if (model.asMap().get("fail") != null)
			redirect.addFlashAttribute("fail", model.asMap().get("fail").toString());
		return "redirect:/h3phone/page/1";
	}

	@GetMapping("/h3phone/page/{pageNumber}")
	public String showProductPage(@AuthenticationPrincipal UserDetailCustom userInfo, HttpServletRequest request,
			@PathVariable int pageNumber, Model model) {
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productDetailList");
		int pagesize = 9;
		List<ProductDetailDto> list = productDetailService.getAllByProduct();
		List<BrandDto> listBrandDto = brandService.getAll();

		SearchProductDetailDto searchDto = new SearchProductDetailDto();
		ArrayList<BrandDto> brands = new ArrayList<BrandDto>();
		ArrayList<InternalMemoryDto> listInternalMemory = new ArrayList<InternalMemoryDto>();

		DataFilteDto dataFilter = productDetailService.getDataFilter();
		InternalMemoryDto internal;

		for (String internalMemory : dataFilter.getListInternalMemory()) {
			internal = new InternalMemoryDto();
			internal.setInternalMemory(internalMemory);
			listInternalMemory.add(internal);
		}

		searchDto.setListInternalMemory(listInternalMemory);
		List<ProductDetailDto> listProductSpecial = productDetailService.getProductSpecial();

		for (BrandDto brand : listBrandDto) {

			brands.add(brand);
		}
		searchDto.setListBrand(brands);

		if (pages == null) {
			pages = new PagedListHolder<>(list);
			pages.setPageSize(pagesize);
		} else {
			SearchProductDetailDto search = (SearchProductDetailDto) request.getSession()
					.getAttribute("searchBrandList");
			if (search != null) {
				searchDto = search;
			}
			final int goToPage = pageNumber - 1;
			if (goToPage <= pages.getPageCount() && goToPage >= 0) {
				pages.setPage(goToPage);
			}
		}
		request.getSession().setAttribute("productDetailList", pages);
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/h3phone/page/";
		
		int quantityOrder = 0;
		if(userInfo != null)
		{
			List<ProductOrderDto> listProductOrder = productOrderService.getOrederByUserName(userInfo.getUsername());
			quantityOrder = listProductOrder.size();
		}
		model.addAttribute("quantityOrder", quantityOrder);
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("listProductDetail", pages);
		model.addAttribute("checked", false);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("searchDto", searchDto);
		model.addAttribute("searchInterDto", searchDto);
		model.addAttribute("listProductSpecial", listProductSpecial);
        
		return "shop";
	}

	@GetMapping("/h3phone/search/{pageNumber}")
	public String searchProductPage(@AuthenticationPrincipal UserDetailCustom userInfo, HttpServletRequest request,
			@PathVariable int pageNumber, Model model, @RequestParam("keyWords") String keyWords) {
		if (StringUtils.isEmptyOrWhitespace(keyWords)) {
			return "redirect:/h3phone";
		}
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productDetailList");
		PagedListHolder<?> pagesHeadPhone = (PagedListHolder<?>) request.getSession().getAttribute("headPhoneList");
		int pagesize = 9;
		List<ProductDetailDto> list = productDetailService.searchProductDetail(keyWords);
		List<HeadPhoneDetailDto> listHeadPhoneDetailDtos = headPhoneService.searchHeadPhone(keyWords);
		List<ProductDetailDto> listProductSpecial = productDetailService.getProductSpecial();
		List<BrandDto> listBrandDto = brandService.getAll();

		SearchProductDetailDto searchDto = new SearchProductDetailDto();
		ArrayList<BrandDto> brands = new ArrayList<BrandDto>();

		ArrayList<InternalMemoryDto> listInternalMemory = new ArrayList<InternalMemoryDto>();
		ArrayList<RamDto> listRam = new ArrayList<RamDto>();
		DataFilteDto dataFilter = productDetailService.getDataFilter();
		InternalMemoryDto internal;
		RamDto ramDto;
		for (String internalMemory : dataFilter.getListInternalMemory()) {
			internal = new InternalMemoryDto();
			internal.setInternalMemory(internalMemory);
			listInternalMemory.add(internal);
		}
		for (String ram : dataFilter.getListRam()) {
			ramDto = new RamDto();
			ramDto.setRam(ram);
			listRam.add(ramDto);
		}
		searchDto.setListInternalMemory(listInternalMemory);

		for (BrandDto brand : listBrandDto) {
			brands.add(brand);
		}
		searchDto.setListBrand(brands);
		if (listHeadPhoneDetailDtos.size() > 0) {
			pagesHeadPhone = new PagedListHolder<>(listHeadPhoneDetailDtos);
			pagesHeadPhone.setPageSize(pagesize);

			final int goToPage = pageNumber - 1;
			if (goToPage <= pagesHeadPhone.getPageCount() && goToPage >= 0) {
				pagesHeadPhone.setPage(goToPage);
			}
			request.getSession().setAttribute("headPhoneList", pagesHeadPhone);
			request.getSession().setAttribute("searchBrandList", searchDto);
			int current = pagesHeadPhone.getPage() + 1;
			int begin = Math.max(1, current - listHeadPhoneDetailDtos.size());
			int end = Math.min(begin + 5, pagesHeadPhone.getPageCount());
			int totalPageCount = pagesHeadPhone.getPageCount();
			String baseUrl = "/h3phone/headPhone/page/";
			int quantityOrder = 0;
			if(userInfo != null)
			{
				List<ProductOrderDto> listProductOrder = productOrderService.getOrederByUserName(userInfo.getUsername());
				quantityOrder = listProductOrder.size();
			}
			model.addAttribute("quantityOrder", quantityOrder);
			model.addAttribute("beginIndex", begin);
			model.addAttribute("endIndex", end);
			model.addAttribute("currentIndex", current);
			model.addAttribute("totalPageCount", totalPageCount);
			model.addAttribute("baseUrl", baseUrl);
			model.addAttribute("listHeadPhone", pagesHeadPhone);
			model.addAttribute("checked", false);
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("searchDto", searchDto);

			model.addAttribute("listProductSpecial", listProductSpecial);

			return "shopHeadPhone";
		} else {
			pages = new PagedListHolder<>(list);
			pages.setPageSize(pagesize);

			final int goToPage = pageNumber - 1;
			if (goToPage <= pages.getPageCount() && goToPage >= 0) {
				pages.setPage(goToPage);
			}
			request.getSession().setAttribute("productDetailList", pages);

			int current = pages.getPage() + 1;
			int begin = Math.max(1, current - list.size());
			int end = Math.min(begin + 5, pages.getPageCount());
			int totalPageCount = pages.getPageCount();
			String baseUrl = "/h3phone/page/";
			int quantityOrder = 0;
			if(userInfo != null)
			{
				List<ProductOrderDto> listProductOrder = productOrderService.getOrederByUserName(userInfo.getUsername());
				quantityOrder = listProductOrder.size();
			}
			model.addAttribute("quantityOrder", quantityOrder);
			model.addAttribute("beginIndex", begin);
			model.addAttribute("endIndex", end);
			model.addAttribute("currentIndex", current);
			model.addAttribute("totalPageCount", totalPageCount);
			model.addAttribute("baseUrl", baseUrl);
			model.addAttribute("listProductDetail", pages);
			model.addAttribute("checked", false);
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("searchDto", searchDto);
			model.addAttribute("searchInterDto", searchDto);
			model.addAttribute("listProductSpecial", listProductSpecial);

			return "shop";
		}
	}

	@GetMapping("/h3phone/searchBrand/{pageNumber}")
	public String searchBrandPage(@AuthenticationPrincipal UserDetailCustom userInfo, HttpServletRequest request,
			@PathVariable int pageNumber, Model model, @Validated SearchProductDetailDto searchDto) {
		if (searchDto.getListBrand() == null) {
			return "redirect:/h3phone";
		}
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productDetailList");
		int pagesize = 9;
		List<String> brandNameList = new ArrayList<>();
		for (BrandDto brand : searchDto.getListBrand()) {
			if (brand.isCheckBrand()) {
				brandNameList.add(brand.getBrandName());
			}
		}

		ArrayList<InternalMemoryDto> listInternalMemory = new ArrayList<InternalMemoryDto>();
		ArrayList<RamDto> listRam = new ArrayList<RamDto>();
		DataFilteDto dataFilter = productDetailService.getDataFilter();
		InternalMemoryDto internal;
		RamDto ramDto;
		for (String internalMemory : dataFilter.getListInternalMemory()) {
			internal = new InternalMemoryDto();
			internal.setInternalMemory(internalMemory);
			listInternalMemory.add(internal);
		}
		for (String ram : dataFilter.getListRam()) {
			ramDto = new RamDto();
			ramDto.setRam(ram);
			listRam.add(ramDto);
		}
		searchDto.setListInternalMemory(listInternalMemory);
		List<ProductDetailDto> listProductSpecial = productDetailService.getProductSpecial();
		List<ProductDetailDto> list;
		if (brandNameList.size() > 0) {
			list = productDetailService.getProductByBrand(brandNameList);
		} else {
			list = productDetailService.getAllByProduct();
		}

		pages = new PagedListHolder<>(list);
		pages.setPageSize(pagesize);

		final int goToPage = pageNumber - 1;
		if (goToPage <= pages.getPageCount() && goToPage >= 0) {
			pages.setPage(goToPage);
		}
		request.getSession().setAttribute("productDetailList", pages);
		request.getSession().setAttribute("searchBrandList", searchDto);
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/h3phone/page/";
		int quantityOrder = 0;
		if(userInfo != null)
		{
			List<ProductOrderDto> listProductOrder = productOrderService.getOrederByUserName(userInfo.getUsername());
			quantityOrder = listProductOrder.size();
		}
		model.addAttribute("quantityOrder", quantityOrder);
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("listProductDetail", pages);
		model.addAttribute("checked", false);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("searchDto", searchDto);
		model.addAttribute("searchInterDto", searchDto);
		model.addAttribute("listProductSpecial", listProductSpecial);

		return "shop";
	}

	@GetMapping("/h3phone/searchPrices/{pageNumber}")
	public String searchPricesPage(@AuthenticationPrincipal UserDetailCustom userInfo, HttpServletRequest request,
			@PathVariable int pageNumber, Model model, @RequestParam("minPrices") int minPrices,
			@RequestParam("maxPrices") int maxPrices) {
		if (maxPrices <= 0 || maxPrices <= minPrices) {
			return "redirect:/h3phone";
		}
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productDetailList");
		int pagesize = 9;

		List<BrandDto> listBrandDto = brandService.getAll();
		List<ProductDetailDto> listProductSpecial = productDetailService.getProductSpecial();
		SearchProductDetailDto searchDto = new SearchProductDetailDto();
		ArrayList<BrandDto> brands = new ArrayList<BrandDto>();

		ArrayList<InternalMemoryDto> listInternalMemory = new ArrayList<InternalMemoryDto>();
		ArrayList<RamDto> listRam = new ArrayList<RamDto>();
		DataFilteDto dataFilter = productDetailService.getDataFilter();
		InternalMemoryDto internal;
		RamDto ramDto;
		for (String internalMemory : dataFilter.getListInternalMemory()) {
			internal = new InternalMemoryDto();
			internal.setInternalMemory(internalMemory);
			listInternalMemory.add(internal);
		}
		for (String ram : dataFilter.getListRam()) {
			ramDto = new RamDto();
			ramDto.setRam(ram);
			listRam.add(ramDto);
		}
		searchDto.setListInternalMemory(listInternalMemory);

		for (BrandDto brand : listBrandDto) {
			brands.add(brand);
		}
		searchDto.setListBrand(brands);

		List<ProductDetailDto> list = productDetailService.getProductByPrice(minPrices, maxPrices);

		pages = new PagedListHolder<>(list);
		pages.setPageSize(pagesize);

		final int goToPage = pageNumber - 1;
		if (goToPage <= pages.getPageCount() && goToPage >= 0) {
			pages.setPage(goToPage);
		}
		request.getSession().setAttribute("productDetailList", pages);

		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/h3phone/page/";
		int quantityOrder = 0;
		if(userInfo != null)
		{
			List<ProductOrderDto> listProductOrder = productOrderService.getOrederByUserName(userInfo.getUsername());
			quantityOrder = listProductOrder.size();
		}
		model.addAttribute("quantityOrder", quantityOrder);
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("listProductDetail", pages);
		model.addAttribute("checked", false);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("searchDto", searchDto);
		model.addAttribute("searchInterDto", searchDto);
		model.addAttribute("listProductSpecial", listProductSpecial);

		return "shop";
	}

	@GetMapping("/h3phone/searchInternalMemory/{pageNumber}")
	public String searchInternalMemoryPage(@AuthenticationPrincipal UserDetailCustom userInfo,
			HttpServletRequest request, @PathVariable int pageNumber, Model model,
			@Validated SearchProductDetailDto searchDto) {
		if (searchDto.getListInternalMemory() == null) {
			return "redirect:/h3phone";
		}
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productDetailList");
		int pagesize = 9;
		List<String> internalMemoryList = new ArrayList<>();
		for (InternalMemoryDto internal : searchDto.getListInternalMemory()) {
			if (internal.isCheck()) {
				internalMemoryList.add(internal.getInternalMemory());
			}
		}

		List<BrandDto> listBrandDto = brandService.getAll();
		SearchProductDetailDto searchBrandDto = new SearchProductDetailDto();
		ArrayList<BrandDto> brands = new ArrayList<BrandDto>();

		for (BrandDto brand : listBrandDto) {
			brands.add(brand);
		}
		searchBrandDto.setListBrand(brands);
		List<ProductDetailDto> listProductSpecial = productDetailService.getProductSpecial();
		List<ProductDetailDto> list;
		if (internalMemoryList.size() > 0) {
			list = productDetailService.getProductByInternalMemory(internalMemoryList);
		} else {
			list = productDetailService.getAllByProduct();
		}

		pages = new PagedListHolder<>(list);
		pages.setPageSize(pagesize);

		final int goToPage = pageNumber - 1;
		if (goToPage <= pages.getPageCount() && goToPage >= 0) {
			pages.setPage(goToPage);
		}
		request.getSession().setAttribute("productDetailList", pages);

		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/h3phone/page/";
		int quantityOrder = 0;
		if(userInfo != null)
		{
			List<ProductOrderDto> listProductOrder = productOrderService.getOrederByUserName(userInfo.getUsername());
			quantityOrder = listProductOrder.size();
		}
		model.addAttribute("quantityOrder", quantityOrder);
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("listProductDetail", pages);
		model.addAttribute("checked", false);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("searchInterDto", searchDto);
		model.addAttribute("searchDto", searchBrandDto);
		model.addAttribute("listProductSpecial", listProductSpecial);

		return "shop";
	}

	@GetMapping("/h3phone/searchVote/{pageNumber}")
	public String searchVotePage(@AuthenticationPrincipal UserDetailCustom userInfo, HttpServletRequest request,
			@PathVariable int pageNumber, Model model, @RequestParam("minVote") float minVote,
			@RequestParam("maxVote") float maxVote) {

		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productDetailList");
		int pagesize = 9;

		List<BrandDto> listBrandDto = brandService.getAll();
		List<ProductDetailDto> listProductSpecial = productDetailService.getProductSpecial();
		SearchProductDetailDto searchDto = new SearchProductDetailDto();
		ArrayList<BrandDto> brands = new ArrayList<BrandDto>();

		ArrayList<InternalMemoryDto> listInternalMemory = new ArrayList<InternalMemoryDto>();
		ArrayList<RamDto> listRam = new ArrayList<RamDto>();
		DataFilteDto dataFilter = productDetailService.getDataFilter();
		InternalMemoryDto internal;
		RamDto ramDto;
		for (String internalMemory : dataFilter.getListInternalMemory()) {
			internal = new InternalMemoryDto();
			internal.setInternalMemory(internalMemory);
			listInternalMemory.add(internal);
		}
		for (String ram : dataFilter.getListRam()) {
			ramDto = new RamDto();
			ramDto.setRam(ram);
			listRam.add(ramDto);
		}
		searchDto.setListInternalMemory(listInternalMemory);

		for (BrandDto brand : listBrandDto) {
			brands.add(brand);
		}
		searchDto.setListBrand(brands);

		List<ProductDetailDto> list = productDetailService.getProductByVote(minVote, maxVote);

		pages = new PagedListHolder<>(list);
		pages.setPageSize(pagesize);

		final int goToPage = pageNumber - 1;
		if (goToPage <= pages.getPageCount() && goToPage >= 0) {
			pages.setPage(goToPage);
		}
		request.getSession().setAttribute("productDetailList", pages);

		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/h3phone/page/";
		int quantityOrder = 0;
		if(userInfo != null)
		{
			List<ProductOrderDto> listProductOrder = productOrderService.getOrederByUserName(userInfo.getUsername());
			quantityOrder = listProductOrder.size();
		}
		model.addAttribute("quantityOrder", quantityOrder);
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("listProductDetail", pages);
		model.addAttribute("checked", false);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("searchDto", searchDto);
		model.addAttribute("searchInterDto", searchDto);
		model.addAttribute("listProductSpecial", listProductSpecial);

		return "shop";
	}

	@GetMapping("login")
	public String login() {
		return "login";
	}

	@GetMapping("register")
	public String register(Model model) {
		model.addAttribute("newUser", new UserDto());
		return "register";
	}

	@PostMapping("register")
	public String registered(@Validated UserDto userDto, Model model, HttpServletRequest request) {

		UserDto userConfim = userService.findByUserName(userDto.getUserName());
		if (userConfim != null) {

			model.addAttribute("newUser", userDto);

			model.addAttribute("fail", "Tên đăng nhập đã tồn tại");
			model.addAttribute("title", "Rigister");

			return "register";
		} else {
			userDto.setCreatedBy(userDto.getUserName());
			userDto.setUpdatedBy(userDto.getUserName());

			userDto = userService.addUser(userDto);
			request.getSession().setAttribute("newUser", userDto);

			return "redirect:/login";
		}
	}

	@GetMapping("editUser")
	public String editUser(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@RequestParam("userName") String userName) {
		UserDto user = userService.findByUserName(userName);
		model.addAttribute("updateUser", user);
		return "editUser";
	}

	@PostMapping("editUser")
	public String editedUser(@Validated UserDto userDto, Model model) {
		userService.updateUser(userDto);
		return "redirect:/h3phone";
	}

	@GetMapping("shoppingCart")
	public String shoppingCart(@AuthenticationPrincipal UserDetailCustom userInfo, RedirectAttributes redirect,
			@RequestParam("productDetailId") long productDetailId) {
		UserDto userOrder = new UserDto();
		ProductDetailDto productDetailDto = productDetailService.getProductDetailById(productDetailId);
		int productRemain = productDetailDto.getQuantityImport() - productDetailDto.getQuantityExport();

		if (productRemain == 0) {

			redirect.addFlashAttribute("fail", "Sản phẩm hiện đã hết hàng quý khách vui lòng chon sản phẩm khác");

		} else {

			userOrder.setUserId(userInfo.getUser().getUserId());
			ProductOrderDto productOrder = new ProductOrderDto();
			productOrder.setCreatedBy(userInfo.getUsername());
			productOrder.setUpdatedBy(userInfo.getUsername());
			productOrder.setProductDetail(productDetailDto);
			productOrder.setOrderType(productDetailDto.getProductInfo().getProductType());
			productOrder.setUserOrder(userOrder);
			productOrder.setUpdatedBy(userInfo.getUsername());

			productOrder = productOrderService.createProductOrder(productOrder);

		}

		return "redirect:/h3phone";
	}

	@GetMapping("checkout")
	public String checkOut(@AuthenticationPrincipal UserDetailCustom userInfo, RedirectAttributes redirect,
			@RequestParam("userName") String userName, Model model) {

		List<ProductOrderDto> listProductOrder = productOrderService.getOrederByUserName(userName);
		if (listProductOrder.size() > 0) {
			ArrayList<ProductOrderDto> listOrder = new ArrayList<ProductOrderDto>();
			for (ProductOrderDto order : listProductOrder) {
				listOrder.add(order);
			}
			CheckOutDto checkOutDto = new CheckOutDto();
			checkOutDto.setListProductOrder(listOrder);
			checkOutDto.setCustomer(userInfo.getUser().getFullName());
			checkOutDto.setPhoneNumber(userInfo.getUser().getPhoneNumbers());
			checkOutDto.setAddress(userInfo.getUser().getAddress());
			int quantityOrder = 0;
			
			quantityOrder = listProductOrder.size();
			
			model.addAttribute("quantityOrder", quantityOrder);
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("checkOutDto", checkOutDto);
			return "checkout";
		} else {
			redirect.addFlashAttribute("fail", "Không có sản phẩm nào trong giỏ hàng");
			return "redirect:/h3phone";
		}

	}

	@PostMapping("checkout")
	public String checkedOut(@AuthenticationPrincipal UserDetailCustom userInfo, RedirectAttributes redirect,
			@Validated CheckOutDto checkOutDto, Model model) {

		InvoiceDto invoiceDto = new InvoiceDto();;
		
		List<ProductOrderDto> listProductOrder = new ArrayList<>();

		for (ProductOrderDto productOrder : checkOutDto.getListProductOrder()) {

			if (productOrder.isCheckOrder()) {

				invoiceDto.setComment(checkOutDto.getComment());
				invoiceDto.setAddress(checkOutDto.getAddress());
				invoiceDto.setPhoneNumber(checkOutDto.getPhoneNumber());
				invoiceDto.setCustomer(checkOutDto.getCustomer());
				invoiceDto.setCreatedBy(userInfo.getUsername());
				invoiceDto.setUpdatedBy(userInfo.getUsername());
				listProductOrder.add(productOrder);
			}
		}
		invoiceDto.setOrderInfo(listProductOrder);
		InvoiceDto invoice = invoiceService.createInvoice(invoiceDto);
		
		if (invoice != null) {
			redirect.addFlashAttribute("success", "Đặt hàng thành công");
		} else {
			redirect.addFlashAttribute("fail", "Đã có lỗi xảy ra vui lòng thử lại");
		}
		return "redirect:/h3phone";
	}

	@GetMapping("checkout/cancelOrder")
	public String cancelOrder(@AuthenticationPrincipal UserDetailCustom userInfo, RedirectAttributes redirect,
			Model model, @RequestParam("orderId") long orderId) {
		productOrderService.deleteProductOrder(orderId, userInfo.getUsername());
		return "redirect:/checkout?userName=" + userInfo.getUsername();
	}

	@GetMapping("delivery")
	public String delivery(@AuthenticationPrincipal UserDetailCustom userInfo, RedirectAttributes redirect, Model model,
			@RequestParam("userName") String userName) {
		List<ProductOrderDto> listInvoice = productOrderService.getProductDeliveryByUserName(userName);
		if (listInvoice.size() > 0) {
			int quantityOrder = 0;
			if(userInfo != null)
			{
				List<ProductOrderDto> listProductOrder = productOrderService.getOrederByUserName(userInfo.getUsername());
				quantityOrder = listProductOrder.size();
			}
			model.addAttribute("quantityOrder", quantityOrder);
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("listInvoice", listInvoice);
			return "delivery";
		} else {
			redirect.addFlashAttribute("fail", "Bạn cần đặt hàng trước khi kiểm tra");
			return "redirect:/h3phone";
		}
	}

	@GetMapping("delivery/cancelDelivery")
	public String cancelDelivery(@AuthenticationPrincipal UserDetailCustom userInfo, RedirectAttributes redirect,
			Model model, @RequestParam("invoiceId") long invoiceId) {
		int cancelStatus = productOrderService.cancelDelivery(userInfo.getUsername(), invoiceId);

		if (cancelStatus == 0) {

			redirect.addFlashAttribute("success", "Đơn hàng đã được hủy");
			return "redirect:/delivery?userName=" + userInfo.getUsername();
		} else {
			redirect.addFlashAttribute("fail",
					"Không thể hủy đơn hàng! Vui lòng liên hệ bộ phận chăm sóc khách hàng để được hỗ trọ");
			return "redirect:/delivery?userName=" + userInfo.getUsername();
		}

	}

	@GetMapping("h3phone/single")
	public String single(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@RequestParam("productDetailId") long productDetailId) {

		ProductDetailDto productDetail = productDetailService.getProductDetailById(productDetailId);

		SearchProductDetailDto searchColor = new SearchProductDetailDto();

		searchColor.setProductDetailId(productDetail.getProductInfo().getProductId());
		searchColor.setInternalMemory(productDetail.getInternalMemory());
		searchColor.setRam(productDetail.getRam());

		SearchProductDetailDto searchOther = new SearchProductDetailDto();
		searchOther.setBrandName(productDetail.getProductInfo().getBrandInfo().getBrandName());
		searchOther.setInternalMemory(productDetail.getInternalMemory());
		searchOther.setRam(productDetail.getRam());
		searchOther.setProductDetailId(productDetailId);

		List<ProductDetailDto> listProductColor = productDetailService.filterProductDetail(searchColor);

		List<ProductDetailDto> listProductOther = productDetailService.getProductOther(searchOther);
		List<ProductOrderDto> listCustomerReview = productOrderService.getReviewProduct(productDetailId);
		
		int quantityOrder = 0;
		if(userInfo != null)
		{
			List<ProductOrderDto> listProductOrder = productOrderService.getOrederByUserName(userInfo.getUsername());
			quantityOrder = listProductOrder.size();
		}
		model.addAttribute("quantityOrder", quantityOrder);
		model.addAttribute("listReview", listCustomerReview);
		model.addAttribute("listColor", listProductColor);
		model.addAttribute("listProductOther", listProductOther);
		model.addAttribute("productDetail", productDetail);
		model.addAttribute("userInfo", userInfo);
		return "single";
	}

	@GetMapping("customerReview")
	public String customerReview(@AuthenticationPrincipal UserDetailCustom userInfo, RedirectAttributes redirect,
			Model model, @RequestParam("userName") String userName) {

		List<ProductOrderDto> checkReview = productOrderService.getCheckReviewProduct(userName);

		if (checkReview != null) {

			int quantityOrder = 0;
			if(userInfo != null)
			{
				List<ProductOrderDto> listProductOrder = productOrderService.getOrederByUserName(userInfo.getUsername());
				quantityOrder = listProductOrder.size();
			}
			model.addAttribute("quantityOrder", quantityOrder);
			model.addAttribute("reviewProduct", checkReview);
			model.addAttribute("userInfo", userInfo);

			return "checkReview";
		} else {
			redirect.addFlashAttribute("fail", "Không có sản phẩm để bạn đánh giá");
			return "redirect:/h3phone";
		}

	}

	@GetMapping("customerReview/review")
	public String reivewProduct(@AuthenticationPrincipal UserDetailCustom userInfo, RedirectAttributes redirect,
			Model model, @RequestParam("invoiceId") long invoiceId) {

		ProductOrderDto invoiceDto = productOrderService.getOrderById(invoiceId);
		ProductDetailDto productDetail = productDetailService
				.getProductDetailById(invoiceDto.getProductDetail().getProductDetailId());

		SearchProductDetailDto searchColor = new SearchProductDetailDto();

		searchColor.setProductDetailId(productDetail.getProductInfo().getProductId());
		searchColor.setInternalMemory(productDetail.getInternalMemory());
		searchColor.setRam(productDetail.getRam());

		SearchProductDetailDto searchOther = new SearchProductDetailDto();
		searchOther.setBrandName(productDetail.getProductInfo().getBrandInfo().getBrandName());
		searchOther.setInternalMemory(productDetail.getInternalMemory());
		searchOther.setRam(productDetail.getRam());
		searchOther.setProductDetailId(productDetail.getProductDetailId());

		List<ProductDetailDto> listProductColor = productDetailService.filterProductDetail(searchColor);

		List<ProductDetailDto> listProductOther = productDetailService.getProductOther(searchOther);
        int quantityOrder = 0;
		if(userInfo != null)
		{
			List<ProductOrderDto> listProductOrder = productOrderService.getOrederByUserName(userInfo.getUsername());
			quantityOrder = listProductOrder.size();
		}
		model.addAttribute("quantityOrder", quantityOrder);
		model.addAttribute("listColor", listProductColor);
		model.addAttribute("listProductOther", listProductOther);
		model.addAttribute("productDetail", productDetail);
		model.addAttribute("invoiceId", invoiceId);
		model.addAttribute("userInfo", userInfo);

		return "customerReview";
	}

	@PostMapping("customerReview/review")
	public String sendReview(@AuthenticationPrincipal UserDetailCustom userInfo, RedirectAttributes redirect,
			@RequestParam("customerReview") String customerReview, @RequestParam("rate") String rate,
			@RequestParam("invoiceId") long invoiceId, @RequestParam("productDetailId") long productDetailId) {

		ProductOrderDto invoiceDto = new ProductOrderDto();
		int vote = Integer.parseInt(rate);

		invoiceDto.setCustomerVote(vote);
		invoiceDto.setCustomerReview(customerReview);
		invoiceDto.setOrderId(invoiceId);

		invoiceDto = productOrderService.reviewProduct(invoiceDto, productDetailId);
		if (invoiceDto != null) {
			redirect.addFlashAttribute("success", "Cảm ơn đánh giá và ý kiến đóng góp của bạn");
			return "redirect:/h3phone";
		} else {
			return "redirect:/customerReview/review?invoiceId=" + invoiceId;
		}

	}

	@GetMapping("forgotPassword")
	public String forgotPassword() {
		return "checkMail";
	}

	@PostMapping("forgotPassword")
	public String forgotPasswords(@RequestParam("email") String email, HttpServletRequest request, Model model) {

		UserDto userDto = userService.findByEmail(email);

		if (userDto != null) {
			int otp = otpService.generateOTP(userDto.getUserName());

			SimpleMailMessage message = new SimpleMailMessage();

			// message.setTo(userDto.getEmail());
			message.setTo("hieu1998hdtx@gmail.com");

			message.setSubject("Xác nhận thay đổi mật khẩu");
			message.setText("Vui lòng nhập mã xác nhận để lấy lại mật khẩu của bạn " + String.valueOf(otp));

			this.emailSender.send(message);

			request.getSession().setAttribute("user", userDto.getUserName());
			request.getSession().setAttribute("email", email);
			model.addAttribute("email", email);
			model.addAttribute("effect", "effect");
			return "checkOtp";
		} else {
			model.addAttribute("fail", "Email không chính xác");
			return "checkMail";
		}
	}

	@GetMapping("forgotPassword/changeOtp")
	public String changeOtp(@RequestParam("email") String email, HttpServletRequest request, Model model) {
		UserDto userDto = userService.findByEmail(email);

		if (userDto != null) {
			int otp = otpService.generateOTP(userDto.getUserName());

			SimpleMailMessage message = new SimpleMailMessage();

			message.setTo(userDto.getEmail());
			// message.setTo("hieu1998hdtx@gmail.com");

			message.setSubject("Xác nhận thay đổi mật khẩu");
			message.setText("Vui lòng nhập mã xác nhận để lấy lại mật khẩu của bạn " + String.valueOf(otp));

			this.emailSender.send(message);

			request.getSession().setAttribute("user", userDto.getUserName());
			request.getSession().setAttribute("email", email);
			model.addAttribute("email", email);
			model.addAttribute("effect", "effect");
			return "checkOtp";
		} else {
			model.addAttribute("fail", "Email không chính xác");
			return "checkMail";
		}
	}

	@PostMapping("forgotPassword/checkOtp")
	public String checkOtp(@RequestParam("otp") int otp, HttpServletRequest request, Model model) {
		String email = (String) request.getSession().getAttribute("email");
		if (otp > 0) {

			String userName = (String) request.getSession().getAttribute("user");
			int serverOtp = otpService.getOtp(userName);
			if (serverOtp > 0) {
				if (serverOtp == otp) {
					otpService.clearOTP(userName);
					UserDto user = userService.findByUserName(userName);
					model.addAttribute("userUpdate", user);
					return "changePasswords";
				} else {
					model.addAttribute("email", email);
					return "checkOtp";
				}
			} else {
				model.addAttribute("email", email);
				return "checkOtp";
			}

		} else {
			model.addAttribute("email", email);
			return "checkOtp";
		}

	}

	@PostMapping("forgotPassword/changePassword")
	public String changePassword(@Validated UserDto userDto, Model model) {

		UserDto user = userService.findByUserId(userDto.getUserId());
		user.setPassWords(userDto.getPassWords());

		user = userService.updateUser(user);
		if (user != null) {
			return "redirect:/login";
		} else {
			model.addAttribute("userUpdate", userDto);
			return "changePasswords";
		}

	}

	@GetMapping("403")
	public String errorPage() {
		return "403";
	}

	@GetMapping("contact")
	public String contactShop(@AuthenticationPrincipal UserDetailCustom userInfo, Model model) {
		
		int quantityOrder = 0;
		if(userInfo != null)
		{
			List<ProductOrderDto> listProductOrder = productOrderService.getOrederByUserName(userInfo.getUsername());
			quantityOrder = listProductOrder.size();
		}
		model.addAttribute("quantityOrder", quantityOrder);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("newContact", new CustomerContactDto());
		return "contact";
	}

	@PostMapping("contact")
	public String contactedSho(@AuthenticationPrincipal UserDetailCustom userInfo,
			@Validated CustomerContactDto contactDto, RedirectAttributes redirect) {

		UserDto user = new UserDto();
		user.setUserId(userInfo.getUser().getUserId());

		contactDto.setUserContact(user);
		contactDto.setCreatedBy(userInfo.getUsername());
		contactDto.setUpdatedBy(userInfo.getUsername());

		CustomerContactDto newContact = contactService.createContact(contactDto);

		return "redirect:/h3phone";
	}

	@GetMapping("h3phone/headPhone")
	public String headPhonePage(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			HttpServletRequest request, RedirectAttributes redirect) {

		model.addAttribute("userInfo", userInfo);
		request.getSession().setAttribute("headPhoneList", null);
		request.getSession().setAttribute("searchproductList", null);

		if (model.asMap().get("success") != null)
			redirect.addFlashAttribute("success", model.asMap().get("success").toString());
		if (model.asMap().get("fail") != null)
			redirect.addFlashAttribute("fail", model.asMap().get("fail").toString());
		return "redirect:/h3phone/headPhone/page/1";
	}

	@GetMapping("/h3phone/headPhone/page/{pageNumber}")
	public String showHeadPhonePage(@AuthenticationPrincipal UserDetailCustom userInfo, HttpServletRequest request,
			@PathVariable int pageNumber, Model model) {
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("headPhoneList");
		int pagesize = 9;
		List<HeadPhoneDetailDto> list = headPhoneService.getAll();
		List<BrandDto> listBrandDto = brandService.getAll();

		SearchProductDetailDto searchDto = new SearchProductDetailDto();
		ArrayList<BrandDto> brands = new ArrayList<BrandDto>();

		List<ProductDetailDto> listProductSpecial = productDetailService.getProductSpecial();

		for (BrandDto brand : listBrandDto) {

			brands.add(brand);
		}
		searchDto.setListBrand(brands);

		if (pages == null) {
			pages = new PagedListHolder<>(list);
			pages.setPageSize(pagesize);
		} else {
			SearchProductDetailDto search = (SearchProductDetailDto) request.getSession()
					.getAttribute("searchBrandList");
			if (search != null) {
				searchDto = search;
			}
			final int goToPage = pageNumber - 1;
			if (goToPage <= pages.getPageCount() && goToPage >= 0) {
				pages.setPage(goToPage);
			}
		}
		request.getSession().setAttribute("headPhoneList", pages);
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/h3phone/headPhone/page/";
		int quantityOrder = 0;
		if(userInfo != null)
		{
			List<ProductOrderDto> listProductOrder = productOrderService.getOrederByUserName(userInfo.getUsername());
			quantityOrder = listProductOrder.size();
		}
		model.addAttribute("quantityOrder", quantityOrder);
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("listHeadPhone", pages);
		model.addAttribute("checked", false);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("searchDto", searchDto);

		model.addAttribute("listProductSpecial", listProductSpecial);

		return "shopHeadPhone";
	}

	@GetMapping("/h3phone/headPhone/searchBrand/{pageNumber}")
	public String searchBrandHeadPhonege(@AuthenticationPrincipal UserDetailCustom userInfo, HttpServletRequest request,
			@PathVariable int pageNumber, Model model, @Validated SearchProductDetailDto searchDto) {
		if (searchDto.getListBrand() == null) {
			return "redirect:/h3phone/headPhone";
		}
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("headPhoneList");
		int pagesize = 9;
		List<String> brandNameList = new ArrayList<>();
		for (BrandDto brand : searchDto.getListBrand()) {
			if (brand.isCheckBrand()) {
				brandNameList.add(brand.getBrandName());
			}
		}

		List<ProductDetailDto> listProductSpecial = productDetailService.getProductSpecial();
		List<HeadPhoneDetailDto> list;
		if (brandNameList.size() > 0) {
			list = headPhoneService.getProductByBrand(brandNameList);
		} else {
			list = headPhoneService.getAll();
		}

		pages = new PagedListHolder<>(list);
		pages.setPageSize(pagesize);

		final int goToPage = pageNumber - 1;
		if (goToPage <= pages.getPageCount() && goToPage >= 0) {
			pages.setPage(goToPage);
		}
		request.getSession().setAttribute("headPhoneList", pages);
		request.getSession().setAttribute("searchBrandList", searchDto);
		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/h3phone/headPhone/page/";
		int quantityOrder = 0;
		if(userInfo != null)
		{
			List<ProductOrderDto> listProductOrder = productOrderService.getOrederByUserName(userInfo.getUsername());
			quantityOrder = listProductOrder.size();
		}
		model.addAttribute("quantityOrder", quantityOrder);
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("listHeadPhone", pages);
		model.addAttribute("checked", false);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("searchDto", searchDto);
		model.addAttribute("searchInterDto", searchDto);
		model.addAttribute("listProductSpecial", listProductSpecial);

		return "shopHeadPhone";
	}

	@GetMapping("/h3phone/headPhone/searchPrices/{pageNumber}")
	public String searchPricesHeadPhonePage(@AuthenticationPrincipal UserDetailCustom userInfo,
			HttpServletRequest request, @PathVariable int pageNumber, Model model,
			@RequestParam("minPrices") int minPrices, @RequestParam("maxPrices") int maxPrices) {
		if (maxPrices <= 0 || maxPrices <= minPrices) {
			return "redirect:/h3phone/headPhone";
		}
		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("headPhoneList");
		int pagesize = 9;

		List<BrandDto> listBrandDto = brandService.getAll();
		List<ProductDetailDto> listProductSpecial = productDetailService.getProductSpecial();
		SearchProductDetailDto searchDto = new SearchProductDetailDto();
		ArrayList<BrandDto> brands = new ArrayList<BrandDto>();

		for (BrandDto brand : listBrandDto) {
			brands.add(brand);
		}
		searchDto.setListBrand(brands);

		List<HeadPhoneDetailDto> list = headPhoneService.getProductByPrice(minPrices, maxPrices);

		pages = new PagedListHolder<>(list);
		pages.setPageSize(pagesize);

		final int goToPage = pageNumber - 1;
		if (goToPage <= pages.getPageCount() && goToPage >= 0) {
			pages.setPage(goToPage);
		}
		request.getSession().setAttribute("headPhoneList", pages);

		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/h3phone/headPhone/page/";
		int quantityOrder = 0;
		if(userInfo != null)
		{
			List<ProductOrderDto> listProductOrder = productOrderService.getOrederByUserName(userInfo.getUsername());
			quantityOrder = listProductOrder.size();
		}
		model.addAttribute("quantityOrder", quantityOrder);
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("listHeadPhone", pages);
		model.addAttribute("checked", false);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("searchDto", searchDto);

		model.addAttribute("listProductSpecial", listProductSpecial);

		return "shopHeadPhone";
	}

	@GetMapping("/h3phone/headPhone/searchVote/{pageNumber}")
	public String searchVoteHeadPhonePage(@AuthenticationPrincipal UserDetailCustom userInfo,
			HttpServletRequest request, @PathVariable int pageNumber, Model model,
			@RequestParam("minVote") float minVote, @RequestParam("maxVote") float maxVote) {

		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("headPhoneList");
		int pagesize = 9;

		List<BrandDto> listBrandDto = brandService.getAll();
		List<ProductDetailDto> listProductSpecial = productDetailService.getProductSpecial();
		SearchProductDetailDto searchDto = new SearchProductDetailDto();
		ArrayList<BrandDto> brands = new ArrayList<BrandDto>();

		for (BrandDto brand : listBrandDto) {
			brands.add(brand);
		}
		searchDto.setListBrand(brands);

		List<HeadPhoneDetailDto> list = headPhoneService.getProductByVote(minVote, maxVote);

		pages = new PagedListHolder<>(list);
		pages.setPageSize(pagesize);

		final int goToPage = pageNumber - 1;
		if (goToPage <= pages.getPageCount() && goToPage >= 0) {
			pages.setPage(goToPage);
		}
		request.getSession().setAttribute("headPhoneList", pages);

		int current = pages.getPage() + 1;
		int begin = Math.max(1, current - list.size());
		int end = Math.min(begin + 5, pages.getPageCount());
		int totalPageCount = pages.getPageCount();
		String baseUrl = "/h3phone/headPhone/page/";
		int quantityOrder = 0;
		if(userInfo != null)
		{
			List<ProductOrderDto> listProductOrder = productOrderService.getOrederByUserName(userInfo.getUsername());
			quantityOrder = listProductOrder.size();
		}
		model.addAttribute("quantityOrder", quantityOrder);
		model.addAttribute("beginIndex", begin);
		model.addAttribute("endIndex", end);
		model.addAttribute("currentIndex", current);
		model.addAttribute("totalPageCount", totalPageCount);
		model.addAttribute("baseUrl", baseUrl);
		model.addAttribute("listHeadPhone", pages);
		model.addAttribute("checked", false);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("searchDto", searchDto);
		model.addAttribute("searchInterDto", searchDto);
		model.addAttribute("listProductSpecial", listProductSpecial);

		return "shopHeadPhone";
	}

	@GetMapping("shoppingCart/headPhone")
	public String shoppingCartHeadPhone(@AuthenticationPrincipal UserDetailCustom userInfo, RedirectAttributes redirect,
			@RequestParam("headPhoneId") long headPhoneId) {
		UserDto userOrder = new UserDto();
		HeadPhoneDetailDto headPhoneDto = headPhoneService.getById(headPhoneId);
		int productRemain = headPhoneDto.getQuantityImport() - headPhoneDto.getQuantityExport();

		if (productRemain == 0) {

			redirect.addFlashAttribute("fail", "Sản phẩm hiện đã hết hàng quý khách vui lòng chon sản phẩm khác");

		} else {

			userOrder.setUserId(userInfo.getUser().getUserId());
			ProductOrderDto productOrder = new ProductOrderDto();
			productOrder.setCreatedBy(userInfo.getUsername());
			productOrder.setUpdatedBy(userInfo.getUsername());
			productOrder.setHeadPhone(headPhoneDto);
			productOrder.setOrderType(headPhoneDto.getProduct().getProductType());
			productOrder.setUserOrder(userOrder);
			productOrder.setUpdatedBy(userInfo.getUsername());

			productOrder = productOrderService.createProductOrder(productOrder);

		}

		return "redirect:/h3phone/headPhone";
	}

	@GetMapping("h3phone/single/headPhone")
	public String singleHeadPhone(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@RequestParam("headPhoneId") long headPhoneId) {

		HeadPhoneDetailDto headPhone = headPhoneService.getById(headPhoneId);

		List<HeadPhoneDetailDto> listHeadPhoneOther = headPhoneService
				.getProductOther(headPhone.getProduct().getProductName(), headPhone.getPrices(),headPhoneId);
		List<ProductOrderDto> listCustomerReview = productOrderService.getReviewHeadPhone(headPhoneId);
		int quantityOrder = 0;
		if(userInfo != null)
		{
			List<ProductOrderDto> listProductOrder = productOrderService.getOrederByUserName(userInfo.getUsername());
			quantityOrder = listProductOrder.size();
		}
		model.addAttribute("quantityOrder", quantityOrder);
		model.addAttribute("listReview", listCustomerReview);
		model.addAttribute("listHeadPhoneOther", listHeadPhoneOther);
		model.addAttribute("headPhone", headPhone);
		model.addAttribute("userInfo", userInfo);
		return "singleHeadPhone";
	}
	@GetMapping("customerReview/review/headPhone")
	public String reivewHeadPhone(@AuthenticationPrincipal UserDetailCustom userInfo, RedirectAttributes redirect,
			Model model, @RequestParam("invoiceId") long invoiceId) {

		ProductOrderDto invoiceDto = productOrderService.getOrderById(invoiceId);
		HeadPhoneDetailDto headPhone = headPhoneService.getById(invoiceDto.getHeadPhone().getHeadPhoneId());

		List<HeadPhoneDetailDto> listHeadPhoneOther = headPhoneService
				.getProductOther(headPhone.getProduct().getProductName(), headPhone.getPrices(),headPhone.getHeadPhoneId());
		
		model.addAttribute("listHeadPhoneOther", listHeadPhoneOther);
		model.addAttribute("invoiceId", invoiceId);
		model.addAttribute("headPhone", headPhone);
		model.addAttribute("userInfo", userInfo);

		return "customerReviewHeadPhone";
	}

	@PostMapping("customerReview/review/headPhone")
	public String sendReviewHeadPhone(@AuthenticationPrincipal UserDetailCustom userInfo, RedirectAttributes redirect,
			@RequestParam("customerReview") String customerReview, @RequestParam("rate") String rate,
			@RequestParam("invoiceId") long invoiceId, @RequestParam("headPhoneId") long headPhone) {

		ProductOrderDto invoiceDto = new ProductOrderDto();
		int vote = Integer.parseInt(rate);

		invoiceDto.setCustomerVote(vote);
		invoiceDto.setCustomerReview(customerReview);
		invoiceDto.setOrderId(invoiceId);

		invoiceDto = productOrderService.reviewProduct(invoiceDto, headPhone);
		if (invoiceDto != null) {
			redirect.addFlashAttribute("success", "Cảm ơn đánh giá và ý kiến đóng góp của bạn");
			return "redirect:/h3phone";
		} else {
			return "redirect:/customerReview/review?invoiceId=" + invoiceId;
		}

	}
	@GetMapping("h3phone/about")
	public String about(@AuthenticationPrincipal UserDetailCustom userInfo, Model model)
	{
		int quantityOrder = 0;
		if(userInfo != null)
		{
			List<ProductOrderDto> listProductOrder = productOrderService.getOrederByUserName(userInfo.getUsername());
			quantityOrder = listProductOrder.size();
		}
		model.addAttribute("quantityOrder", quantityOrder);
		model.addAttribute("userInfo", userInfo);
		return "about";
	}
}
