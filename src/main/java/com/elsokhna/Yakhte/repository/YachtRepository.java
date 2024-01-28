package com.elsokhna.Yakhte.repository;

import com.elsokhna.Yakhte.model.Yacht;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;



public interface YachtRepository extends JpaRepository<Yacht, Long> {

    @Query("SELECT DISTINCT r.yachtType FROM Yacht r")
    List<String> findDistinctYachtTypes();

    @Query(" SELECT r FROM Yacht r " +
            " WHERE r.yachtType LIKE %:yachtType% " +
            " AND r.id NOT IN (" +
            "  SELECT br.yacht.id FROM BookedYacht br " +
            "  WHERE ((br.checkInDate <= :checkOutDate) AND (br.checkOutDate >= :checkInDate))" +
            ")")

    List<Yacht> findAvailableYachtsByDatesAndType(LocalDate checkInDate, LocalDate checkOutDate, String yachtType);
}

