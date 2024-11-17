package com.example.lottery.DAO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResRoleDTO {
    private long id;
    private String name;
    private String description;
    private List<PermistionRole> permissions;



    @Setter
    @Getter
    @AllArgsConstructor
    public static class PermistionRole {
        private long id;
        private String name;
    }
}
