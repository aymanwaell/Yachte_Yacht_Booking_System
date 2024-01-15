package com.elsokhna.Yakhte.service;

import com.elsokhna.Yakhte.model.Yacht;
import com.elsokhna.Yakhte.repository.YachtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class YachtService implements IYachtService {
    private final YachtRepository yachtRepository;

    @Override
    public Yacht addNewYacht(MultipartFile file, String yachtType, BigDecimal yachtPrice) throws SQLException, IOException {
        Yacht yacht = new Yacht();
        yacht.setRoomType(yachtType);
        yacht.setRoomPrice(yachtPrice);
        if(!file.isEmpty()){
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            yacht.setPhoto(photoBlob);
        }
        return yachtRepository.save(yacht);
    }
}
