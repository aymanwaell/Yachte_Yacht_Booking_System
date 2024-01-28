package com.elsokhna.Yakhte.service;

import com.elsokhna.Yakhte.model.BookedYacht;

import java.util.List;



public interface IBookingService {
    void cancelBooking(Long bookingId);

    List<BookedYacht> getAllBookingsByYachtId(Long yachtId);

    String saveBooking(Long yachtId, BookedYacht bookingRequest);

    BookedYacht findByBookingConfirmationCode(String confirmationCode);

    List<BookedYacht> getAllBookings();

    List<BookedYacht> getBookingsByUserEmail(String email);
}
