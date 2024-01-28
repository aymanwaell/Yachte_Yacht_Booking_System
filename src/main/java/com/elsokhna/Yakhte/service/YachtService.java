package com.elsokhna.Yakhte.service;


import com.elsokhna.Yakhte.exception.InternalServerException;
import com.elsokhna.Yakhte.exception.ResourceNotFoundException;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;



@Service
@RequiredArgsConstructor
public class YachtService implements IYachtService {
    private final YachtRepository yachtRepository;
    @Override
    public Yacht addNewYacht(MultipartFile file, String yachtType, BigDecimal yachtPrice) throws SQLException, IOException {
        Yacht yacht = new Yacht();
        yacht.setYachtType(yachtType);
        yacht.setYachtPrice(yachtPrice);
        if (!file.isEmpty()){
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            yacht.setPhoto(photoBlob);
        }
        return yachtRepository.save(yacht);
    }

    @Override
    public List<String> getAllYachtTypes() {
        return yachtRepository.findDistinctYachtTypes();
    }

    @Override
    public List<Yacht> getAllYachts() {
        return yachtRepository.findAll();
    }

    @Override
    public byte[] getYachtPhotoByYachtId(Long yachtId) throws SQLException {
        Optional<Yacht> theYacht = yachtRepository.findById(yachtId);
        if(theYacht.isEmpty()){
            throw new ResourceNotFoundException("Sorry, Yacht not found!");
        }
        Blob photoBlob = theYacht.get().getPhoto();
        if(photoBlob != null){
            return photoBlob.getBytes(1, (int) photoBlob.length());
        }
        return null;
    }

    @Override
    public void deleteYacht(Long yachtId) {
        Optional<Yacht> theYacht = yachtRepository.findById(yachtId);
        if(theYacht.isPresent()){
            yachtRepository.deleteById(yachtId);
        }
    }

    @Override
    public Yacht updateYacht(Long yachtId, String yachtType, BigDecimal yachtPrice, byte[] photoBytes) {
        Yacht yacht = yachtRepository.findById(yachtId).get();
        if (yachtType != null) yacht.setYachtType(yachtType);
        if (yachtPrice != null) yacht.setYachtPrice(yachtPrice);
        if (photoBytes != null && photoBytes.length > 0) {
            try {
                yacht.setPhoto(new SerialBlob(photoBytes));
            } catch (SQLException ex) {
                throw new InternalServerException("Fail updating yacht");
            }
        }
       return yachtRepository.save(yacht);
    }

    @Override
    public Optional<Yacht> getYachtById(Long yachtId) {
        return Optional.of(yachtRepository.findById(yachtId).get());
    }

    @Override
    public List<Yacht> getAvailableYachts(LocalDate checkInDate, LocalDate checkOutDate, String yachtType) {
        return yachtRepository.findAvailableYachtsByDatesAndType(checkInDate, checkOutDate, yachtType);
    }
}
