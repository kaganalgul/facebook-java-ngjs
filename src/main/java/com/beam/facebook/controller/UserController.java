package com.beam.facebook.controller;

import com.beam.facebook.dto.AuthenticationRequest;
import com.beam.facebook.dto.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import com.beam.facebook.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.beam.facebook.service.UserService;

import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.beam.facebook.service.UserService.SESSION_ACCOUNT;

@RequiredArgsConstructor
@Controller
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    @ResponseBody
    @PostMapping("login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request, HttpSession session) {
        AuthenticationResponse response = userService.login(request);

        if (response.getCode() == 0) {
            session.setAttribute(SESSION_ACCOUNT, response.getUser());
        }

        return response;
    }

    @GetMapping("logout")
    public String logout(HttpSession session) {
        session.removeAttribute(SESSION_ACCOUNT);
        return "redirect:/login";
    }

    @ResponseBody
    @PostMapping("register")
    public void register(@RequestBody User user) {
        userService.register(user);
    }

    @ResponseBody
    @PostMapping("send-friend-request")
    public void sendFriendRequest(@RequestBody String receiverEmail, HttpSession session) {
        User sender = (User) session.getAttribute(SESSION_ACCOUNT);
        userService.sendFriendRequest(sender, receiverEmail);
    }

    @ResponseBody
    @PostMapping("send-friend-response")
    public void sendFriendResponse(@RequestBody String receiverEmail, HttpSession session) {
        User sender = (User) session.getAttribute(SESSION_ACCOUNT);
        userService.sendFriendResponse(sender, receiverEmail);
    }

    @ResponseBody
    @GetMapping("request-list")
    public Set<User> requestList(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_ACCOUNT);
        Set<User> users = userService.requestList(user.getId());
        return users;
    }

    @ResponseBody
    @PostMapping("accept/{id}")
    public void accept(@PathVariable String id, HttpSession session) {
        userService.accept(id, session);
    }

    @ResponseBody
    @PostMapping("reject/{id}")
    public void reject(@PathVariable String id, HttpSession session) {
        User user = (User) session.getAttribute(SESSION_ACCOUNT);
        userService.reject(id, user);
    }

    @ResponseBody
    @GetMapping("/friend-list")
    public List<User> friendList(HttpSession session) {
        return userService.friendList(session);
    }
}
