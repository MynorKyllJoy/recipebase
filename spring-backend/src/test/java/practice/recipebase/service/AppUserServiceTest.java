package practice.recipebase.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import practice.recipebase.model.AppUser;
import practice.recipebase.model.AppUserRole;
import practice.recipebase.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppUserServiceTest {
    @Mock
    UserRepository userRepo;

    @Mock
    PasswordEncoder passwordEncoder;

    @Spy
    @InjectMocks
    AppUserService userService;

    private AppUser createUser() {
        return new AppUser(
                "Max Mustermann", "MMMan", "email",
                "pwd", AppUserRole.USER, true, true
        );
    }
/*
    @Test
    void testRegister() {
        AppUser expectedUser = this.createUser();
        given(userRepo.save(expectedUser))
                .willReturn(expectedUser);

        given(passwordEncoder.encode(expectedUser.getPassword()))
                .willReturn(new BCryptPasswordEncoder(12).encode(expectedUser.getPassword()));
        AppUser actualUser = userService.register(expectedUser);
        assertThat(actualUser).isEqualTo(expectedUser);
    }
*/
}
