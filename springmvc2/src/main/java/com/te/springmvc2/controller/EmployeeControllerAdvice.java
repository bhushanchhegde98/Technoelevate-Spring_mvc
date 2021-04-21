package com.te.springmvc2.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.te.springmvc2.customexception.EmployeeExp;

@ControllerAdvice
public class EmployeeControllerAdvice {
	
	@ExceptionHandler(EmployeeExp.class)
	public String handleExp(EmployeeExp exp, HttpServletRequest request) {
		request.setAttribute("err", exp.getMessage());
		return "errPage";
		
	}

}
