package com.Project.LibraryManagement.Service;

import com.Project.LibraryManagement.Model.Borrower;
import com.Project.LibraryManagement.Repository.BorrowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class BorrowerService {

    private final BorrowerRepository borrowerRepository;

    @Autowired
    public BorrowerService(BorrowerRepository borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
    }

    public List<Borrower> getAllBorrowers() {
        return borrowerRepository.findAll();
    }

    public Borrower getBorrowerById(Long id) {
        return borrowerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrower not found with ID: " + id));
    }

    public Borrower addBorrower(Borrower borrower) {
        if (borrowerRepository.existsByEmail(borrower.getEmail())) {
            throw new RuntimeException("Email already exists: " + borrower.getEmail());
        }
        borrower.setId(null); // Ensure new entity
        return borrowerRepository.save(borrower);
    }

    public Borrower updateBorrower(Long id, Borrower updatedBorrower) {
        Borrower existing = borrowerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrower not found with ID: " + id));

        if (!existing.getEmail().equals(updatedBorrower.getEmail()) &&
                borrowerRepository.existsByEmail(updatedBorrower.getEmail())) {
            throw new RuntimeException("Email already exists: " + updatedBorrower.getEmail());
        }

        existing.setName(updatedBorrower.getName());
        existing.setEmail(updatedBorrower.getEmail());
        existing.setPhone(updatedBorrower.getPhone());

        return borrowerRepository.save(existing);
    }

    public void deleteBorrower(Long id) {
        if (!borrowerRepository.existsById(id)) {
            throw new RuntimeException("Borrower not found with ID: " + id);
        }
        borrowerRepository.deleteById(id);
    }
}