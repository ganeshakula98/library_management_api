package com.Project.LibraryManagement.Controller;

import com.Project.LibraryManagement.DTO.UserWithBorrowerDTO;
import com.Project.LibraryManagement.Model.Role;
import com.Project.LibraryManagement.Model.User;
import com.Project.LibraryManagement.Repository.UserRepository;
import com.Project.LibraryManagement.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private  final UserRepository userRepository;

    @GetMapping("/users-with-borrowers")
    public ResponseEntity<List<UserWithBorrowerDTO>> getUsersWithBorrowers() {
        List<User> users = userRepository.findAllWithBorrowers();
        List<UserWithBorrowerDTO> dtos = users.stream()
                .map(UserWithBorrowerDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PostMapping("/users/create-admin")
    public ResponseEntity<User> createAdmin(@RequestBody User user) {
        return ResponseEntity.ok(adminService.createAdmin(user));
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<User> updateUserRole(
            @PathVariable Long userId,
            @RequestParam Role role) {
        return ResponseEntity.ok(adminService.updateUserRole(userId, role));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}