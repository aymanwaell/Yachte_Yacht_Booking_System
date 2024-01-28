package com.elsokhna.Yakhte.service;

import com.elsokhna.Yakhte.model.Yacht;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;



public interface IYachtService {
    Yacht addNewYacht(MultipartFile photo, String yachtType, BigDecimal yachtPrice) throws SQLException, IOException;

    List<String> getAllYachtTypes();

    List<Yacht> getAllYachts();

    byte[] getYachtPhotoByYachtId(Long yachtId) throws SQLException;

    void deleteYacht(Long yachtId);

    Yacht updateYacht(Long yachtId, String yachtType, BigDecimal yachtPrice, byte[] photoBytes);

    Optional<Yacht> getYachtById(Long yachtId);

    List<Yacht> getAvailableYachts(LocalDate checkInDate, LocalDate checkOutDate, String yachtType);
}
