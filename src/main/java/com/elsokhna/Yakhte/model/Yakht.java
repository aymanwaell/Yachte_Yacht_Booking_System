package com.elsokhna.Yakhte.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Getter
@Setter
@AllArgsConstructor

public class Yakht {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomType;
    private BigDecimal roomPrice;
    private boolean isBooked = false;

    @Lob
    private Blob photo;

    @OneToMany(mappedBy = "yakht", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<BookedYakht> bookings;

    public Yakht() {
        this.bookings = new ArrayList<>();
    }

    public void addBooking(BookedYakht booking){
        if(bookings==null){
            bookings = new ArrayList<>();
        }
        bookings.add(booking);
        booking.setYakht(this);
        isBooked=true;
        String bookingCode = RandomStringUtils.random(10);
        booking.setBookingConfirmationCode(bookingCode);
    }
}
