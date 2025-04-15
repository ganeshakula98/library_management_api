package com.Project.LibraryManagement.Repository;


import com.Project.LibraryManagement.Model.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BorrowerRepository extends JpaRepository<Borrower, Long> {
    boolean existsByEmail(String email);
    Optional<Borrower> findByUserId(Long userId);
    Optional<Borrower> findByUserUsername(String username);
}