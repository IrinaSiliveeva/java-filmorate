package ru.yandex.practicum.filmorate.storage.filmstorage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class GenreDao {
    private final JdbcTemplate jdbcTemplate;

    public GenreDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder().id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("genre")).build();
    }

    private void checkId(int genreId) {
        String sqlQuery = "select count(*) from GENRES where GENRE_ID = ?";
        Integer result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, genreId);
        if (result == 0) {
            throw new NotFoundException(String.format("Жанра с id %s не найдено", genreId));
        }
    }

    public Genre getGenre(int genreId) {
        checkId(genreId);
        String sqlQuery = "select * from GENRES where GENRE_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, genreId);
    }

    public Collection<Genre> getAllGenres() {
        String sqlQuery = "select * from GENRES";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    public Collection<Genre> getFilmGenres(int filmId) {
        String sqlQuery = "select g.* from GENRES g, FILM_GENRE fg where g.GENRE_ID = fg.GENRE_ID and fg.FILM_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre, filmId);
    }

    public void deleteFilmGenres(int filmId) {
        String sqlQuery = "delete from FILM_GENRE where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    public void addFilmGenres(Film film) {
        if (film.getGenres() != null) {
            String sqlQuery = "insert into FILM_GENRE (FILM_ID, GENRE_ID) values (?, ?)";
            for (Genre genre : film.getGenres().stream().distinct().collect(Collectors.toList())) {
                jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
            }
        }
    }
}
