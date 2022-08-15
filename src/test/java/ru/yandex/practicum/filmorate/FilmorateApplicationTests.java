package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.filmstorage.FilmDao;
import ru.yandex.practicum.filmorate.storage.filmstorage.GenreDao;
import ru.yandex.practicum.filmorate.storage.filmstorage.MpaDao;
import ru.yandex.practicum.filmorate.storage.userstorage.UserDao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDao userStorage;
    private final User user = new User(1, "test@test.ru", "test", "name",
            LocalDate.of(1992, 1, 1), new HashSet<>());
    private final Film film = new Film(1, "Test Film", "test",
            LocalDate.of(1990, 2, 3),
            100, new HashSet<>(), Mpa.builder().id(1).build(), new ArrayList<>(), 1);
    private final MpaDao mpaDao;
    private final GenreDao genreDao;
    private final FilmDao filmDao;

    @Test
    public void testFindUserById() {
        userStorage.create(user);
        Optional<User> userOptional = Optional.ofNullable(userStorage.getById(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testFindMpaById() {
        Optional<Mpa> userOptional = Optional.ofNullable(mpaDao.getMpa(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id", 1));
    }

    @Test
    public void testFindGenreById() {
        Optional<Genre> userOptional = Optional.ofNullable(genreDao.getGenre(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("id", 1));
    }

    @Test
    public void testFindFilmById() {
        filmDao.add(film);
        Optional<Film> userOptional = Optional.ofNullable(filmDao.getById(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1));
    }
}