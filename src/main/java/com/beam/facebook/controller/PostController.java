package com.beam.facebook.controller;

import com.beam.facebook.dto.SharePostRequest;
import com.beam.facebook.model.Post;
import com.beam.facebook.model.User;
import com.beam.facebook.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import java.util.List;

import static com.beam.facebook.service.UserService.SESSION_ACCOUNT;

@RestController
@RequiredArgsConstructor
@RequestMapping("post")
public class PostController {

    private final PostService postService;

    @PostMapping("share")
    public void share(@RequestBody SharePostRequest request, HttpSession session) {
        User user = (User) session.getAttribute(SESSION_ACCOUNT);

        postService.share(request, user);
    }

    @GetMapping("list")
    public List<Post> list(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_ACCOUNT);
        return postService.list(user);
    }
}
