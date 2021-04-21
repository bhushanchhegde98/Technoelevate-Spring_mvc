package com.te.springmvc2.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.te.springmvc2.bean.EmployeeBean;
import com.te.springmvc2.dao.EmployeeDAO;
import com.te.springmvc2.service.EmployeeService;

@Controller
public class EmployeeController {

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		CustomDateEditor dateEditor = new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true);
		binder.registerCustomEditor(Date.class, dateEditor);
	}

	@Autowired
	private EmployeeService service;

	@GetMapping("empLogin")
	public String getLoginPage() {
		return "login";

	}

	@PostMapping("empLogin")
	public String getEmpDetails(int id, String password, HttpServletRequest request, ModelMap map) {

		EmployeeBean employeeBean = service.authenticate(id, password);
		if (employeeBean != null) {
			HttpSession httpSession = request.getSession(true);
			httpSession.setAttribute("emp", employeeBean);
			return "homePage";
		} else {
			map.addAttribute("errMsg", "Invalid Credentials");
			return "login";
		}

	}

	@GetMapping("searchForm")
	public String getSearchForm(ModelMap map, HttpSession session) {
		if (session.getAttribute("emp") != null) {
			return "search";

		} else {
			map.addAttribute("errMsg", "please login first");
			return "login";
		}
	}

	@GetMapping("search")
	public String searchEmp(int id, ModelMap map,
			@SessionAttribute(name = "emp", required = false) EmployeeBean employeeBean) {

		if (employeeBean != null) {
			EmployeeBean employeeBean2 = service.searchemp(id);
			if (employeeBean2 != null) {
				map.addAttribute("data", employeeBean2);

			} else {
				map.addAttribute("msg", "Data not found for id :" + id);

			}
			return "search";
		} else {
			map.addAttribute("errMsg", "please login first");
			return "login";
		}

	}

	@GetMapping("getdeleteform")
	public String getDeleteForm(ModelMap map,
			@SessionAttribute(name = "emp", required = false) EmployeeBean employeeBean) {
		if (employeeBean != null) {
			return "delete";
		} else {
			map.addAttribute("errMsg", "please login first");
			return "login";

		}

	}

	@GetMapping("udelete")
	public String deleteEmployee(int id, @SessionAttribute(name = "emp", required = false) EmployeeBean bean,
			ModelMap map) {
		if (bean != null) {
			boolean deleted = service.deleteEmp(id);
			if (deleted == true) {
				map.addAttribute("msg", "deleted successfully");
				return "delete";
			} else {
				map.addAttribute("errMsg", "user not found");
				return "delete";
			}

		}
		return null;
	}

	@GetMapping("viewall")
	public String viewAllEmployee(ModelMap map, @SessionAttribute(name = "emp", required = false) EmployeeBean bean) {
		if (bean != null) {
			List<EmployeeBean> employeeBeans = service.getAllData();
			map.addAttribute("empdata", employeeBeans);
			return "allData";
		} else {
			map.addAttribute("msg", "no employees found");
			return "allData";
		}

	}

	@GetMapping("addForm")
	public String addEmployee(ModelMap map, @SessionAttribute(name = "emp", required = false) EmployeeBean bean) {
		if (bean != null) {
			return "register";
		} else {
			map.addAttribute("msg", "please login first");
			return "login";
		}

	}

	@PostMapping("add")
	public String addemp(ModelMap map, @SessionAttribute(name = "emp", required = false) EmployeeBean bean,
			EmployeeBean empdata) {
		if (bean != null) {
			if (bean != null) {
				if (service.addemp(empdata)) {
					map.addAttribute("msg", "added successfully");
					return "register";
				} else {
					map.addAttribute("errMsg", "something went wrong");
					return "register";
				}
			}

		}
		return "login";

	}

	@GetMapping("/updateForm")
	public String updateEmployee(@SessionAttribute(name = "emp", required = false) EmployeeBean bean, ModelMap map) {
		if (bean != null) {
			map.addAttribute("id", bean.getId());
			return "update";
		} else {
			map.addAttribute("errMsg", "please login first");
			return "login";
		}
	}

	@PostMapping("/update")
	public String updateEmployee(@SessionAttribute(name = "emp", required = false) EmployeeBean employeeBean,
			ModelMap map, EmployeeBean empData) {
		if (employeeBean != null) {
			if (service.update(empData)) {
				map.addAttribute("id", employeeBean.getId());
				map.addAttribute("msg", "updated successfully");
				return "update";
			} else {
				map.addAttribute("errMsg", "something went wrong");
				return "update";
			}
		} else {
			map.addAttribute("msg", "please login first");
			return "login";

		}

	}

	@GetMapping("logout")
	public String logOut(HttpSession session, ModelMap map) {
		session.invalidate();
		map.addAttribute("msg", "Logout Successfull");
		return "login";

	}

}
