package com.elsokhna.Yakhte.controller;

import com.elsokhna.Yakhte.exception.PhotoRetrievalException;
import com.elsokhna.Yakhte.exception.ResourceNotFoundException;
import com.elsokhna.Yakhte.model.BookedYacht;
import com.elsokhna.Yakhte.model.Yacht;
import com.elsokhna.Yakhte.response.BookingResponse;
import com.elsokhna.Yakhte.response.YachtResponse;
import com.elsokhna.Yakhte.service.BookingService;
import com.elsokhna.Yakhte.service.IYachtService;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/yachts")
public class YachtController {

    private final IYachtService yachtService;
    private final BookingService bookingService;

    @PostMapping("/add/new-yacht")
    public ResponseEntity<YachtResponse> addNewYacht
            (@RequestParam("photo") MultipartFile photo,
             @RequestParam("yachtType") String yachtType,
             @RequestParam("yachtPrice") BigDecimal yachtPrice) throws SQLException, IOException {
        Yacht savedYacht = yachtService.addNewYacht(photo,yachtType,yachtPrice);
        YachtResponse response = new YachtResponse(savedYacht.getId(), savedYacht.getYachtType(), savedYacht.getYachtPrice());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/yacht/types")
    public List<String> getYachtTypes(){
        return yachtService.getAllYachtTypes();
    }

    @GetMapping("/yachts/all-yachts")
    public ResponseEntity <List<YachtResponse>> getAllYachts() throws SQLException, ResourceNotFoundException, PhotoRetrievalException {
        List<Yacht> yachts = yachtService.getAllYachts();
        List<YachtResponse> yachtResponses = new ArrayList<>();
        for(Yacht yacht: yachts){
            byte[] photoBytes = yachtService.getYachtPhotoByYachtId(yacht.getId());
            if(photoBytes != null  && photoBytes.length > 0){
                String base64Photo = Base64.getEncoder().encodeToString(photoBytes);
                YachtResponse yachtResponse = getYachtResponse(yacht);
                yachtResponse.setPhoto(base64Photo);
                yachtResponse.add(yachtResponse);
            }
        }
        return ResponseEntity.ok(yachtResponses);
    }

    @DeleteMapping("/delete/yacht/{yachtId}")
    public  ResponseEntity <Void> deleteYacht(@PathVariable("yachtId") Long yachtId){
        yachtService.deleteYacht(yachtId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{yachtId}")
    public ResponseEntity<YachtResponse> updateYacht
            (Long yachtId,@RequestParam(required = false) String yachtType
                         ,@RequestParam(required = false) BigDecimal yachtPrice
                         ,@RequestParam(required = false) MultipartFile photo) throws IOException, SQLException, ResourceNotFoundException, PhotoRetrievalException {
        byte[] photoBytes = photo != null && photo.isEmpty()?
                photo.getBytes() : yachtService.getYachtPhotoByYachtId(yachtId);
        Blob photoBlob = photoBytes != null && photoBytes.length>0 ? new SerialBlob(photoBytes): null;
        Yacht theYacht = yachtService.updateYacht(yachtId,yachtType,yachtPrice,photoBytes);
        theYacht.setPhoto(photoBlob);
        YachtResponse yachtResponse = getYachtResponse(theYacht);
        return ResponseEntity.ok(yachtResponse);
    }

    @GetMapping("/yacht/{yachtId}")
    public ResponseEntity<Optional<YachtResponse>> getYachtById(@PathVariable Long yachtId) throws ResourceNotFoundException {
        Optional<Yacht> theYacht = yachtService.getYachtById(yachtId);
        return theYacht.map(yacht -> {
            YachtResponse yachtResponse = null;
            try {
                yachtResponse = getYachtResponse(yacht);
            } catch (SQLException | PhotoRetrievalException e) {
                throw new RuntimeException(e);
            }
            return ResponseEntity.ok(Optional.of(yachtResponse));
        }).orElseThrow(()->new ResourceNotFoundException("Yacht not found"));
    }

    private YachtResponse getYachtResponse(Yacht yacht) throws SQLException, PhotoRetrievalException {
        List<BookedYacht> bookings = getAllBookingsByYachtId(yacht.getId());
        List<BookingResponse> bookingsInfo = bookings
                .stream()
                .map(booking -> new BookingResponse(booking.getBookingId()
                        ,booking.getCheckInDate(),booking.getCheckOutDate()
                        ,booking.getBookingConfirmationCode())).toList();
        byte[] photoBytes = null;
        Blob photoBlob = yacht.getPhoto();
        if (photoBlob != null){
            try {
                photoBytes = photoBlob.getBytes(1,(int) photoBlob.length());
            } catch (SQLException e){
                throw  new PhotoRetrievalException("Error retrieving photo");
            }
        }
        return  new YachtResponse(yacht.getId(), yacht.getYachtType(),
                yacht.getYachtPrice(), yacht.isBooked(),photoBytes, bookingsInfo);
    }

    private List<BookedYacht> getAllBookingsByYachtId(Long yachtId) {
        return bookingService.getAllBookingsByYachtId(yachtId);
    }
}
