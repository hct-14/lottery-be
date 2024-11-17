package com.example.lottery.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "history")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="history_user" ,joinColumns = @JoinColumn(name = "history_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;
}
