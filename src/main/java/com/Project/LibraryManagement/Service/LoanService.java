package com.Project.LibraryManagement.Service;

import com.Project.LibraryManagement.Model.*;
import com.Project.LibraryManagement.Repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;
    private final UserRepository userRepository;

    @Transactional
    public Loan requestLoan(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Borrower borrower = borrowerRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Borrower not found"));

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("No available copies of this book");
        }

        // Check if user already has this book checked out
        boolean alreadyBorrowed = loanRepository.existsByBookAndBorrowerAndStatus(
                book, borrower, LoanStatus.ACTIVE);
        if (alreadyBorrowed) {
            throw new RuntimeException("You already have this book checked out");
        }

        // Update book availability
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        // Create loan record
        Loan loan = Loan.builder()
                .book(book)
                .borrower(borrower)
                .checkoutDate(LocalDate.now())
                .dueDate(LocalDate.now().plusWeeks(2)) // 2 weeks loan period
                .status(LoanStatus.ACTIVE)
                .build();

        return loanRepository.save(loan);
    }

    @Transactional
    public Loan returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new RuntimeException("This book has already been returned");
        }

        // Update book availability
        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        // Update loan record
        loan.setReturnDate(LocalDate.now());
        loan.setStatus(LoanStatus.RETURNED);

        return loanRepository.save(loan);
    }

    public List<Loan> getLoansByUser(String username) {
        return loanRepository.findByBorrowerUserUsername(username);
    }

    public List<Loan> getAllActiveLoans() {
        return loanRepository.findByStatus(LoanStatus.ACTIVE);
    }

    public List<Loan> getAllOverdueLoans() {
        return loanRepository.findByStatusAndDueDateBefore(
                LoanStatus.ACTIVE, LocalDate.now());
    }
}