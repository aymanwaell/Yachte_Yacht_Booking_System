package com.elsokhna.Yakhte.service;

import com.elsokhna.Yakhte.model.Yakht;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

public interface IYakhtService {
    Yakht addNewYakht(MultipartFile photo, String yakhtType, BigDecimal yakhtPrice) throws SQLException, IOException;
}
