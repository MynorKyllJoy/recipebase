package practice.recipebase.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import practice.recipebase.model.AppUser;
import practice.recipebase.repository.UserRepository;

@Service
public class AppUserService {
    @Autowired
    private UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    public void register(AppUser user) {
        String encodedPwd = encoder.encode(user.getPassword());
        user.setPassword(encodedPwd);
        userRepository.save(user);
    }
}
