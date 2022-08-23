package com.beam.facebook.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import com.beam.facebook.model.User;

@Data
@Accessors(chain = true)
public class AuthenticationResponse {

    /**
     *  0 = ok,
     *  1 = password incorrect,
     *  -1 = user not found
     * */
    private int code;
    private User user;
}
