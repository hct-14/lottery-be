package com.example.lottery.Entity;

import com.example.lottery.Util.constant.GenderEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "user")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private GenderEnum gender;
    private String email;
    private String password;
    @Column(columnDefinition = "MEDIUMTEXT")

    private String refreshToken;
    private String address;
    private int age;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    @JsonIgnore
    private List<History> history;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}
