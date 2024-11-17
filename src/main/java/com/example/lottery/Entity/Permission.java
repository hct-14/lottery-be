package com.example.lottery.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "name không được để trống")
    private String Name;

    @NotBlank(message = "apiPath không được để trống")
    private String apiPath;

    @NotBlank(message = "method không được để trống")
    private String method;

    @NotBlank(message = "module không được để trống")
    private String module;


    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
    @JsonIgnore
    private List<Role> roles;

    public Permission(String name, String apiPath, String method, String module) {
        this.Name = name;
        this.apiPath = apiPath;
        this.method = method;
        this.module = module;
    }



}
