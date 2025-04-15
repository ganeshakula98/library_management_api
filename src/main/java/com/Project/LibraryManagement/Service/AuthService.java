package com.Project.LibraryManagement.Service;

import com.Project.LibraryManagement.DTO.AuthRequest;
import com.Project.LibraryManagement.DTO.AuthResponse;
import com.Project.LibraryManagement.DTO.RegisterRequest;
import com.Project.LibraryManagement.Model.Borrower;
import com.Project.LibraryManagement.Model.Role;
import com.Project.LibraryManagement.Model.User;
import com.Project.LibraryManagement.Repository.BorrowerRepository;
import com.Project.LibraryManagement.Repository.UserRepository;
import com.Project.LibraryManagement.websecconfig.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final BorrowerRepository borrowerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        var borrower = Borrower.builder()
                .name(request.getName())
                .email(request.getEmail())
                .user(user)
                .build();

        borrowerRepository.save(borrower);

        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .role(user.getRole().name())
                .build();

    }
    public AuthResponse authenticate(AuthRequest request) {
        try {
            // Authenticate first
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Then load the actual User entity from repository
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Generate token using UserDetails
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            var jwtToken = jwtService.generateToken(userDetails);

            return AuthResponse.builder()
                    .token(jwtToken)
                    .userId(user.getId())
                    .role(user.getRole().name()) // Get role from User entity
                    .build();
        } catch (Exception e) {
            System.err.println("Authentication error: " + e.getMessage());
            throw new RuntimeException("Authentication failed", e);
        }
    }
}