package com.elsokhna.Yakhte.service;

import com.elsokhna.Yakhte.model.BookedYacht;

import java.util.List;

public interface IBookingService {
    void cancelBooking(Long bookingId);

    String saveBooking(Long yachtId, BookedYacht bookingRequest);

    BookedYacht findByBookingConfirmationCode(String confirmationCode);

    List<BookedYacht>getAllBookings();
}
