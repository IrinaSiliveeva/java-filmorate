package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ControllerValidatorTest {
    private final FilmController filmController = new FilmController();
    private final UserController userController = new UserController();

    @Test
    public void test_01_ShouldValidationWithCorrectData() {
        User user = new User(1, "test@test.ru", "Test_log", "Test_Name",
                LocalDate.of(1995, 9, 6));
        Film film = new Film(1, "Test_Film", "Test", LocalDate.of(2000, 2, 2),
                190);
        filmController.validation(film);
        userController.validation(user);
    }

    @Test
    public void test_02_ShouldValidationWithIncorrectId() {
        User user = new User(-1, "test@test.ru", "Test_log", "Test_Name",
                LocalDate.of(1995, 9, 6));
        Film film = new Film(-1, "Test_Film", "Test", LocalDate.of(2000, 2, 2),
                190);
        assertThrows(ValidationException.class, () -> filmController.validation(film));
        assertThrows(ValidationException.class, () -> userController.validation(user));
    }

    @Test
    public void test_03_ShouldValidationWithIncorrectReleaseDateFilm() {
        Film film = new Film(-1, "Test_Film", "Test", LocalDate.of(1000, 2, 2),
                190);
        assertThrows(ValidationException.class, () -> filmController.validation(film));
    }

    @Test
    public void test_04_ShouldValidationWithEmptyUserName() {
        User user = new User(1, "test@test.ru", "Test_log", "",
                LocalDate.of(1995, 9, 6));
        userController.updateUser(user);
        assertEquals(user.getLogin(), user.getName());
    }
}
