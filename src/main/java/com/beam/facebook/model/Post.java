package com.beam.facebook.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Accessors(chain = true)
@Document(collection = "Post")
@TypeAlias("Post")
public class Post extends Base{

    private String content;
    @DBRef
    private User user;
    private Date created = new Date();
}
