package com.beam.facebook.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

import static com.beam.facebook.service.UserService.SESSION_ACCOUNT;

@Controller
public class PageController {

    @GetMapping(value = {"/home"})
    public String home(HttpSession session) {
        if (session.getAttribute(SESSION_ACCOUNT) == null) {
            return "redirect:/login";
        } else {
            return "index";
        }
    }

    @GetMapping(value = {"/", "/login", "/register"}, produces = {MediaType.TEXT_HTML_VALUE})
    public String login() {
        return "index";
    }
}
