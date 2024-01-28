package com.elsokhna.Yakhte.repository;

import com.elsokhna.Yakhte.model.BookedYacht;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;



public interface BookingRepository extends JpaRepository<BookedYacht, Long> {

    List<BookedYacht> findByYachtId(Long yachtId);

 Optional<BookedYacht> findByBookingConfirmationCode(String confirmationCode);

    List<BookedYacht> findByGuestEmail(String email);
}
