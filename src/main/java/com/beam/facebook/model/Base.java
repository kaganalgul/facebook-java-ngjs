package com.beam.facebook.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Base {

    private String id = UUID.randomUUID().toString();

}
