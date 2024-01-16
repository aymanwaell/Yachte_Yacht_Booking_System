package com.elsokhna.Yakhte.controller;

import com.elsokhna.Yakhte.exception.InvalidBookingRequestexception;
import com.elsokhna.Yakhte.exception.ResourceNotFoundException;
import com.elsokhna.Yakhte.model.BookedYacht;
import com.elsokhna.Yakhte.model.Yacht;
import com.elsokhna.Yakhte.response.BookingResponse;
import com.elsokhna.Yakhte.response.YachtResponse;
import com.elsokhna.Yakhte.service.BookingService;
import com.elsokhna.Yakhte.service.IBookingService;
import com.elsokhna.Yakhte.service.IYachtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Controller
public class BookingController {

    private final IBookingService bookingService;
    private final IYachtService yachtService;

    @GetMapping("/all-bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings(){
        List<BookedYacht> bookings = bookingService.getAllBookings();
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for(BookedYacht booking : bookings){
            BookingResponse bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }


    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode){
        BookedYacht booking = bookingService.findByBookingConfirmationCode(confirmationCode);
        BookingResponse bookingResponse = getBookingResponse(booking);
        return ResponseEntity.ok(bookingResponse);
    }

    @GetMapping("/yacht/{yachtId}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long yachtId,
                                         @RequestBody BookedYacht bookingRequest){
        try {
            String confirmationCode = bookingService.saveBooking(yachtId,bookingRequest);
            return ResponseEntity.ok("Yacht booked successfully, " +
                    "Your booking confirmation code is :"+confirmationCode);
        } catch (InvalidBookingRequestexception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("booking/{bookingId}/delete")
    public void cancelBooking(@PathVariable Long bookingId){
        bookingService.cancelBooking(bookingId);
    }

    private BookingResponse getBookingResponse(BookedYacht booking) {
        Yacht theYacht = yachtService.getYachtById(booking.getYacht().getId()).get();
        YachtResponse yacht = new YachtResponse(
                theYacht.getId(),
                theYacht.getYachtType(),
                theYacht.getYachtPrice());
        return new BookingResponse(
                booking.getBookingId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getGuestFullName(),
                booking.getGuestEmail(),
                booking.getNumOfAdults(),
                booking.getNumOfChildren(),
                booking.getTotalNumOfGuest(),
                booking.getBookingConfirmationCode(),
                yacht
                );
    }

}
