package com.Project.LibraryManagement.Repository;


import com.Project.LibraryManagement.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);
}