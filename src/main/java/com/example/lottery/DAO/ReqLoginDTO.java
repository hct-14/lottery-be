package com.example.lottery.DAO;

import jakarta.validation.constraints.NotBlank;

public class ReqLoginDTO {
    @NotBlank(message = " user khong duoc de trong dau em")
    private String username;
    @NotBlank(message = "password khogn duoc de trong dau em")
    private String password;

    public ReqLoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
