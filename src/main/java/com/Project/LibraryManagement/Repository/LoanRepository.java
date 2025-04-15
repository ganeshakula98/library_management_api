package com.Project.LibraryManagement.Repository;

import com.Project.LibraryManagement.Model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByBorrowerId(Long borrowerId);
    List<Loan> findByStatus(LoanStatus status);
    List<Loan> findByBorrowerUserUsername(String username);

    boolean existsByBookAndBorrowerAndStatus(Book book, Borrower borrower, LoanStatus status);

    @Query("SELECT l FROM Loan l WHERE l.status = :status AND l.dueDate < :currentDate")
    List<Loan> findByStatusAndDueDateBefore(LoanStatus status, LocalDate currentDate);
}