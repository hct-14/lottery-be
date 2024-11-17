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
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private List<RolePermission> rolePermission;

    public <R> ResPermissionDTO(int id, @NotBlank(message = "name không được để trống") String name, @NotBlank(message = "apiPath không được để trống") String apiPath, @NotBlank(message = "method không được để trống") String method, @NotBlank(message = "module không được để trống") String module, R collect) {
    }


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