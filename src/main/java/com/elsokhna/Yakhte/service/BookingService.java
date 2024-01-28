package com.elsokhna.Yakhte.service;


import com.elsokhna.Yakhte.exception.InvalidBookingRequestException;
import com.elsokhna.Yakhte.exception.ResourceNotFoundException;
import com.elsokhna.Yakhte.model.BookedYacht;
import com.elsokhna.Yakhte.model.Yacht;
import com.elsokhna.Yakhte.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {
    private final BookingRepository bookingRepository;
    private final IYachtService yachtService;


    @Override
    public List<BookedYacht> getAllBookings() {
        return bookingRepository.findAll();
    }


    @Override
    public List<BookedYacht> getBookingsByUserEmail(String email) {
        return bookingRepository.findByGuestEmail(email);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public List<BookedYacht> getAllBookingsByYachtId(Long yachtId) {
        return bookingRepository.findByYachtId(yachtId);
    }

    @Override
    public String saveBooking(Long yachtId, BookedYacht bookingRequest) {
        if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())){
            throw new InvalidBookingRequestException("Check-in date must come before check-out date");
        }
        Yacht yacht = yachtService.getYachtById(yachtId).get();
        List<BookedYacht> existingBookings = yacht.getBookings();
        boolean yachtIsAvailable = yachtIsAvailable(bookingRequest,existingBookings);
        if (yachtIsAvailable){
            yacht.addBooking(bookingRequest);
            bookingRepository.save(bookingRequest);
        }else{
            throw  new InvalidBookingRequestException("Sorry, This yacht is not available for the selected dates;");
        }
        return bookingRequest.getBookingConfirmationCode();
    }

    @Override
    public BookedYacht findByBookingConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode)
                .orElseThrow(() -> new ResourceNotFoundException("No booking found with booking code :"+confirmationCode));

    }


    private boolean yachtIsAvailable(BookedYacht bookingRequest, List<BookedYacht> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }




}
