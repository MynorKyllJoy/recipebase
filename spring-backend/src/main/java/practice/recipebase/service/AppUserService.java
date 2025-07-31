package practice.recipebase.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import practice.recipebase.model.AppUser;
import practice.recipebase.model.AppUserRole;
import practice.recipebase.repository.UserRepository;

@Service
public class AppUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JWTService jwtService;

    public String register(AppUser user) {
        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "Username already exists.";
        }
        String encodedPwd = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPwd);
        user.setRole(AppUserRole.USER);
        user.setEnabled(true);
        user.setLocked(false);
        AppUser newUser = userRepository.save(user);
        return jwtService.generateToken(newUser.getUsername());
    }

    public String verify(AppUser user) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        return jwtService.generateToken(user.getUsername());
    }
}
