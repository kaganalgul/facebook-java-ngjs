package com.beam.facebook.service;

import com.beam.facebook.dto.AuthenticationRequest;
import com.beam.facebook.dto.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import com.beam.facebook.model.User;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.beam.facebook.repository.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserService {

    public static final String SESSION_ACCOUNT = "user";

    private final PasswordEncoder passwordEncoder;

    private final SimpMessagingTemplate template;

    private final UserRepository userRepository;

    public AuthenticationResponse login(AuthenticationRequest request) {

        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        AuthenticationResponse response = new AuthenticationResponse();

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                response.setCode(0)
                        .setUser(user);

                return response;
            } else {
                response.setCode(1);

                return response;
            }
        } else {
            response.setCode(-1);
            return response;
        }
    }

    public void register(User user) {
        if (!userRepository.findByEmail(user.getEmail()).isPresent()) {
            User newUser = new User()
                    .setName(user.getName())
                    .setSurname(user.getSurname())
                    .setEmail(user.getEmail())
                    .setPassword(passwordEncoder.encode(user.getPassword()));

            userRepository.save(newUser);
        }
    }

    public void sendFriendRequest(User sender, String receiverEmail) {
        Optional<User> optUser = userRepository.findByEmail(receiverEmail);
        User receiver = optUser.get();
        receiver.getFriendRequests().add(sender);
        template.convertAndSend("/user/send-friend-request/" + receiver.getId(), receiver);
        userRepository.save(receiver);
    }

    public void sendFriendResponse(User sender, String receiverEmail) {
        Optional<User> optUser = userRepository.findByEmail(receiverEmail);
        User receiver = optUser.get();
        sender.getFriendRequests().remove(receiver);
        userRepository.save(sender);
    }

    public Set<User> requestList(String userId) {
        Optional<User> optUser = userRepository.findById(userId);
        User user = optUser.get();

        return user.getFriendRequests();
    }

    public void accept(String id, HttpSession session) {
        User user = (User) session.getAttribute(SESSION_ACCOUNT);
        Optional<User> optUser = userRepository.findById(id);
        User addedUser = optUser.get();
        user.getFriendRequests().remove(addedUser);
        addedUser.getFriends().add(user.getEmail());
        user.getFriends().add(addedUser.getEmail());
        userRepository.save(addedUser);
        userRepository.save(user);
    }

    public void reject(String id, User user) {
        Optional<User> optUser = userRepository.findById(id);
        User rejectUser = optUser.get();
        user.getFriendRequests().remove(rejectUser);
        userRepository.save(user);
    }

    public List<User> friendList(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_ACCOUNT);
        Set<String> uMails = user.getFriends();
        List<User> users = new ArrayList<>();
        for (String um : uMails) {
            Optional<User> optUser = userRepository.findByEmail(um);
            User u = optUser.get();
            users.add(u);
        }
        return users;
    }
}
