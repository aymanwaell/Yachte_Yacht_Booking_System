package com.elsokhna.Yakhte.response;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class YachtResponse {
    private Long id;
    private String yachtType;
    private BigDecimal yachtPrice;
    private boolean isBooked;
    private String photo;
    private List<BookingResponse>bookings;

    public YachtResponse(Long id, String yachtType, BigDecimal yachtPrice) {
        this.id = id;
        this.yachtType = yachtType;
        this.yachtPrice = yachtPrice;
    }

    public YachtResponse(Long id, String yachtType, BigDecimal yachtPrice, boolean isBooked,
                         byte[] photoBytes , List<BookingResponse> bookings) {
        this.id = id;
        this.yachtType = yachtType;
        this.yachtPrice = yachtPrice;
        this.isBooked = isBooked;
        this.photo = photoBytes != null ? Base64.encodeBase64String(photoBytes) : null;
       this.bookings = bookings;
    }

}
