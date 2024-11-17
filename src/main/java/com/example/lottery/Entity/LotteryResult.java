package com.example.lottery.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "lotteryResult")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LotteryResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDate date;

    private int special;
    private int prize1;
    private int prize2_1;
    private int prize2_2;
    private int prize3_1;
    private int prize3_2;
    private int prize3_3;
    private int prize3_4;
    private int prize3_5;
    private int prize3_6;
    private int prize4_1;
    private int prize4_2;
    private int prize4_3;
    private int prize4_4;
    private int prize5_1;
    private int prize5_2;
    private int prize5_3;
    private int prize5_4;
    private int prize5_5;
    private int prize5_6;
    private int prize6_1;
    private int prize6_2;
    private int prize6_3;
    private int prize7_1;
    private int prize7_2;
    private int prize7_3;
    private int prize7_4;

    }