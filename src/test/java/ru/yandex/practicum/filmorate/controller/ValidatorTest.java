package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.filmstorage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.userstorage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
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
                190, new HashSet<>(), Mpa.builder().build(), new ArrayList<>(), 1);
        filmStorage.add(film);
        userStorage.create(user);
        assertEquals(user, userStorage.getById(user.getId()));
        assertEquals(film, filmStorage.getById(film.getId()));
    }

    @Test
    public void test_02_ShouldValidationWithIncorrectId() {
        User user = new User(-1, "test@test.ru", "Test_log", "Test_Name",
                LocalDate.of(1995, 9, 6), new HashSet<>());
        Film film = new Film(-1, "Test_Film", "Test", LocalDate.of(2000, 2, 2),
                190, new HashSet<>(), Mpa.builder().build(), new ArrayList<>(), 1);
        userStorage.create(user);
        filmStorage.add(film);
        assertEquals(1, filmStorage.getById(1).getId());
        assertEquals(1, userStorage.getById(1).getId());
    }

    @Test
    public void test_03_ShouldValidationWithIncorrectReleaseDateFilm() {
        Film film = new Film(-1, "Test_Film", "Test", LocalDate.of(1000, 2, 2),
                190, new HashSet<>(), Mpa.builder().build(), new ArrayList<>(), 1);
        assertThrows(ValidationException.class, () -> filmStorage.add(film));
    }

    @Test
    public void test_04_ShouldValidationWithEmptyUserName() {
        User user = new User(1, "test@test.ru", "Test_log", "",
                LocalDate.of(1995, 9, 6), new HashSet<>());
        userStorage.create(user);
        assertEquals(user.getLogin(), user.getName());
    }
}
