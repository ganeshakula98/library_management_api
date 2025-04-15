package com.Project.LibraryManagement.DTO;

import com.Project.LibraryManagement.Model.Borrower;
import com.Project.LibraryManagement.Model.User;
import lombok.Data;

@Data
public class UserWithBorrowerDTO {
    private Long id;
    private String username;
    private String role;
    private BorrowerDTO borrower;

    public UserWithBorrowerDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole().name();
        this.borrower = user.getBorrower() != null ?
                new BorrowerDTO(user.getBorrower()) : null;
    }

    @Data
    public static class BorrowerDTO {
        private Long id;
        private String name;
        private String email;
        private String phone;

        public BorrowerDTO(Borrower borrower) {
            this.id = borrower.getId();
            this.name = borrower.getName();
            this.email = borrower.getEmail();
            this.phone = borrower.getPhone();
        }
    }
}