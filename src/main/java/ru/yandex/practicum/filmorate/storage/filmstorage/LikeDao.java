package ru.yandex.practicum.filmorate.storage.filmstorage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;

import java.util.HashSet;
import java.util.Set;

@Component
public class LikeDao {
    private final JdbcTemplate jdbcTemplate;

    public LikeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private void checkId(int filmId, int userId) {
        String sqlQuery = "select count(*) from FILMS where FILM_ID = ?";
        Integer filmResult = jdbcTemplate.queryForObject(sqlQuery, Integer.class, filmId);
        sqlQuery = "select count(*) from USERS where USER_ID = ?";
        Integer userResult = jdbcTemplate.queryForObject(sqlQuery, Integer.class, userId);
        if (filmResult == 0) {
            throw new NotFoundException(String.format("фильм с id %s не найден", filmId));
        }
        if (userResult == 0) {
            throw new NotFoundException(String.format("пользователь с id %s не найден", userId));
        }
    }

    public void addLike(int filmId, int userId) {
        checkId(filmId, userId);
        String sqlQuery = "insert into FILM_LIKE (FILM_ID, USER_ID) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public Set<Integer> getUsersLike(int filmId) {
        String sqlQuery = "select USER_ID from FILM_LIKE where FILM_ID = ?";
        return new HashSet<>(jdbcTemplate.queryForList(sqlQuery, Integer.class, filmId));
    }

    public void deleteLike(int filmId, int userId) {
        checkId(filmId, userId);
        String sqlQuery = "delete from FILM_LIKE where FILM_ID = ? and USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }
}
