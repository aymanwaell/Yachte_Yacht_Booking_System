package com.elsokhna.Yakhte.controller;

import com.elsokhna.Yakhte.exception.PhotoRetrievalException;
import com.elsokhna.Yakhte.exception.ResourceNotFoundException;
import com.elsokhna.Yakhte.model.BookedYacht;
import com.elsokhna.Yakhte.model.Yacht;
import com.elsokhna.Yakhte.response.BookingResponse;
import com.elsokhna.Yakhte.response.YachtResponse;
import com.elsokhna.Yakhte.service.BookingService;
import com.elsokhna.Yakhte.service.IYachtService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/yachts")
public class YachtController {
    private final IYachtService yachtService;
    private final BookingService bookingService;

    @PostMapping("/add/new-yacht")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<YachtResponse> addNewYacht(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("yachtType") String yachtType,
            @RequestParam("yachtPrice") BigDecimal yachtPrice) throws SQLException, IOException {
        Yacht savedYacht = yachtService.addNewYacht(photo, yachtType, yachtPrice);
        YachtResponse response = new YachtResponse(savedYacht.getId(), savedYacht.getYachtType(),
                savedYacht.getYachtPrice());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/yacht/types")
    public List<String> getYachtTypes() {
        return yachtService.getAllYachtTypes();
    }

    @GetMapping("/all-yachts")
    public ResponseEntity<List<YachtResponse>> getAllYachts() throws SQLException {
        List<Yacht> yachts = yachtService.getAllYachts();
        List<YachtResponse> yachtRespons = new ArrayList<>();
        for (Yacht yacht : yachts) {
            byte[] photoBytes = yachtService.getYachtPhotoByYachtId(yacht.getId());
            if (photoBytes != null && photoBytes.length > 0) {
                String base64Photo = Base64.encodeBase64String(photoBytes);
                YachtResponse yachtResponse = getYachtResponse(yacht);
                yachtResponse.setPhoto(base64Photo);
                yachtRespons.add(yachtResponse);
            }
        }
        return ResponseEntity.ok(yachtRespons);
    }
    @DeleteMapping("/delete/yacht/{yachtId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteYacht(@PathVariable Long yachtId){
        yachtService.deleteYacht(yachtId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{yachtId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<YachtResponse> updateYacht(@PathVariable Long yachtId,
                                                                                             @RequestParam(required = false)  String yachtType,
                                                                                             @RequestParam(required = false) BigDecimal yachtPrice,
                                                                                             @RequestParam(required = false) MultipartFile photo) throws SQLException, IOException {
        byte[] photoBytes = photo != null && !photo.isEmpty() ?
                photo.getBytes() : yachtService.getYachtPhotoByYachtId(yachtId);
        Blob photoBlob = photoBytes != null && photoBytes.length >0 ? new SerialBlob(photoBytes): null;
        Yacht theYacht = yachtService.updateYacht(yachtId, yachtType, yachtPrice, photoBytes);
        theYacht.setPhoto(photoBlob);
        YachtResponse yachtResponse = getYachtResponse(theYacht);
        return ResponseEntity.ok(yachtResponse);
    }

    @GetMapping("/yacht/{yachtId}")
    public ResponseEntity<Optional<YachtResponse>> getYachtById(@PathVariable Long yachtId){
        Optional<Yacht> theYacht = yachtService.getYachtById(yachtId);
        return theYacht.map(yacht -> {
            YachtResponse yachtResponse = getYachtResponse(yacht);
            return  ResponseEntity.ok(Optional.of(yachtResponse));
        }).orElseThrow(() -> new ResourceNotFoundException("Yacht not found"));
    }

    @GetMapping("/available-yachts")
    public ResponseEntity<List<YachtResponse>> getAvailableYachts(
            @RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate checkInDate,
            @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate checkOutDate,
            @RequestParam("yachtType") String yachtType) throws SQLException {
        List<Yacht> availableYachts = yachtService.getAvailableYachts(checkInDate, checkOutDate, yachtType);
        List<YachtResponse> yachtRespons = new ArrayList<>();
        for (Yacht yacht : availableYachts){
            byte[] photoBytes = yachtService.getYachtPhotoByYachtId(yacht.getId());
            if (photoBytes != null && photoBytes.length > 0){
                String photoBase64 = Base64.encodeBase64String(photoBytes);
                YachtResponse yachtResponse = getYachtResponse(yacht);
                yachtResponse.setPhoto(photoBase64);
                yachtRespons.add(yachtResponse);
            }
        }
        if(yachtRespons.isEmpty()){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(yachtRespons);
        }
    }




    private YachtResponse getYachtResponse(Yacht yacht) {
        List<BookedYacht> bookings = getAllBookingsByYachtId(yacht.getId());
       List<BookingResponse> bookingInfo = bookings
                .stream()
                .map(booking -> new BookingResponse(booking.getBookingId(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(), booking.getBookingConfirmationCode())).toList();
        byte[] photoBytes = null;
        Blob photoBlob = yacht.getPhoto();
        if (photoBlob != null) {
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            } catch (SQLException e) {
                throw new PhotoRetrievalException("Error retrieving photo");
            }
        }
        return new YachtResponse(yacht.getId(),
                yacht.getYachtType(), yacht.getYachtPrice(),
                yacht.isBooked(), photoBytes, bookingInfo);
    }

    private List<BookedYacht> getAllBookingsByYachtId(Long yachtId) {
        return bookingService.getAllBookingsByYachtId(yachtId);

    }

}
