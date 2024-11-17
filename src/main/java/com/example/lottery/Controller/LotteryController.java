package com.example.lottery.Controller;

import com.example.lottery.DAO.LotteryDTO;
import com.example.lottery.DAO.ResultPaginationDTO;
import com.example.lottery.Entity.LotteryResult;
import com.example.lottery.Service.LotteryService;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/lottery")
public class LotteryController {

    @Autowired
    private LotteryService lotteryService;

    @GetMapping("/fetchLotteryHistory")
    public ResponseEntity<String> fetchLotteryHistory() {
        try {
            lotteryService.fetchAndSaveLotteryHistory();
            return ResponseEntity.ok("Lottery history saved successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error saving lottery history: " + e.getMessage());
        }
    }

    @GetMapping("/extractLotteryDetails")
    public ResponseEntity<String> extractLotteryDetails() {
        try {
            lotteryService.extractAndSaveLotteryDetails();
            return ResponseEntity.ok("Lottery details extracted and saved successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error extracting lottery details: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> getLotteryResultByDate(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        Optional<LotteryResult> result = lotteryService.findLotteryResultByDate(date);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/search/all")
    public ResponseEntity<ResultPaginationDTO> getAllCompany(@Filter Specification<LotteryResult> spec, Pageable pageable) {
        return  ResponseEntity.status(HttpStatus.OK).body(this.lotteryService.getAllLottery(spec, pageable));
    }

}
