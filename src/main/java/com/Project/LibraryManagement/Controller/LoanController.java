package com.Project.LibraryManagement.Controller;

import com.Project.LibraryManagement.DTO.CheckoutBookDTO;
import com.Project.LibraryManagement.Model.Loan;
import com.Project.LibraryManagement.Service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    @PostMapping("/checkout")
    public ResponseEntity<Loan> checkoutBook(
            @RequestBody CheckoutBookDTO checkoutBookDTO) {
        System.out.println("###########");

        return ResponseEntity.ok(loanService.requestLoan(checkoutBookDTO.getBookId(), checkoutBookDTO.getUserId()));
    }

    @PutMapping("/{loanId}/return")
    public ResponseEntity<Loan> returnBook(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanService.returnBook(loanId));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<Loan>> getUserLoans(@PathVariable String username) {
        return ResponseEntity.ok(loanService.getLoansByUser(username));
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Loan>> getActiveLoans() {
        return ResponseEntity.ok(loanService.getAllActiveLoans());
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Loan>> getOverdueLoans() {
        return ResponseEntity.ok(loanService.getAllOverdueLoans());
    }
}