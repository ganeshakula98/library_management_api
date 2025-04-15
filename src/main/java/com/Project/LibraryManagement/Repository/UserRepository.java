package com.Project.LibraryManagement.Repository;

import com.Project.LibraryManagement.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.borrower WHERE u.id = :id")
    Optional<User> findByIdWithBorrower(@Param("id") Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.borrower")
    List<User> findAllWithBorrowers();
}