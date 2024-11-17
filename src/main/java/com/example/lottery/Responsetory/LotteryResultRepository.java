package com.example.lottery.Responsetory;
import com.example.lottery.Entity.LotteryResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.Optional;

public interface LotteryResultRepository extends JpaRepository<LotteryResult, Long>, JpaSpecificationExecutor<LotteryResult> {

    Optional<LotteryResult> findByDate(LocalDate date);

//    Page<LotteryResult> findAll(Specification<LotteryResult> spec, Pageable pageable);
}
