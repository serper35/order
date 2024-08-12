package ru.t1.Order.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import ru.t1.Order.dto.UserDTO;
import ru.t1.Order.exception.UserNotFoundException;
import ru.t1.Order.mapper.Mapper;
import ru.t1.Order.model.User;
import ru.t1.Order.repository.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import ru.t1.Order.exception.UserAlreadyExistException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private Mapper mapper;

    @Test
    public void testCreateUserLogging(CapturedOutput output) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Test User");

        User user = new User();
        user.setName("Test User");

        when(userRepository.existsByName(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(mapper.convertToEntity(any(UserDTO.class))).thenReturn(user);
        when(mapper.convertToDTO(any(User.class))).thenReturn(userDTO);

        userService.createUser(userDTO);

        // Проверка логов
        assertThat(output.getOut()).contains("Calling method createUser with args");
        assertThat(output.getOut()).contains("Returning from method createUser with result");
    }

    @Test
    public void testCreateUserThrowsExceptionLogging(CapturedOutput output) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("User");

        when(userRepository.existsByName(anyString())).thenReturn(true);

        assertThrows(UserAlreadyExistException.class, () -> userService.createUser(userDTO));

        // Проверка логов
        assertThat(output.getOut()).contains("Calling method createUser with args");
        assertThat(output.getOut()).contains("Exception thrown in method createUser");
    }

    @Test
    public void testUpdateUserLogging(CapturedOutput output) {
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setName("User");

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId(1);
        updatedUserDTO.setName("Updated User");

        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setName("Updated User");

        when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(mapper.convertToEntity(any(UserDTO.class))).thenReturn(updatedUser);
        when(mapper.convertToDTO(any(User.class))).thenReturn(updatedUserDTO);

        userService.updateUser(updatedUserDTO);

        // Проверка логов
        assertThat(output.getOut()).contains("Calling method updateUser with args");
        assertThat(output.getOut()).contains("Returning from method updateUser with result");
    }

    @Test
    public void testGetUserLogging(CapturedOutput output) {
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setName("User");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        userDTO.setName("User");

        when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.of(existingUser));
        when(mapper.convertToDTO(any(User.class))).thenReturn(userDTO);

        userService.getUser(1);

        // Проверка логов
        assertThat(output.getOut()).contains("Calling method getUser with args");
        assertThat(output.getOut()).contains("Returning from method getUser with result");
    }

    @Test
    public void testDeleteUserLogging(CapturedOutput output) {
        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setName("User");

        when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.of(existingUser));

        userService.deleteUser(1);

        // Проверка логов
        assertThat(output.getOut()).contains("Calling method deleteUser with args");
        assertThat(output.getOut()).contains("Returning from method deleteUser");
    }

    @Test
    public void testDeleteUserThrowsExceptionLogging(CapturedOutput output) {
        when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1));

        // Проверка логов
        assertThat(output.getOut()).contains("Calling method deleteUser with args");
        assertThat(output.getOut()).contains("Exception thrown in method deleteUser");
    }
}