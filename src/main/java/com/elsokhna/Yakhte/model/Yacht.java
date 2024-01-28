package com.elsokhna.Yakhte.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
public class Yacht {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private String yachtType;
    private BigDecimal yachtPrice;
    private boolean isBooked = false;
    @Lob
    private Blob photo;

    @OneToMany(mappedBy="yacht", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BookedYacht> bookings;

    public Yacht() {
        this.bookings = new ArrayList<>();
    }
    public void addBooking(BookedYacht booking){
        if (bookings == null){
            bookings = new ArrayList<>();
        }
        bookings.add(booking);
        booking.setYacht(this);
        isBooked = true;
        String bookingCode = RandomStringUtils.randomNumeric(10);
        booking.setBookingConfirmationCode(bookingCode);
    }
}
