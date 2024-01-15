package com.elsokhna.Yakhte.service;

import com.elsokhna.Yakhte.model.Yacht;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

public interface IYachtService {
    Yacht addNewYacht(MultipartFile photo, String yakhtType, BigDecimal yakhtPrice) throws SQLException, IOException;
}
