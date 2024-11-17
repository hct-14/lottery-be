package com.example.lottery.Service;

import com.example.lottery.DAO.LotteryDTO;
import com.example.lottery.DAO.ResultPaginationDTO;
import com.example.lottery.Entity.LotteryResult;
import com.example.lottery.Responsetory.LotteryResultRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LotteryService {
    private final LotteryResultRepository lotteryResultRepository;

    public LotteryService(LotteryResultRepository lotteryResultRepository) {
        this.lotteryResultRepository = lotteryResultRepository;
    }
    private final String apiUrl = "https://xoso188.net/api/front/open/lottery/history/list/1/miba";
    @Scheduled(cron = "0 35 18 * * ?")

    public void fetchAndSaveLotteryHistory() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(apiUrl, String.class);

        File file = new File("src/main/resources/static/lotteryHistory.json");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(response);
        }
    }
    @Scheduled(cron = "0 36 18 * * ?")
    public void extractAndSaveLotteryDetails() throws IOException {
        File file = new File("src/main/resources/static/lotteryHistory.json");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new FileReader(file));
        JsonNode issueList = rootNode.path("t").path("issueList");
        ArrayNode resultArray = objectMapper.createArrayNode();
        for (JsonNode issue : issueList) {
            String turnNum = issue.path("turnNum").asText();
            String detail = issue.path("detail").asText();
            detail = detail.replaceAll("\\[|\\]", "").replaceAll("\"", "");

            String[] prizes = detail.split(",");
            LotteryResult lotteryResult = new LotteryResult();
            lotteryResult.setDate(LocalDate.parse(turnNum, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            lotteryResult.setSpecial(Integer.parseInt(prizes[0])); // Giải đặc biệt
            lotteryResult.setPrize1(Integer.parseInt(prizes[1]));  // Giải nhất
            lotteryResult.setPrize2_1(Integer.parseInt(prizes[2])); // Giải nhì 1
            lotteryResult.setPrize2_2(Integer.parseInt(prizes[3])); // Giải nhì 2
            lotteryResult.setPrize3_1(Integer.parseInt(prizes[4])); // Giải ba 1
            lotteryResult.setPrize3_2(Integer.parseInt(prizes[5])); // Giải ba 2
            lotteryResult.setPrize3_3(Integer.parseInt(prizes[6])); // Giải ba 3
            lotteryResult.setPrize3_4(Integer.parseInt(prizes[7])); // Giải ba 4
            lotteryResult.setPrize3_5(Integer.parseInt(prizes[8])); // Giải ba 5
            lotteryResult.setPrize3_6(Integer.parseInt(prizes[9])); // Giải ba 6
            lotteryResult.setPrize4_1(Integer.parseInt(prizes[10])); // Giải tư 1
            lotteryResult.setPrize4_2(Integer.parseInt(prizes[11])); // Giải tư 2
            lotteryResult.setPrize4_3(Integer.parseInt(prizes[12])); // Giải tư 3
            lotteryResult.setPrize4_4(Integer.parseInt(prizes[13])); // Giải tư 4
            lotteryResult.setPrize5_1(Integer.parseInt(prizes[14])); // Giải năm 1
            lotteryResult.setPrize5_2(Integer.parseInt(prizes[15])); // Giải năm 2
            lotteryResult.setPrize5_3(Integer.parseInt(prizes[16])); // Giải năm 3
            lotteryResult.setPrize5_4(Integer.parseInt(prizes[17])); // Giải năm 4
            lotteryResult.setPrize5_5(Integer.parseInt(prizes[18])); // Giải năm 5
            lotteryResult.setPrize5_6(Integer.parseInt(prizes[19])); // Giải năm 6
            lotteryResult.setPrize6_1(Integer.parseInt(prizes[20])); // Giải sáu 1
            lotteryResult.setPrize6_2(Integer.parseInt(prizes[21])); // Giải sáu 2
            lotteryResult.setPrize6_3(Integer.parseInt(prizes[22])); // Giải sáu 3
            lotteryResult.setPrize7_1(Integer.parseInt(prizes[23])); // Giải bảy 1
            lotteryResult.setPrize7_2(Integer.parseInt(prizes[24])); // Giải bảy 2
            lotteryResult.setPrize7_3(Integer.parseInt(prizes[25])); // Giải bảy 3
            lotteryResult.setPrize7_4(Integer.parseInt(prizes[26])); // Giải bảy 4
            // Lưu vào DB
            lotteryResultRepository.save(lotteryResult);
        }

    }

    public Optional<LotteryResult> findLotteryResultByDate(LocalDate date) {
        return lotteryResultRepository.findByDate(date);
    }



    public ResultPaginationDTO getAllLottery(Specification<LotteryResult> spec, Pageable pageable) {
        // Lấy dữ liệu phân trang từ repository
        Page<LotteryResult> pageLottery = this.lotteryResultRepository.findAll(spec, pageable);

        // Tạo đối tượng ResultPaginationDTO để lưu thông tin phân trang
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);  // Trang hiện tại
        mt.setPageSize(pageable.getPageSize());    // Kích thước trang
        mt.setPages(pageLottery.getTotalPages());  // Tổng số trang
        mt.setTotal(pageLottery.getTotalElements()); // Tổng số phần tử

        rs.setMeta(mt);  // Đặt thông tin phân trang vào đối tượng

        // Chuyển đổi các LotteryResult thành LotteryDTO
        List<LotteryDTO> listLottery = pageLottery.getContent().stream()
                .map(item -> new LotteryDTO(
                        item.getId(),
                        item.getDate(),
                        item.getSpecial(),
                        item.getPrize1(),
                        item.getPrize2_1(),
                        item.getPrize2_2(),
                        item.getPrize3_1(),
                        item.getPrize3_2(),
                        item.getPrize3_3(),
                        item.getPrize3_4(),
                        item.getPrize3_5(),
                        item.getPrize3_6(),
                        item.getPrize4_1(),
                        item.getPrize4_2(),
                        item.getPrize4_3(),
                        item.getPrize4_4(),
                        item.getPrize5_1(),
                        item.getPrize5_2(),
                        item.getPrize5_3(),
                        item.getPrize5_4(),
                        item.getPrize5_5(),
                        item.getPrize5_6(),
                        item.getPrize6_1(),
                        item.getPrize6_2(),
                        item.getPrize6_3(),
                        item.getPrize7_1(),
                        item.getPrize7_2(),
                        item.getPrize7_3(),
                        item.getPrize7_4()
                ))
                .collect(Collectors.toList());

        // Đặt danh sách LotteryDTO vào ResultPaginationDTO
        rs.setResult(listLottery);

        return rs;  // Trả về kết quả phân trang
    }

}
