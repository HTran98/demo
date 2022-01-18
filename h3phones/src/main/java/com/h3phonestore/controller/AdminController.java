package com.h3phonestore.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import org.thymeleaf.util.StringUtils;

import com.h3phonestore.constants.Constants;
import com.h3phonestore.dto.BrandDto;
import com.h3phonestore.dto.CustomerContactDto;
import com.h3phonestore.dto.DataFilteDto;
import com.h3phonestore.dto.ExportProduct;
import com.h3phonestore.dto.ExportProductDetail;
import com.h3phonestore.dto.HeadPhoneDetailDto;
import com.h3phonestore.dto.InvoiceDto;
import com.h3phonestore.dto.ProductDetailDto;
import com.h3phonestore.dto.ProductDto;
import com.h3phonestore.dto.ProductOrderDto;
import com.h3phonestore.dto.ReportDto;
import com.h3phonestore.dto.RoleDto;
import com.h3phonestore.dto.SearchDateDto;
import com.h3phonestore.dto.SearchProductDetailDto;
import com.h3phonestore.dto.UserDto;
import com.h3phonestore.entity.Role;
import com.h3phonestore.entity.UserDetailCustom;
import com.h3phonestore.service.BrandService;
import com.h3phonestore.service.CustomerContactService;
import com.h3phonestore.service.HeadPhoneService;
import com.h3phonestore.service.InvoiceService;
import com.h3phonestore.service.ProductDetailService;
import com.h3phonestore.service.ProductOrderService;
import com.h3phonestore.service.ProductService;
import com.h3phonestore.service.RoleService;
import com.h3phonestore.service.UserService;

@Controller
@RequestMapping("/adminPage")
public class AdminController {

	@Autowired
	UserService userService;

	@Autowired
	RoleService roleService;

	@Autowired
	BrandService brandService;

	@Autowired
	ProductService productService;

	@Autowired
	ProductDetailService productDetailService;

	@Autowired
	InvoiceService invoiceService;

	@Autowired
	CustomerContactService contactService;

	@Autowired
	HeadPhoneService headPhoneService;

	@Autowired
	public JavaMailSender emailSender;

	@Autowired
	public ProductOrderService productOrderService;

	@GetMapping("/home")
	public String homeAdmin(@AuthenticationPrincipal UserDetailCustom userInfo, Model model) {
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("title", "Home");
		return "admin/admin";
	}

	@GetMapping("/user")
	public String user(@AuthenticationPrincipal UserDetailCustom userInfo, Model model) {

		List<UserDto> listUser = userService.getAllUser();
		List<RoleDto> listRole = roleService.getAll();

		model.addAttribute("listRole", listRole);
		model.addAttribute("listUser", listUser);
		model.addAttribute("listUserFilter", listUser);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("title", "User");

		return "admin/user";
	}

	@GetMapping("/user/createUser")
	public String createUser(@AuthenticationPrincipal UserDetailCustom userInfo, Model model) {

		List<RoleDto> listRole = roleService.getAll();
		UserDto newUser = new UserDto();

		model.addAttribute("listRole", listRole);
		model.addAttribute("newUser", newUser);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("title", "Create User");

		return "admin/addUser";
	}

	@PostMapping("/user/createUser")
	public String createdUser(@AuthenticationPrincipal UserDetailCustom userInfo, @Validated UserDto userDto,
			Model model) {

		UserDto userConfim = userService.findByUserName(userDto.getUserName());
		if (userConfim != null) {
			List<RoleDto> listRole = roleService.getAll();

			model.addAttribute("listRole", listRole);
			model.addAttribute("newUser", userDto);
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("fail", "Tên đăng nhập đã tồn tại");
			model.addAttribute("title", "Create User");

			return "admin/addUser";
		} else {
			userDto.setCreatedBy(userInfo.getUsername());
			userDto.setUpdatedBy(userInfo.getUsername());
			userConfim = userService.addUser(userDto);

			return "redirect:/adminPage/user";
		}

	}

	@GetMapping("/user/update")
	public String updateUser(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@RequestParam("userId") long userId) {

		UserDto user = userService.findByUserId(userId);
		List<RoleDto> listRole = roleService.getAll();

		model.addAttribute("userUpdate", user);
		model.addAttribute("listRole", listRole);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("title", "Update User");

		return "admin/updateUser";
	}

	@PostMapping("/user/update")
	public String updatedUser(@AuthenticationPrincipal UserDetailCustom userInfo, @Validated UserDto userDto,
			RedirectAttributes redirect) {
		userDto.setUpdatedBy(userInfo.getUsername());
		UserDto user = userService.updateUser(userDto);

		if (user != null) {
			redirect.addFlashAttribute("success", "Cập nhật thông tin thành công");
			return "redirect:/adminPage/user";
		} else {
			redirect.addFlashAttribute("fail", "Cập nhật thông tin thất bại");
			return "redirect: /adminPage/user/update?userId=" + userDto.getUserId();
		}

	}

	@GetMapping("/user/delete")
	public String deleteUser(@AuthenticationPrincipal UserDetailCustom userInfo, @RequestParam("userId") long userId) {

		userService.deleteUse(userId, userInfo.getUsername());

		return "redirect:/adminPage/user";
	}

	@GetMapping("/user/search")
	public String searchUser(@AuthenticationPrincipal UserDetailCustom userInfo,
			@RequestParam("keyWords") String keyWords, Model model) {

		List<UserDto> listUser = userService.findUserLike(keyWords);
		List<UserDto> listUserFilter = userService.getAllUser();
		List<RoleDto> listRole = roleService.getAll();

		model.addAttribute("listRole", listRole);
		model.addAttribute("listUser", listUser);
		model.addAttribute("listUserFilter", listUserFilter);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("title", "User");

		return "admin/user";
	}

	@GetMapping("/user/filter")
	public String filterUser(@AuthenticationPrincipal UserDetailCustom userInfo, @RequestParam("roleId") long roleId,
			@RequestParam("userId") String userId, Model model) {
		List<UserDto> listUser = userService.findUser(roleId, userId);
		List<UserDto> listUserFilter = userService.getAllUser();
		List<RoleDto> listRole = roleService.getAll();

		model.addAttribute("listRole", listRole);
		model.addAttribute("listUser", listUser);
		model.addAttribute("listUserFilter", listUserFilter);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("title", "User");

		return "admin/user";
	}

	@GetMapping("/updateAccount")
	public String updaetAccount(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@RequestParam("userName") String userName) {
		UserDto user = userService.findByUserName(userName);

		model.addAttribute("userUpdate", user);

		model.addAttribute("userInfo", userInfo);
		model.addAttribute("title", "Update Account");

		return "admin/updateAccount";
	}

	@GetMapping("/role")
	public String role(@AuthenticationPrincipal UserDetailCustom userInfo, Model model) {

		List<RoleDto> listRole = roleService.getAll();
		model.addAttribute("listRole", listRole);
		model.addAttribute("title", "Role");
		model.addAttribute("userInfo", userInfo);

		return "admin/role";
	}

	@GetMapping("/role/create")
	public String createRole(@AuthenticationPrincipal UserDetailCustom userInfo, Model model) {

		model.addAttribute("newRole", new Role());
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("title", "Create role");

		return "admin/addRole";
	}

	@PostMapping("/role/create")
	public String createdRole(@AuthenticationPrincipal UserDetailCustom userInfo, @Validated RoleDto roleDto,
			Model model) {

		roleDto.setCreatedBy(userInfo.getUsername());
		roleDto.setUpdatedBy(userInfo.getUsername());
		RoleDto role = roleService.createRole(roleDto);

		if (role != null) {
			return "redirect:/adminPage/role";
		} else {

			model.addAttribute("newRole", roleDto);
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("title", "Create role");
			model.addAttribute("fail", "RoleName đã tồn tại");

			return "admin/addRole";
		}

	}

	@GetMapping("/role/update")
	public String updateRole(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@RequestParam("roleId") long roleId) {

		RoleDto role = roleService.getRoleById(roleId);

		model.addAttribute("roleUpdate", role);

		model.addAttribute("userInfo", userInfo);
		model.addAttribute("title", "Update Role");

		return "admin/updateRole";
	}

	@PostMapping("/role/update")
	public String updatedRole(@AuthenticationPrincipal UserDetailCustom userInfo, @Validated RoleDto roleDto,
			RedirectAttributes redirect) {
		roleDto.setUpdatedBy(userInfo.getUsername());
		RoleDto role = roleService.updateRole(roleDto);

		if (role != null) {
			redirect.addFlashAttribute("success", "Cập nhật thông tin thành công");
			return "redirect:/adminPage/role";
		} else {
			redirect.addFlashAttribute("fail", "Cập nhật thông tin thất bại");
			return "redirect:/adminPage/role/update?roleId=" + roleDto.getRoleId();
		}

	}

	@GetMapping("/role/delete")
	public String deleteRole(@AuthenticationPrincipal UserDetailCustom userInfo, @RequestParam("roleId") long roleId) {

		roleService.deleteRole(roleId, userInfo.getUsername());

		return "redirect:/adminPage/role";
	}

	@GetMapping("/role/search")
	public String searchRole(@AuthenticationPrincipal UserDetailCustom userInfo,
			@RequestParam("keyWords") String keyWords, Model model) {

		List<RoleDto> listRole = roleService.getRoleByRoleName(keyWords);

		model.addAttribute("listRole", listRole);

		model.addAttribute("userInfo", userInfo);
		model.addAttribute("title", "Role");

		return "admin/role";
	}

	@GetMapping("/brand")
	public String brand(@AuthenticationPrincipal UserDetailCustom userInfo, Model model) {

		List<BrandDto> listBrand = brandService.getAll();
		ExportProduct exProduct = new ExportProduct();
		ArrayList<BrandDto> listBrandEx = new ArrayList<>();
		for (BrandDto brand : listBrand) {
			listBrandEx.add(brand);
		}
		exProduct.setListBrand(listBrandEx);
		model.addAttribute("listBrandEx", exProduct);
		model.addAttribute("title", "Brand");
		model.addAttribute("userInfo", userInfo);

		return "admin/brand";
	}

	@PostMapping("/brand/export")
	public void exportBrandCsv(HttpServletResponse response, @Validated ExportProduct exProduct) throws IOException {

		response.setContentType("text/csv; charset=UTF-8");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=Brand_" + currentDateTime + ".csv";
		response.setHeader(headerKey, headerValue);

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = { "Mã thương hiệu", "Thương hiệu" };
		String[] nameMapping = { "brandId", "brandName" };

		csvWriter.writeHeader(csvHeader);
		for (BrandDto brand : exProduct.getListBrand()) {
			csvWriter.write(brand, nameMapping);
		}

		csvWriter.close();

	}

	@GetMapping("/brand/create")
	public String createBrand(@AuthenticationPrincipal UserDetailCustom userInfo, Model model) {

		model.addAttribute("newBrand", new BrandDto());
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("title", "Create Brand");

		return "admin/addBrand";
	}

	@PostMapping("/brand/create")
	public String createdBrand(@AuthenticationPrincipal UserDetailCustom userInfo, @Validated BrandDto brandDto,
			Model model) {

		BrandDto brandConfim = brandService.getBrandName(brandDto.getBrandName());
		if (brandConfim != null) {
			model.addAttribute("newBrand", brandDto);
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("title", "Create Brand");
			model.addAttribute("fail", "Thương hiệu đã tồn tại");

			return "admin/addBrand";
		} else {
			brandDto.setCreatedBy(userInfo.getUsername());
			brandDto.setUpdatedBy(userInfo.getUsername());

			brandService.createBrand(brandDto);

			return "redirect:/adminPage/brand";
		}

	}

	@GetMapping("/brand/update")
	public String updateBrand(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@RequestParam("brandId") long brandId) {

		BrandDto brand = brandService.getBrandById(brandId);

		model.addAttribute("brandUpdate", brand);

		model.addAttribute("userInfo", userInfo);
		model.addAttribute("title", "Update Role");

		return "admin/updateBrand";
	}

	@PostMapping("/brand/update")
	public String updatedBrand(@AuthenticationPrincipal UserDetailCustom userInfo, @Validated BrandDto brandDto,
			RedirectAttributes redirect) {

		brandDto.setUpdatedBy(userInfo.getUsername());
		BrandDto brand = brandService.updateBrand(brandDto);

		if (brand != null) {
			redirect.addFlashAttribute("success", "Cập nhật thông tin thành công");
			return "redirect:/adminPage/brand";
		} else {
			redirect.addFlashAttribute("fail", "Cập nhật thông tin thất bại");
			return "redirect:/adminPage/brand/update?brandId=" + brandDto.getBrandId();
		}

	}

	@GetMapping("/brand/delete")
	public String deleteBrand(@AuthenticationPrincipal UserDetailCustom userInfo,
			@RequestParam("brandId") long brandId) {

		brandService.deleteBrand(brandId, userInfo.getUsername());

		return "redirect:/adminPage/brand";
	}

	@GetMapping("/brand/search")
	public String searchBrand(@AuthenticationPrincipal UserDetailCustom userInfo,
			@RequestParam("keyWords") String keyWords, Model model) {

		List<BrandDto> listBrand = brandService.getByBrandName(keyWords);
		;

		ExportProduct exProduct = new ExportProduct();
		ArrayList<BrandDto> listBrandEx = new ArrayList<>();
		for (BrandDto brand : listBrand) {
			listBrandEx.add(brand);
		}
		exProduct.setListBrand(listBrandEx);
		model.addAttribute("listBrandEx", exProduct);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("title", "Brand");

		return "admin/brand";
	}

	@GetMapping("/product")
	public String product(@AuthenticationPrincipal UserDetailCustom userInfo, Model model) {

		List<ProductDto> listProduct = productService.getAll();
		ExportProduct exProduct = new ExportProduct();
		ArrayList<ProductDto> listProductEx = new ArrayList<>();
		for (ProductDto product : listProduct) {
			listProductEx.add(product);
		}
		exProduct.setListProduct(listProductEx);
		model.addAttribute("listProduct", exProduct);
		model.addAttribute("title", "Product");
		model.addAttribute("userInfo", userInfo);

		return "admin/product";
	}

	@PostMapping("/product/export")
	public void exportProductCsv(HttpServletResponse response, @Validated ExportProduct exProduct) throws IOException {

		response.setContentType("text/csv; charset=UTF-8");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=Product_" + currentDateTime + ".csv";
		response.setHeader(headerKey, headerValue);

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = { "Mã sản phẩm", "Sản phẩm", "Thương hiệu" };
		String[] nameMapping = { "productId", "productName", "brandNameProduct" };

		csvWriter.writeHeader(csvHeader);
		for (ProductDto prduct : exProduct.getListProduct()) {
			csvWriter.write(prduct, nameMapping);
		}

		csvWriter.close();

	}

	@GetMapping("/product/create")
	public String createPoduct(@AuthenticationPrincipal UserDetailCustom userInfo, Model model) {
		List<BrandDto> listBrand = brandService.getAll();

		model.addAttribute("listBrand", listBrand);
		model.addAttribute("newProduct", new ProductDto());
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("title", "Create Product");

		return "admin/addProduct";
	}

	@PostMapping("/product/create")
	public String createdProduct(@AuthenticationPrincipal UserDetailCustom userInfo, @Validated ProductDto productDto,
			Model model) {
		ProductDto product = productService.getProduct(productDto.getProductName(),
				productDto.getBrandInfo().getBrandId());

		if (product != null) {
			List<BrandDto> listBrand = brandService.getAll();
			model.addAttribute("listBrand", listBrand);
			model.addAttribute("newProduct", productDto);
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("title", "Create Product");
			model.addAttribute("fail", "Sản phẩm đã tồn tại");

			return "admin/addProduct";
		} else {
			productDto.setCreatedBy(userInfo.getUsername());
			productDto.setUpdatedBy(userInfo.getUsername());
			productService.createProduct(productDto);

			return "redirect:/adminPage/product";
		}

	}

	@GetMapping("/product/update")
	public String updateProduct(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@RequestParam("productId") long productId) {

		ProductDto product = productService.getByProductId(productId);
		List<BrandDto> listBrand = brandService.getAll();

		model.addAttribute("listBrand", listBrand);

		model.addAttribute("productUpdate", product);

		model.addAttribute("userInfo", userInfo);
		model.addAttribute("title", "Update Product");

		return "admin/updateProduct";
	}

	@PostMapping("/product/update")
	public String updatedProduct(@AuthenticationPrincipal UserDetailCustom userInfo, @Validated ProductDto productDto,
			RedirectAttributes redirect) {

		productDto.setUpdatedBy(userInfo.getUsername());
		ProductDto product = productService.updateProduct(productDto);

		if (product != null) {
			redirect.addFlashAttribute("success", "Cập nhật thông tin thành công");
			return "redirect:/adminPage/product";
		} else {
			redirect.addFlashAttribute("fail", "Cập nhật thông tin thất bại");
			return "redirect:/adminPage/product/update?productId=" + productDto.getProductId();
		}

	}

	@GetMapping("/product/delete")
	public String deleteProduct(@AuthenticationPrincipal UserDetailCustom userInfo,
			@RequestParam("productId") long productId) {

		productService.deleteProduct(productId, userInfo.getUsername());

		return "redirect:/adminPage/product";
	}

	@GetMapping("/product/search")
	public String searchProduct(@AuthenticationPrincipal UserDetailCustom userInfo,
			@RequestParam("keyWords") String keyWords, Model model) {

		List<ProductDto> productList = productService.getByProductName(keyWords);
		ExportProduct exProduct = new ExportProduct();
		ArrayList<ProductDto> listProductEx = new ArrayList<>();
		for (ProductDto product : productList) {
			listProductEx.add(product);
		}
		exProduct.setListProduct(listProductEx);

		model.addAttribute("listProduct", exProduct);

		model.addAttribute("userInfo", userInfo);
		model.addAttribute("title", "Product");

		return "admin/product";
	}

	@GetMapping("/product/importCsv")
	public String importFileProductCsv(@AuthenticationPrincipal UserDetailCustom userInfo, Model model) {
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("styleImport", Constants.IMPORTPRODUCT);
		return "admin/inputCSV";
	}

	@PostMapping("/product/importCsv")
	public String importProductCsv(@RequestParam("csv") MultipartFile csvFile, HttpServletResponse response,
			@AuthenticationPrincipal UserDetailCustom userInfo, Model model) throws IOException {
		if (Constants.TYPEFILE.equals(csvFile.getContentType())
				|| csvFile.getContentType().equals("application/vnd.ms-excel")) {
			InputStream fileImport = csvFile.getInputStream();
			BufferedReader fileReader = new BufferedReader(new InputStreamReader(fileImport, "UTF-8"));
			CSVParser csvParser = new CSVParser(fileReader,
					CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

			List<ProductDto> listProduct = new ArrayList<>();

			Iterable<CSVRecord> csvRecords = csvParser.getRecords();
			ProductDto productDto;
			BrandDto brandDto;

			for (CSVRecord record : csvRecords) {

				productDto = new ProductDto();
				brandDto = new BrandDto();
				brandDto.setBrandId(Long.parseLong(record.get("brandCode")));
				productDto.setBrandInfo(brandDto);
				productDto.setBrandNames(record.get("brandName"));
				productDto.setProductName(record.get("productName"));
				productDto.setProductType(Integer.parseInt(record.get("productType")));
				productDto.setDeleteFlag(Constants.DELETE_FLAG);
				productDto.setCreatedBy(userInfo.getUsername());
				productDto.setUpdatedBy(userInfo.getUsername());

				listProduct.add(productDto);
			}
			ArrayList<ProductDto> listPr = new ArrayList<>();
			for (ProductDto product : listProduct) {
				listPr.add(product);
			}

			ExportProduct exProduct = new ExportProduct();
			exProduct.setListProduct(listPr);

			model.addAttribute("title", "Load Import CSV");
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("productList", exProduct);
			return "admin/loadImportCsv";
		} else {
			model.addAttribute("userInfo", userInfo);
			return "admin/inputCSV";
		}

	}

	@PostMapping("product/saveData")
	public String saveDataProductImport(@Validated ExportProduct exProduct) {

		productService.saveData(exProduct.getListProduct());

		return "redirect:/adminPage/product";
	}

	@GetMapping("/productDetail")
	public String productDetail(@AuthenticationPrincipal UserDetailCustom userInfo, Model model) {

		List<ProductDetailDto> listProductDetail = productDetailService.getAll();
		List<BrandDto> listBrand = brandService.getAll();
		List<ProductDto> listProduct = productService.getByProductType(Constants.TYPEPHONE);
		DataFilteDto dataFilter = productDetailService.getDataFilter();

		ArrayList<ProductDetailDto> listPr = new ArrayList<>();
		for (ProductDetailDto productDetail : listProductDetail) {
			listPr.add(productDetail);
		}
		ExportProductDetail exProduct = new ExportProductDetail();
		exProduct.setListProductDetail(listPr);

		model.addAttribute("searchDto", new SearchProductDetailDto());
		model.addAttribute("exProduct", exProduct);
		model.addAttribute("listProduct", listProduct);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("listBrand", listBrand);
		model.addAttribute("dataFilter", dataFilter);
		model.addAttribute("title", "Product Detail");

		return "admin/productdetail";
	}

	@PostMapping("/productDetail/export")
	public void exportCsv(HttpServletResponse response, @Validated ExportProductDetail exProduct) throws IOException {

		response.setContentType("text/csv; charset=UTF-8");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=ProductDetail_" + currentDateTime + ".csv";
		response.setHeader(headerKey, headerValue);

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = { "Sản phẩm", "Ram", "Bộ nhớ", "Màu", "Số lượng nhập", "Đã bán",
				"Hiện có", "Giá nhập", "Giá bán", "Ngày tạo" };
		String[] nameMapping = { "productDetailName", "ram", "internalMemory", "color", "quantityImport",
				"quantityExport", "available", "pricesImport", "prices", "createdDate" };

		csvWriter.writeHeader(csvHeader);
		for (ProductDetailDto prductDetail : exProduct.getListProductDetail()) {
			csvWriter.write(prductDetail, nameMapping);
		}

		csvWriter.close();

	}

	@GetMapping("/productDetail/importCsv")
	public String importFileCsv(@AuthenticationPrincipal UserDetailCustom userInfo, Model model) {
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("styleImport", Constants.IMPORTPROUCTDETAIL);
		return "admin/inputCSV";
	}

	@PostMapping("/productDetail/importCsv")
	public String importCsv(@RequestParam("csv") MultipartFile csvFile, HttpServletResponse response,
			@AuthenticationPrincipal UserDetailCustom userInfo, Model model) throws IOException {
		if (Constants.TYPEFILE.equals(csvFile.getContentType())
				|| csvFile.getContentType().equals("application/vnd.ms-excel")) {
			InputStream fileImport = csvFile.getInputStream();
			BufferedReader fileReader = new BufferedReader(new InputStreamReader(fileImport, "UTF-8"));
			CSVParser csvParser = new CSVParser(fileReader,
					CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

			List<ProductDetailDto> listProductDetail = new ArrayList<>();

			Iterable<CSVRecord> csvRecords = csvParser.getRecords();
			ProductDetailDto newProductDetail;
			ProductDto product;
			for (CSVRecord record : csvRecords) {
				newProductDetail = new ProductDetailDto();
				product = new ProductDto();

				product.setProductId(Long.parseLong(record.get("productCode")));
				newProductDetail.setProductInfo(product);
				newProductDetail.setProductDetailName(record.get("productName"));
				newProductDetail.setScreen(record.get("screen"));
				newProductDetail.setResolution(record.get("resolution"));
				newProductDetail.setOperatingSystem(record.get("operatingSystem"));
				newProductDetail.setRearCamera(record.get("rearCamera"));
				newProductDetail.setFrontCamera(record.get("frontCamera"));
				newProductDetail.setChips(record.get("chips"));
				newProductDetail.setRam(record.get("ram"));
				newProductDetail.setInternalMemory(record.get("internalMemory"));
				newProductDetail.setSim(record.get("sim"));
				newProductDetail.setRechargeableBatteries(record.get("rechargeableBatteries"));
				newProductDetail.setConnector(record.get("connector"));
				newProductDetail.setHeadphoneJack(record.get("headphoneJack"));
				newProductDetail.setMobileNetwork(record.get("mobileNetwork"));
				newProductDetail.setBluetooth(record.get("bluetooth"));
				newProductDetail.setWifi(record.get("wifi"));
				newProductDetail.setGps(record.get("gps"));
				newProductDetail.setPrices(Integer.parseInt(record.get("prices")));
				newProductDetail.setPricesImport(Integer.parseInt(record.get("pricesImport")));
				newProductDetail.setColor(record.get("color"));
				newProductDetail.setQuantityImport(Integer.parseInt(record.get("quantityImport")));
				newProductDetail.setDiscount(Integer.parseInt(record.get("discount")));
				newProductDetail.setCreatedBy(userInfo.getUsername());
				newProductDetail.setUpdatedBy(userInfo.getUsername());
				newProductDetail.setDeleteFlag(Constants.DELETE_FLAG);
				newProductDetail.setVote(5);

				listProductDetail.add(newProductDetail);
			}
			ArrayList<ProductDetailDto> listPr = new ArrayList<>();
			for (ProductDetailDto productDetail : listProductDetail) {
				listPr.add(productDetail);
			}

			ExportProductDetail exProduct = new ExportProductDetail();
			exProduct.setListProductDetail(listPr);

			model.addAttribute("title", "Load Import CSV");
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("exProduct", exProduct);
			return "admin/loadInputCSV";
		} else {
			model.addAttribute("userInfo", userInfo);
			return "admin/inputCSV";
		}

	}

	@PostMapping("productDetail/saveData")
	public String saveDataImport(@Validated ExportProductDetail exProduct) {

		productDetailService.saveData(exProduct.getListProductDetail());

		return "redirect:/adminPage/productDetail";
	}

	@GetMapping("/productDetail/create")
	public String createProductDetail(@AuthenticationPrincipal UserDetailCustom userInfo, Model model) {
		List<ProductDto> listProductDto = productService.getByProductType(Constants.TYPEPHONE);
		ProductDetailDto newProduct = new ProductDetailDto();
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("listProduct", listProductDto);
		model.addAttribute("newProductDetail", newProduct);
		model.addAttribute("title", "Create Product Detail");
		return "admin/addProductDetail";
	}

	@PostMapping("/productDetail/create")
	public String createdProductDetail(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@Validated ProductDetailDto productDetailDto) {
		ProductDto product = productService.getByProductId(productDetailDto.getProductInfo().getProductId());
		String uploadDir = "./src/main/resources/static/images/imagesProduct/" + product.getProductName();
		Path uploadpath = Paths.get(uploadDir);
		try {
			if (!Files.exists(uploadpath)) {
				Files.createDirectories(uploadpath);
			}
			String fileName;
			String newFileName;

			for (int i = 0; i < productDetailDto.getMultipartFiles().length; i++) {
				newFileName = null;
				fileName = productDetailDto.getMultipartFiles()[i].getOriginalFilename();
				if (!StringUtils.isEmptyOrWhitespace(fileName)) {
					if (i == 0) {
						newFileName = Constants.IMAGEOVERVIEW + fileName;
						productDetailDto.setImageOverview(newFileName);
					}
					if (i == 1) {
						newFileName = Constants.IMAGEUNDER + fileName;
						productDetailDto.setImageUnder(newFileName);
					}
					if (i == 2) {
						newFileName = Constants.IMAGESIDE + fileName;
						productDetailDto.setImageSide(newFileName);
					}
					if (i == 3) {
						newFileName = Constants.IMAGEOTHER + fileName;
						productDetailDto.setImageOther(newFileName);
					}
					MultipartFile files = productDetailDto.getMultipartFiles()[i];
					saveImage(files, uploadpath, newFileName);
				}

			}

			productDetailDto.setCreatedBy(userInfo.getUsername());
			productDetailDto.setUpdatedBy(userInfo.getUsername());

			productDetailService.createProductDetail(productDetailDto);

		} catch (IOException e) {

			e.printStackTrace();
		}

		return "redirect:/adminPage/productDetail";
	}

	@GetMapping("/productDetail/update")
	public String updateProductDetail(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@RequestParam("productDetailId") long productDetailId) {

		List<ProductDto> listProductDto = productService.getByProductType(Constants.TYPEPHONE);
		ProductDetailDto updateProductDetail = productDetailService.getProductDetailById(productDetailId);

		model.addAttribute("userInfo", userInfo);
		model.addAttribute("listProduct", listProductDto);
		model.addAttribute("updateProductDetail", updateProductDetail);
		model.addAttribute("title", "Update Product Detail");

		return "admin/updateProductDetail";
	}

	@PostMapping("/productDetail/update")
	public String updatedProductDetail(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@Validated ProductDetailDto productDetailDto) {

		ProductDto product = productService.getByProductId(productDetailDto.getProductInfo().getProductId());
		String uploadDir = "./src/main/resources/static/images/imagesProduct/" + product.getProductName();
		Path uploadpath = Paths.get(uploadDir);
		try {
			if (!Files.exists(uploadpath)) {
				Files.createDirectories(uploadpath);
			}
			String fileName;
			String newFileName;

			for (int i = 0; i < productDetailDto.getMultipartFiles().length; i++) {
				newFileName = null;
				fileName = productDetailDto.getMultipartFiles()[i].getOriginalFilename();
				if (!StringUtils.isEmptyOrWhitespace(fileName)) {
					if (i == 0) {
						newFileName = Constants.IMAGEOVERVIEW + fileName;
						productDetailDto.setImageOverview(newFileName);
					}
					if (i == 1) {
						newFileName = Constants.IMAGEUNDER + fileName;
						productDetailDto.setImageUnder(newFileName);
					}
					if (i == 2) {
						newFileName = Constants.IMAGESIDE + fileName;
						productDetailDto.setImageSide(newFileName);
					}
					if (i == 3) {
						newFileName = Constants.IMAGEOTHER + fileName;
						productDetailDto.setImageOther(newFileName);
					}
					MultipartFile files = productDetailDto.getMultipartFiles()[i];
					saveImage(files, uploadpath, newFileName);
				}

			}

			productDetailDto.setUpdatedBy(userInfo.getUsername());

			productDetailService.updateProductDetail(productDetailDto);

		} catch (IOException e) {

			e.printStackTrace();
		}

		return "redirect:/adminPage/productDetail";
	}

	@GetMapping("/productDetail/delete")
	public String deleteProductDetail(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@RequestParam("productDetailId") long productDetailId) {

		model.addAttribute("userInfo", userInfo);
		productDetailService.deleteProductDetail(productDetailId, userInfo.getUsername());
		return "redirect:/adminPage/productDetail";
	}

	@GetMapping("/productDetail/filter")
	public String filterProductDetail(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@Validated SearchProductDetailDto searchDto) {
		List<ProductDetailDto> listProductDetail = productDetailService.filterProductDetail(searchDto);
		List<BrandDto> listBrand = brandService.getAll();
		List<ProductDto> listProduct = productService.getByProductType(Constants.TYPEPHONE);
		DataFilteDto dataFilter = productDetailService.getDataFilter();

		ArrayList<ProductDetailDto> listPr = new ArrayList<>();
		for (ProductDetailDto productDetail : listProductDetail) {
			listPr.add(productDetail);
		}
		ExportProductDetail exProduct = new ExportProductDetail();
		exProduct.setListProductDetail(listPr);

		model.addAttribute("searchDto", searchDto);
		model.addAttribute("exProduct", exProduct);
		model.addAttribute("listProduct", listProduct);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("listBrand", listBrand);
		model.addAttribute("dataFilter", dataFilter);
		model.addAttribute("title", "Product Detail");

		return "admin/productdetail";
	}

	@GetMapping("/productDetail/search")
	public String searchProductDetail(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@RequestParam String keyWords) {
		List<ProductDetailDto> listProductDetail = productDetailService.searchProductDetail(keyWords);
		List<BrandDto> listBrand = brandService.getAll();
		List<ProductDto> listProduct = productService.getByProductType(Constants.TYPEPHONE);
		DataFilteDto dataFilter = productDetailService.getDataFilter();

		ArrayList<ProductDetailDto> listPr = new ArrayList<>();
		for (ProductDetailDto productDetail : listProductDetail) {
			listPr.add(productDetail);
		}
		ExportProductDetail exProduct = new ExportProductDetail();
		exProduct.setListProductDetail(listPr);

		model.addAttribute("searchDto", new SearchProductDetailDto());
		model.addAttribute("exProduct", exProduct);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("listProduct", listProduct);
		model.addAttribute("listBrand", listBrand);
		model.addAttribute("dataFilter", dataFilter);
		model.addAttribute("title", "Product Detail");

		return "admin/productdetail";
	}

	@GetMapping("/invoice")
	public String invoice(@AuthenticationPrincipal UserDetailCustom userInfo, Model model) {

		List<InvoiceDto> invoiceList = invoiceService.getAll();
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("listInvoice", invoiceList);
		model.addAttribute("searchDto", new SearchDateDto());
		return "admin/invoice";
	}

	@GetMapping("/invoice/delete")
	public String deleteInvoice(@AuthenticationPrincipal UserDetailCustom userInfo,
			@RequestParam("invoiceId") long invoiceId) {

		invoiceService.deleteInvoice(invoiceId, userInfo.getUsername());
		return "redirect:/adminPage/invoice";
	}

	@GetMapping("/invoice/update")
	public String updateInvoice(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@RequestParam("invoiceId") long invoiceId, HttpServletRequest request) {

		InvoiceDto invoiceUpdate = invoiceService.getInvoiceById(invoiceId);

		model.addAttribute("updateInvoice", invoiceUpdate);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("type", 1);
		request.getSession().setAttribute("deliveryTime", invoiceUpdate.getDeliveryTime());
		request.getSession().setAttribute("email", invoiceUpdate.getOrderInfo().get(0).getUserOrder().getEmail());

		return "admin/updateInvoice";
	}

	@GetMapping("/invoice/invoiceDetail")
	public String invoiceDetail(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@RequestParam("invoiceId") long invoiceId, HttpServletRequest request) {

		List<ProductOrderDto> invoiceUpdate = productOrderService.getInvoice(invoiceId);

		model.addAttribute("userInfo", userInfo);
		model.addAttribute("listInvoice", invoiceUpdate);
		return "admin/invoiceDetail";
	}

	@GetMapping("/invoice/updateDetail")
	public String updateInvoiceDetail(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@RequestParam("invoiceId") long invoiceId, HttpServletRequest request) {

		ProductOrderDto invoiceUpdate = productOrderService.getOrderById(invoiceId);

		model.addAttribute("updateInvoice", invoiceUpdate);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("type", 2);

		return "admin/updateInvoice";
	}

	@PostMapping("/invoice/updateDetail")
	public String updatedInvoiceDetail(@AuthenticationPrincipal UserDetailCustom userInfo,
			@Validated ProductOrderDto productOrder, HttpServletRequest request) {

		productOrderService.updateSatus(productOrder.getOrderId(), productOrder.getStatus(), userInfo.getUsername());

		return "redirect:/adminPage/invoice";

	}

	@PostMapping("/invoice/update")
	public String updatedInvoice(@AuthenticationPrincipal UserDetailCustom userInfo, @Validated InvoiceDto invoiceDto,
			HttpServletRequest request) {

		invoiceDto.setUpdatedBy(userInfo.getUsername());
		String deliveryTime = (String) request.getSession().getAttribute("deliveryTime");
		if (!deliveryTime.equals(invoiceDto.getDeliveryTime())) {
			String email = (String) request.getSession().getAttribute("email");
			SimpleMailMessage message = new SimpleMailMessage();

			message.setTo(email);
			// message.setTo("hieu1998hdtx@gmail.com");

			message.setSubject("Thay đổi thời gian giao hàng");
			message.setText("Chào bạn, do việc giao hàng gặp sự cố vì vậy đơn hàng của bạn sẽ được giao vào ngày  "
					+ invoiceDto.getDeliveryTime() + " Mong bạn thông cảm về sự bất tiện này");

			this.emailSender.send(message);
		}
		InvoiceDto invoice = invoiceService.updateInvocie(invoiceDto);
		if (invoice != null) {
			return "redirect:/adminPage/invoice";
		} else {
			return "redirect:/adminPage/update?invoiceId=" + invoiceDto.getInvoiceId();
		}

	}

	@GetMapping("invoice/search")
	public String searchInvoice(@AuthenticationPrincipal UserDetailCustom userInfo,
			@RequestParam("keyWords") String keyWords, Model model) {

		List<InvoiceDto> listInvoice = invoiceService.getByKeyWords(keyWords);

		model.addAttribute("userInfo", userInfo);
		model.addAttribute("listInvoice", listInvoice);
		model.addAttribute("searchDto", new SearchDateDto());
		return "admin/invoice";
	}

	@GetMapping("invoice/filter")
	public String filterInvoice(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@Validated SearchDateDto searchDto) {

		List<InvoiceDto> listInvoice = invoiceService.filterInvoice(searchDto);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("listInvoice", listInvoice);
		model.addAttribute("searchDto", searchDto);
		return "admin/invoice";
	}

	private void saveImage(MultipartFile fileUpload, Path uploadpath, String fileName) {

		try {
			InputStream inputStream = fileUpload.getInputStream();
			Path filePath = uploadpath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@GetMapping("/report/productDetail")
	public String exportProduct(@AuthenticationPrincipal UserDetailCustom userInfo, Model model) {
		Date dateNow = new Date();
		Calendar cal = Calendar.getInstance();

		cal.setTime(dateNow);
		int enndMonth = cal.get(Calendar.MONTH) + 1;
		int startMont = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);

		String startDate = String.valueOf(year) + "-" + String.valueOf(startMont) + "-01";
		String endDate = String.valueOf(year) + "-" + String.valueOf(enndMonth) + "-01";

		SearchDateDto searchDto = new SearchDateDto();
		searchDto.setEndDate(endDate);
		searchDto.setStartDate(startDate);
		model.addAttribute("searchDto", searchDto);
		model.addAttribute("userInfo", userInfo);
		
		return "admin/reportProduct";
	}
    @PostMapping("/report/outputCsv")
    public void outPutReportCsv(@AuthenticationPrincipal UserDetailCustom userInfo, @Validated SearchDateDto searchDto, HttpServletResponse response) throws IOException {
    	response.setContentType("text/csv; charset=UTF-8");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=Sales_" + currentDateTime + ".csv";
		response.setHeader(headerKey, headerValue);

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = { "Sản phẩm", "Doanh số"};
		String[] nameMapping = { "productName","sales"};

		csvWriter.writeHeader(csvHeader);
		
		List<ProductDto> listProduct = new ArrayList<>();
		
		 ReportDto reportDto = productOrderService.getDataReport(searchDto);
		ProductDto product ;
		for ( Entry<String, Integer> entry : reportDto.getSales().entrySet()) {
			product = new ProductDto();
			product.setProductName(entry.getKey());
			product.setSales(entry.getValue());
		    
			listProduct.add(product);
		}

		for (ProductDto productDto : listProduct) {
			csvWriter.write(productDto, nameMapping);
		}

		csvWriter.close();
    }
    
    @PostMapping("/report/productDetail")
	public String searchExport(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@Validated SearchDateDto searchDto) {
		
		  ReportDto reportDto = productOrderService.getDataReport(searchDto);
		  
		  model.addAttribute("searchDto", searchDto);
		  model.addAttribute("chartDataSales", reportDto.getSales());
		  model.addAttribute("userInfo", userInfo);
		 
		return "admin/reportProduct";
	}
	@GetMapping("/reportRevenue/productDetail")
	public String exportRevenueProduct(@AuthenticationPrincipal UserDetailCustom userInfo, Model model) {

		Date dateNow = new Date();
		Calendar cal = Calendar.getInstance();

		cal.setTime(dateNow);
		int enndMonth = cal.get(Calendar.MONTH) + 1;
		int startMont = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);

		String startDate = String.valueOf(year) + "-" + String.valueOf(startMont) + "-01";
		String endDate = String.valueOf(year) + "-" + String.valueOf(enndMonth) + "-01";

		SearchDateDto searchDto = new SearchDateDto();
		searchDto.setEndDate(endDate);
		searchDto.setStartDate(startDate);
		model.addAttribute("searchDto", searchDto);
		model.addAttribute("userInfo", userInfo);
		
		return "admin/reportRevenue";
	}

	

	@PostMapping("/reportRevenue/productDetail")
	public String searchRevenueExport(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@Validated SearchDateDto searchDto) {

		  ReportDto reportDto = productOrderService.getDataReport(searchDto);
		  
		  model.addAttribute("searchDto", searchDto);
		  model.addAttribute("chartDataSales", reportDto.getRevenue());
		  model.addAttribute("userInfo", userInfo);
		return "admin/reportRevenue";
	}

	@PostMapping("/reportRevenue/outputCsv")
    public void outPutReportRevenueCsv(@AuthenticationPrincipal UserDetailCustom userInfo, @Validated SearchDateDto searchDto, HttpServletResponse response) throws IOException {
    	response.setContentType("text/csv; charset=UTF-8");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=Revenue_" + currentDateTime + ".csv";
		response.setHeader(headerKey, headerValue);

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = { "Sản phẩm", "Doanh Thu"};
		String[] nameMapping = { "productName","revenue"};

		csvWriter.writeHeader(csvHeader);
		
		List<ProductDto> listProduct = new ArrayList<>();
		
		 ReportDto reportDto = productOrderService.getDataReport(searchDto);
		ProductDto product ;
		for ( Entry<String, Integer> entry : reportDto.getRevenue().entrySet()) {
			product = new ProductDto();
			product.setProductName(entry.getKey());
			product.setRevenue(entry.getValue());
		    
			listProduct.add(product);
		}

		for (ProductDto productDto : listProduct) {
			csvWriter.write(productDto, nameMapping);
		}

		csvWriter.close();
    }

	@GetMapping("/contact")
	public String contact(@AuthenticationPrincipal UserDetailCustom userInfo, Model model) {
		List<CustomerContactDto> listContact = contactService.getAll();
		model.addAttribute("listContact", listContact);
		model.addAttribute("userInfo", userInfo);
		return "admin/contact";
	}

	@GetMapping("/contact/search")
	public String searchContact(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@RequestParam("keyWords") String keyWords) {
		List<CustomerContactDto> listContact = contactService.findByKeyWords(keyWords);
		model.addAttribute("listContact", listContact);
		model.addAttribute("userInfo", userInfo);
		return "admin/contact";
	}

	@GetMapping("/contact/delete")
	public String deleteContact(@AuthenticationPrincipal UserDetailCustom userInfo,
			@RequestParam("contactId") long contactId) {

		contactService.delete(contactId, userInfo.getUsername());
		return "redirect:/adminPage/contact";
	}

	@GetMapping("/headPhone")
	public String headPhone(@AuthenticationPrincipal UserDetailCustom userInfo, Model model) {
		ExportProductDetail exProduct = new ExportProductDetail();
		ArrayList<HeadPhoneDetailDto> listHeadPhoneEx = new ArrayList<>();
		List<HeadPhoneDetailDto> listHeadPhone = headPhoneService.getAll();

		for (HeadPhoneDetailDto headPhone : listHeadPhone) {
			listHeadPhoneEx.add(headPhone);
		}

		exProduct.setListHeadPhone(listHeadPhoneEx);
		List<BrandDto> listBrand = brandService.getAll();
		List<ProductDto> listProduct = productService.getByProductType(Constants.TYPEHEADPHONE);

		model.addAttribute("listProduct", listProduct);
		model.addAttribute("listBrand", listBrand);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("exProduct", exProduct);
		model.addAttribute("title", "HeadPhone");
		model.addAttribute("searchDto", new SearchProductDetailDto());
		return "admin/headPhone";
	}

	@PostMapping("/headPhone/export")
	public void exportCsvHeadPhone(HttpServletResponse response, @Validated ExportProductDetail exProduct)
			throws IOException {

		response.setContentType("text/csv; charset=UTF-8");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=HeadPhone_" + currentDateTime + ".csv";
		response.setHeader(headerKey, headerValue);

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = { "Sản phẩm", "Màu", "Số lượng nhập", "Đã bán", "Hiện có",
				"Giá nhập", "Giá bán" };
		String[] nameMapping = { "headPhoneName", "color", "quantityImport", "quantityExport", "available",
				"pricesImport", "prices" };

		csvWriter.writeHeader(csvHeader);
		for (HeadPhoneDetailDto headPhone : exProduct.getListHeadPhone()) {
			csvWriter.write(headPhone, nameMapping);
		}

		csvWriter.close();

	}

	@GetMapping("/headPhone/importCsv")
	public String importFileCsvHeadPhone(@AuthenticationPrincipal UserDetailCustom userInfo, Model model) {
		model.addAttribute("userInfo", userInfo);

		model.addAttribute("styleImport", Constants.IMPORTHEADPHONE);
		return "admin/inputCSV";
	}

	@PostMapping("/headPhone/importCsv")
	public String importCsvHeadPhone(@RequestParam("csv") MultipartFile csvFile, HttpServletResponse response,
			@AuthenticationPrincipal UserDetailCustom userInfo, Model model) throws IOException {
		if (Constants.TYPEFILE.equals(csvFile.getContentType())
				|| csvFile.getContentType().equals("application/vnd.ms-excel")) {
			InputStream fileImport = csvFile.getInputStream();
			BufferedReader fileReader = new BufferedReader(new InputStreamReader(fileImport, "UTF-8"));
			CSVParser csvParser = new CSVParser(fileReader,
					CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

			List<HeadPhoneDetailDto> listHeadPhone = new ArrayList<>();

			Iterable<CSVRecord> csvRecords = csvParser.getRecords();
			HeadPhoneDetailDto newHeadPhone;
			ProductDto product;
			for (CSVRecord record : csvRecords) {
				newHeadPhone = new HeadPhoneDetailDto();
				product = new ProductDto();

				product.setProductId(Long.parseLong(record.get("productCode")));
				newHeadPhone.setProduct(product);
				newHeadPhone.setHeadPhoneName(record.get("productName"));
				newHeadPhone.setHeadphoneUsageTime(record.get("headphoneUsageTime"));
				newHeadPhone.setChargingBoxUsageTime(record.get("chargingBoxUsageTime"));
				newHeadPhone.setChargingPort(record.get("chargingPort"));
				newHeadPhone.setAudioTechnology(record.get("audioTechnology"));
				newHeadPhone.setCompatible(record.get("compatible"));
				newHeadPhone.setConnectApplication(record.get("connectApplication"));
				newHeadPhone.setUtilities(record.get("utilities"));
				newHeadPhone.setConnectSameTime(record.get("connectSameTime"));
				newHeadPhone.setConnectTechnology(record.get("connectTechnology"));
				newHeadPhone.setControl(record.get("control"));

				newHeadPhone.setPrices(Integer.parseInt(record.get("prices")));
				newHeadPhone.setPricesImport(Integer.parseInt(record.get("pricesImport")));
				newHeadPhone.setColor(record.get("color"));
				newHeadPhone.setQuantityImport(Integer.parseInt(record.get("quantityImport")));
				newHeadPhone.setDiscount(Integer.parseInt(record.get("discount")));
				newHeadPhone.setCreatedBy(userInfo.getUsername());
				newHeadPhone.setUpdatedBy(userInfo.getUsername());
				newHeadPhone.setDeleteFlag(Constants.DELETE_FLAG);

				listHeadPhone.add(newHeadPhone);
			}
			ArrayList<HeadPhoneDetailDto> listPr = new ArrayList<>();
			for (HeadPhoneDetailDto headPhone : listHeadPhone) {
				listPr.add(headPhone);
			}

			ExportProductDetail exProduct = new ExportProductDetail();
			exProduct.setListHeadPhone(listPr);

			model.addAttribute("title", "Load Import CSV");
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("exProduct", exProduct);
			return "admin/loadImportCsv";
		} else {
			model.addAttribute("userInfo", userInfo);
			return "admin/inputCSV";
		}

	}

	@PostMapping("headPhone/saveData")
	public String saveHeadPhoneDataImport(@Validated ExportProductDetail exProduct) {

		headPhoneService.saveData(exProduct.getListHeadPhone());

		return "redirect:/adminPage/headPhone";
	}

	@GetMapping("/headPhone/create")
	public String createHeadPhone(@AuthenticationPrincipal UserDetailCustom userInfo, Model model) {

		HeadPhoneDetailDto newHeadPhone = new HeadPhoneDetailDto();

		List<ProductDto> listProduct = productService.getByProductType(Constants.TYPEHEADPHONE);

		model.addAttribute("listProduct", listProduct);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("newHeadPhone", newHeadPhone);
		model.addAttribute("title", "Create Product Detail");

		return "admin/addHeadPhone";
	}

	@PostMapping("/headPhone/create")
	public String createdHeadPhone(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@Validated HeadPhoneDetailDto headPhoneDto) {
		ProductDto product = productService.getByProductId(headPhoneDto.getProduct().getProductId());
		String uploadDir = "./src/main/resources/static/images/imagesProduct/" + product.getProductName();
		Path uploadpath = Paths.get(uploadDir);
		try {
			if (!Files.exists(uploadpath)) {
				Files.createDirectories(uploadpath);
			}
			String fileName;
			String newFileName;

			for (int i = 0; i < headPhoneDto.getMultipartFiles().length; i++) {
				newFileName = null;
				fileName = headPhoneDto.getMultipartFiles()[i].getOriginalFilename();
				if (!StringUtils.isEmptyOrWhitespace(fileName)) {
					if (i == 0) {
						newFileName = Constants.IMAGEOVERVIEW + fileName;
						headPhoneDto.setImageOverView(newFileName);
					}
					if (i == 1) {
						newFileName = Constants.IMAGEUNDER + fileName;
						headPhoneDto.setImageUnder(newFileName);
					}
					if (i == 2) {
						newFileName = Constants.IMAGESIDE + fileName;
						headPhoneDto.setImageSide(newFileName);
					}
					if (i == 3) {
						newFileName = Constants.IMAGEOTHER + fileName;
						headPhoneDto.setImageOther(newFileName);
					}

					MultipartFile files = headPhoneDto.getMultipartFiles()[i];
					saveImage(files, uploadpath, newFileName);
				}

			}

			headPhoneDto.setCreatedBy(userInfo.getUsername());
			headPhoneDto.setUpdatedBy(userInfo.getUsername());

			headPhoneService.createHeadPhone(headPhoneDto);

		} catch (IOException e) {

			e.printStackTrace();
		}

		return "redirect:/adminPage/headPhone";
	}

	@GetMapping("/headPhone/update")
	public String updateHeadPhone(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@RequestParam("headPhoneId") long headPhoneId) {

		HeadPhoneDetailDto updateHeadPhone = headPhoneService.getById(headPhoneId);

		List<ProductDto> listProduct = productService.getByProductType(Constants.TYPEHEADPHONE);

		model.addAttribute("userInfo", userInfo);
		model.addAttribute("listProduct", listProduct);
		model.addAttribute("updateHeadPhone", updateHeadPhone);
		model.addAttribute("title", "Update Product Detail");

		return "admin/updateHeadPhone";
	}

	@PostMapping("/headPhone/update")
	public String updatedHeadPhone(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@Validated HeadPhoneDetailDto headPhoneDto) {

		ProductDto product = productService.getByProductId(headPhoneDto.getProduct().getProductId());
		String uploadDir = "./src/main/resources/static/images/imagesProduct/" + product.getProductName();
		Path uploadpath = Paths.get(uploadDir);
		try {
			if (!Files.exists(uploadpath)) {
				Files.createDirectories(uploadpath);
			}
			String fileName;
			String newFileName;

			for (int i = 0; i < headPhoneDto.getMultipartFiles().length; i++) {
				newFileName = null;
				fileName = headPhoneDto.getMultipartFiles()[i].getOriginalFilename();
				if (!StringUtils.isEmptyOrWhitespace(fileName)) {
					if (i == 0) {
						newFileName = Constants.IMAGEOVERVIEW + fileName;
						headPhoneDto.setImageOverView(newFileName);
					}
					if (i == 1) {
						newFileName = Constants.IMAGEUNDER + fileName;
						headPhoneDto.setImageUnder(newFileName);
					}
					if (i == 2) {
						newFileName = Constants.IMAGESIDE + fileName;
						headPhoneDto.setImageSide(newFileName);
					}
					if (i == 3) {
						newFileName = Constants.IMAGEOTHER + fileName;
						headPhoneDto.setImageOther(newFileName);
					}

					MultipartFile files = headPhoneDto.getMultipartFiles()[i];
					saveImage(files, uploadpath, newFileName);
				}

			}

			headPhoneDto.setUpdatedBy(userInfo.getUsername());

			headPhoneService.updateHeadPhone(headPhoneDto);

		} catch (IOException e) {

			e.printStackTrace();
		}

		return "redirect:/adminPage/headPhone";
	}

	@GetMapping("/headPhone/delete")
	public String deleteHeadPhone(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@RequestParam("headPhoneId") long headPhoneId) {

		model.addAttribute("userInfo", userInfo);
		headPhoneService.deleteHeadPhone(headPhoneId, userInfo.getUsername());
		return "redirect:/adminPage/headPhone";
	}

	@GetMapping("/headPhone/filter")

	public String filterHeadPhone(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,

			@Validated SearchProductDetailDto searchDto) {
		List<HeadPhoneDetailDto> listProductDetail = headPhoneService.filterHeadPhoneDetail(searchDto);
		List<BrandDto> listBrand = brandService.getAll();
		List<ProductDto> listProduct = productService.getByProductType(Constants.TYPEHEADPHONE);

		ArrayList<HeadPhoneDetailDto> listPr = new ArrayList<>();
		for (HeadPhoneDetailDto headPhone : listProductDetail) {
			listPr.add(headPhone);
		}
		ExportProductDetail exProduct = new ExportProductDetail();
		exProduct.setListHeadPhone(listPr);

		model.addAttribute("searchDto", searchDto);
		model.addAttribute("exProduct", exProduct);
		model.addAttribute("listProduct", listProduct);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("listBrand", listBrand);

		model.addAttribute("title", "Product Detail");

		return "admin/headPhone";
	}

	@GetMapping("/headPhone/search")
	public String searchHeadPhone(@AuthenticationPrincipal UserDetailCustom userInfo, Model model,
			@RequestParam String keyWords) {
		List<HeadPhoneDetailDto> listHeadPhone = headPhoneService.searchHeadPhone(keyWords);
		List<BrandDto> listBrand = brandService.getAll();
		List<ProductDto> listProduct = productService.getByProductType(Constants.TYPEHEADPHONE);

		ArrayList<HeadPhoneDetailDto> listPr = new ArrayList<>();
		for (HeadPhoneDetailDto headPhone : listHeadPhone) {
			listPr.add(headPhone);
		}
		ExportProductDetail exProduct = new ExportProductDetail();
		exProduct.setListHeadPhone(listPr);

		model.addAttribute("searchDto", new SearchProductDetailDto());
		model.addAttribute("exProduct", exProduct);
		model.addAttribute("listProduct", listProduct);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("listBrand", listBrand);

		model.addAttribute("title", "Product Detail");

		return "admin/headPhone";
	}
}
