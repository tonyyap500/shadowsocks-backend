package com.shadowsocks.config;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Profile(value = {"dev", "sit"})
@Controller
public class HomeController {
	@RequestMapping(value = "/")
	public String index() {
		return "redirect:swagger-ui.html";
	}
}
