package com.elsokhna.Yakhte.service;

import com.elsokhna.Yakhte.model.Yakht;
import com.elsokhna.Yakhte.repository.YakhtRepository;
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
public class YakhtService implements IYakhtService{
    private final YakhtRepository yakhtRepository;

    @Override
    public Yakht addNewYakht(MultipartFile file, String yakhtType, BigDecimal yakhtPrice) throws SQLException, IOException {
        Yakht yakht = new Yakht();
        yakht.setRoomType(yakhtType);
        yakht.setRoomPrice(yakhtPrice);
        if(!file.isEmpty()){
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            yakht.setPhoto(photoBlob);
        }
        return yakhtRepository.save(yakht);
    }
}
