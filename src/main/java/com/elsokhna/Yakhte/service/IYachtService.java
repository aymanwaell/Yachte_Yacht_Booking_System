package com.elsokhna.Yakhte.service;

import com.elsokhna.Yakhte.exception.InternalServerException;
import com.elsokhna.Yakhte.exception.ResourceNotFoundException;
import com.elsokhna.Yakhte.model.Yacht;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IYachtService {
    Yacht addNewYacht(MultipartFile photo, String yakhtType, BigDecimal yakhtPrice) throws SQLException, IOException;

    List<String> getAllYachtTypes();

    List<Yacht> getAllYachts();

    byte[] getYachtPhotoByYachtId(Long yachtId) throws SQLException, ResourceNotFoundException;

    void deleteYacht(Long yachtId);

    Yacht updateYacht(Long yachtId, String yachtType, BigDecimal yachtPrice, byte[] photoBytes) throws ResourceNotFoundException, InternalServerException;

    Optional<Yacht> getYachtById(Long yachtId);
}
