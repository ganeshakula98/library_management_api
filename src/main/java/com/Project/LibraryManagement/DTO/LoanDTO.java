package com.Project.LibraryManagement.DTO;


import lombok.Data;

@Data
public class LoanDTO {
    private Long loanId;
    private Long bookId;
    private Long borrowerId;
    private String checkoutDate;
    private String dueDate;
    private String returnDate;
    private String status;
    private BookDTO book;
    private BorrowerDTO borrower;

    @Data
    public static class BookDTO {
        private Long bookId;
        private String title;
        private String author;
        private String isbn;
        private Integer availableCopies;
    }

    @Data
    public static class BorrowerDTO {
        private Long borrowerId;
        private String name;
        private String email;
        private String phone;
    }
}
