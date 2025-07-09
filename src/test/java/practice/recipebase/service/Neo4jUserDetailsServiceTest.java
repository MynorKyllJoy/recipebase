package practice.recipebase.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import practice.recipebase.model.AppUser;
import practice.recipebase.model.AppUserRole;
import practice.recipebase.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class Neo4jUserDetailsServiceTest {
    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private Neo4jUserDetailsService userDetailsService;

    private AppUser createUser() {
        return new AppUser(
                "Max Mustermann", "MMMan", "email",
                "pwd", AppUserRole.USER, true, true
        );
    }

    @Test
    void testLoadUserByUsername() {
        given(userRepo.findByUsername(anyString()))
                .willReturn(Optional.of(this.createUser()));
        UserDetails actualUser = userDetailsService.loadUserByUsername("MMMan");
        assertThat(actualUser).isEqualTo(this.createUser());
    }

    @Test
    void testLoadUserByUsernameNotFoundException() {
        given(userRepo.findByUsername(anyString())).willReturn(Optional.empty());

        Exception ex = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("MMMan")
        );

        String actualMessage = ex.getMessage();
        String expectedMessage = "MMMan not found";

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }
}
