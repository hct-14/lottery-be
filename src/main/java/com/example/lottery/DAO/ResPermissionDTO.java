package com.example.lottery.DAO;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResPermissionDTO {


    private long id;
    private String name;
    private String apiPath;
    private String method;
    private String module;

    private List<RolePermission> rolePermission;




    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RolePermission {
        private long id;
        private String name;
        private String description; 
    }
}