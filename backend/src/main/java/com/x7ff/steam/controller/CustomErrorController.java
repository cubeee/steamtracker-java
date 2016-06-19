package com.x7ff.steam.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public final class CustomErrorController implements ErrorController {
	private static final String PATH = "/error";

	@RequestMapping(path = PATH, method = { RequestMethod.GET, RequestMethod.POST })
	public String show(HttpServletResponse response, Model model) {
		int status = response.getStatus();
		model.addAttribute("status", status);
		return getTemplateForStatus(status);
	}

	@Override
	public String getErrorPath() {
		return PATH;
	}

	private String getTemplateForStatus(int status) {
		String template;
		switch (HttpStatus.valueOf(status)) {
			case NOT_FOUND:
				template = "404";
				break;

			default:
				template = "error";
				break;
		}
		return "errors/" + template;
	}

}