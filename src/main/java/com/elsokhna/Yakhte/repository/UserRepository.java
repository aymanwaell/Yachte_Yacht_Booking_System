package com.elsokhna.Yakhte.repository;

import com.elsokhna.Yakhte.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByEmail(String email);

    void deleteByEmail();

    Optional<User>  findByEmail(String email);
}
