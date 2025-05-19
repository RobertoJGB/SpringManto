package br.com.orlands.manto.service;

import br.com.orlands.manto.domain.UserDomain;
import br.com.orlands.manto.repositories.UserRepository;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(UserDomain user) {
        userRepository.save(user);
    }

    public Optional<UserDomain> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<UserDomain> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void updatePassword(UserDomain user, String rawPassword) {
        String encoded = passwordEncoder.encode(rawPassword);
        user.setPassword(encoded);
        userRepository.save(user);
    }

    public void deleteUser(UserDomain user) {
        userRepository.delete(user);
    }

}
