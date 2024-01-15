package com.elsokhna.Yakhte.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Base64;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Base64;
import java.util.List;

@Data
@NoArgsConstructor

public class YakhtResponse {
    private Long id;
    private String roomType;
    private BigDecimal roomPrice;
    private boolean isBooked = false;
    private String photo;
    private List<BookingResponse> bookings;

    public YakhtResponse(Long id, String roomType, BigDecimal roomPrice) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
    }

    public YakhtResponse(Long id, String roomType, BigDecimal roomPrice,
                         boolean isBooked, byte[] photoBytes,
                         List<BookingResponse> bookings) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.isBooked = isBooked;
        this.photo = photoBytes != null ? Base64.getEncoder().encodeToString(photoBytes) : null;
        this.bookings = bookings;
    }

}
