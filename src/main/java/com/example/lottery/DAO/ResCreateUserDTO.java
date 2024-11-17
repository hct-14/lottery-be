package com.example.lottery.DAO;

import com.example.lottery.Util.constant.GenderEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResCreateUserDTO {

    private Long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private Role role;


    @Getter
    @Setter
    public static class Role{
        private int id;
        private String name;
    }
}
