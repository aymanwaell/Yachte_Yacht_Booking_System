package com.elsokhna.Yakhte.repository;

import com.elsokhna.Yakhte.model.BookedYacht;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookedYacht,Long> {

    BookedYacht findByBookingConfirmationCode(String confirmationCode);
    List<BookedYacht> findByYachtId(Long yachtId);
}
