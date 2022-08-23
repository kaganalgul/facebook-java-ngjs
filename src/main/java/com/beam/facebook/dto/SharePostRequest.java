package com.beam.facebook.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SharePostRequest {
    private String content;
}
