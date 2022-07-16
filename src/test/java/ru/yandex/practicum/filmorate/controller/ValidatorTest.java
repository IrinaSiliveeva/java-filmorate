package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.filmstorage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.userstorage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidatorTest {
    private final InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
    private final InMemoryUserStorage userStorage = new InMemoryUserStorage();

    @Test
    public void test_01_ShouldValidationWithCorrectData() {
        User user = new User(1, "test@test.ru", "Test_log", "Test_Name",
                LocalDate.of(1995, 9, 6), new HashSet<>());
        Film film = new Film(1, "Test_Film", "Test", LocalDate.of(2000, 2, 2),
                190, new HashSet<>());
        filmStorage.addFilm(film);
        userStorage.createUser(user);
        assertEquals(user, userStorage.getUserById(user.getId()));
        assertEquals(film, filmStorage.getFilmById(film.getId()));
    }

    @Test
    public void test_02_ShouldValidationWithIncorrectId() {
        User user = new User(-1, "test@test.ru", "Test_log", "Test_Name",
                LocalDate.of(1995, 9, 6), new HashSet<>());
        Film film = new Film(-1, "Test_Film", "Test", LocalDate.of(2000, 2, 2),
                190, new HashSet<>());
        userStorage.createUser(user);
        filmStorage.addFilm(film);
        assertEquals(1, filmStorage.getFilmById(1).getId());
        assertEquals(1, userStorage.getUserById(1).getId());
    }

    @Test
    public void test_03_ShouldValidationWithIncorrectReleaseDateFilm() {
        Film film = new Film(-1, "Test_Film", "Test", LocalDate.of(1000, 2, 2),
                190, new HashSet<>());
        assertThrows(ValidationException.class, () -> filmStorage.addFilm(film));
    }

    @Test
    public void test_04_ShouldValidationWithEmptyUserName() {
        User user = new User(1, "test@test.ru", "Test_log", "",
                LocalDate.of(1995, 9, 6), new HashSet<>());
        userStorage.createUser(user);
        assertEquals(user.getLogin(), user.getName());
    }
}