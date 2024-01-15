package com.elsokhna.Yakhte.service;

import com.elsokhna.Yakhte.exception.ResourceNotFoundException;
import com.elsokhna.Yakhte.model.Yacht;
import com.elsokhna.Yakhte.repository.YachtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
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
        if(!file.isEmpty()){
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
    public byte[] getYachtPhotoByYachtId(Long yachtId) throws SQLException, ResourceNotFoundException {
        Optional<Yacht> theYacht = yachtRepository.findById(yachtId);
                if(theYacht.isEmpty()){
                    throw new ResourceNotFoundException("Sorry, Yacht not found!");
                }
                Blob photoBlob = theYacht.get().getPhoto();
                if(photoBlob != null){
                    return photoBlob.getBytes(1,(int)photoBlob.length());
                }
        return null;
    }
}
