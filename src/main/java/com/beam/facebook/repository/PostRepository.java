package com.beam.facebook.repository;

import com.beam.facebook.model.Post;
import com.beam.facebook.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {

    public Post findByIdAndUserId(String fileId, String userId);

    public List<Post> findByUser(User user);
}
