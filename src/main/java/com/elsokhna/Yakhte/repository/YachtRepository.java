package com.elsokhna.Yakhte.repository;

import com.elsokhna.Yakhte.model.Yacht;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface YachtRepository extends JpaRepository<Yacht, Long> {
    @Query("SELECT DISTINCT r.yachtType FROM Yacht r")
    List<String> findDistinctYachtTypes();
}
