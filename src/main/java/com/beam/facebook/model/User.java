package com.beam.facebook.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@Accessors(chain = true)
@Document(collection = "User")
@TypeAlias("User")
public class User extends Base{

    private String name;
    private String surname;
    private String email;
    private String password;
    private Set<String> friends = new HashSet<>(); //hata verirse email ile tut
    @DBRef
    private Set<User> friendRequests = new HashSet<>();
}
