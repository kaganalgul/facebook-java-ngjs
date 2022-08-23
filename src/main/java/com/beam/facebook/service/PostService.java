package com.beam.facebook.service;

import com.beam.facebook.dto.SharePostRequest;
import com.beam.facebook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.beam.facebook.model.Post;
import com.beam.facebook.model.User;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.beam.facebook.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final SimpMessagingTemplate template;

    public void share(SharePostRequest request, User user) {
        Post newPost = new Post()
                .setContent(request.getContent())
                .setUser(user);
        template.convertAndSend("/post/list/" + user.getId(), newPost);
        postRepository.save(newPost);
        for (String u : user.getFriends()) {
            Optional<User> optUser = userRepository.findByEmail(u);
            User subUser = optUser.get();
            template.convertAndSend("/post/list/" + subUser.getId(), newPost);
        }
    }

    public void delete(String postId, String userId) {
        Post deletedPost = postRepository.findByIdAndUserId(postId, userId);
        if (deletedPost != null) {
            postRepository.delete(deletedPost);
        }
    }

    public List<Post> list(User user) {
        List<Post> posts = postRepository.findByUser(user);
        Set<String> friendList = user.getFriends();

        for (String u : friendList) {
            Optional<User> optUser = userRepository.findByEmail(u);
            User addedUser = optUser.get();
            posts.addAll(postRepository.findByUser(addedUser));
        }

        return posts;
    }
}
