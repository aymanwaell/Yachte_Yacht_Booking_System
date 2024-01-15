package com.elsokhna.Yakhte.controller;

import com.elsokhna.Yakhte.model.Yacht;
import com.elsokhna.Yakhte.response.YachtResponse;
import com.elsokhna.Yakhte.service.IYachtService;
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
@RequestMapping("/yachts")
public class YachtController {

    private final IYachtService yachtService;

    @PostMapping("/add/new-yacht")
    public ResponseEntity<YachtResponse> addNewYacht
            (@RequestParam("photo") MultipartFile photo,
             @RequestParam("yachtType") String yachtType,
             @RequestParam("yachtPrice") BigDecimal yachtPrice) throws SQLException, IOException {
        Yacht savedYacht = yachtService.addNewYacht(photo,yachtType,yachtPrice);
        YachtResponse response = new YachtResponse(savedYacht.getId(), savedYacht.getRoomType(), savedYacht.getRoomPrice());
        return ResponseEntity.ok(response);
    }
}
