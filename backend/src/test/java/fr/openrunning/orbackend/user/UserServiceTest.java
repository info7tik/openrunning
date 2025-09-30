package fr.openrunning.orbackend.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.openrunning.model.database.user.User;
import fr.openrunning.model.database.user.UserRepository;
import fr.openrunning.model.exception.OpenRunningException;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Test
    public void getEmail(@Mock UserRepository repository) throws OpenRunningException {
        String email = "user@monemail.com";
        User user = new User();
        user.setEmail(email);
        when(repository.findById(anyInt())).thenReturn(Optional.of(user));
        UserService service = new UserService(repository, null, null);
        assertEquals(email, service.getUserEmail(0));
    }

    @Test
    public void getEmailWithNoUser(@Mock UserRepository repository) throws OpenRunningException {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());
        UserService service = new UserService(repository, null, null);
        assertThrows(OpenRunningException.class, () -> {
            service.getUserEmail(0);
        });
    }
}
