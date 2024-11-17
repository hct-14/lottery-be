package com.example.lottery.DAO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RestResponse<T> {
    private int statusCode;
    private String error;
    private Object message; // Can be String or ArrayList
    private T data;

    // Getters and setters for all fields


}
