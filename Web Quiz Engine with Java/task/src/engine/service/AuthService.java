package engine.service;

import engine.dto.RegisterInput;
import engine.model.User;
import engine.repository.AuthRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthService implements UserDetailsService {
    private final AuthRepository authRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(AuthRepository authRepository, BCryptPasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    public User register(RegisterInput registerInput) {
        User user = new User();
        user.setEmail(registerInput.getEmail());
        user.setPassword(passwordEncoder.encode(registerInput.getPassword()));
        try {
            authRepository.save(user);
        } catch (Exception e) {
            return null;
        }
        return user;
    }

    private User findByEmail(String email) {
        return authRepository.findByEmail(email);
    }

    public User login(RegisterInput login) {
        User user = findByEmail(login.getEmail());
        if (user == null) {
            return null;
        }
        if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
            return null;
        }
        return user;
    }

    public Long getAuthenticatedUserId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return findByEmail(userDetails.getUsername()).getId();
    }

    public String getAuthenticatedUserEmail() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

}
