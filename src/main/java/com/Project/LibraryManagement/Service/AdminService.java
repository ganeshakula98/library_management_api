package com.Project.LibraryManagement.Service;

import com.Project.LibraryManagement.DTO.UserWithBorrowerDTO;
import com.Project.LibraryManagement.Model.Role;
import com.Project.LibraryManagement.Model.User;
import com.Project.LibraryManagement.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    public List<UserWithBorrowerDTO> getAllUsersWithBorrowers() {
        List<User> users = userRepository.findAllWithBorrowers();
        return users.stream()
                .map(UserWithBorrowerDTO::new)
                .collect(Collectors.toList());
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createAdmin(User user) {
        user.setRole(Role.ADMIN);
        return userRepository.save(user);
    }

    public User updateUserRole(Long userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(role);
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
