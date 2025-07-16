package practice.recipebase.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import practice.recipebase.model.AppUser;
import practice.recipebase.model.AppUserRole;
import practice.recipebase.repository.UserRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppUserServiceTest {
    @Mock
    UserRepository userRepo;

    @Spy
    @InjectMocks
    AppUserService userService;

    private AppUser createUser() {
        return new AppUser(
                "Max Mustermann", "MMMan", "email",
                "pwd", AppUserRole.USER, true, true
        );
    }

    @Test
    void testRegister() {
        AppUser user = this.createUser();
        doNothing().when(userService).register(any(AppUser.class));
        userService.register(user);
        verify(userService, times(1)).register(user);
    }

}
