package com.elsokhna.Yakhte.controller;

import com.elsokhna.Yakhte.model.Yakht;
import com.elsokhna.Yakhte.response.YakhtResponse;
import com.elsokhna.Yakhte.service.IYakhtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@RequiredArgsConstructor
@Controller
@RequestMapping("/yakhts")
public class YakhtController {

    private final IYakhtService yakhtService;

    @PostMapping("/add/new-yakht")
    public ResponseEntity<YakhtResponse> addNewYakht
            (@RequestParam("photo") MultipartFile photo,
             @RequestParam("yakhtType") String yakhtType,
             @RequestParam("yakhtPrice") BigDecimal yakhtPrice) throws SQLException, IOException {
        Yakht savedYakht = yakhtService.addNewYakht(photo,yakhtType,yakhtPrice);
        YakhtResponse response = new YakhtResponse(savedYakht.getId(), savedYakht.getRoomType(), savedYakht.getRoomPrice());
        return ResponseEntity.ok(response);
    }
}
