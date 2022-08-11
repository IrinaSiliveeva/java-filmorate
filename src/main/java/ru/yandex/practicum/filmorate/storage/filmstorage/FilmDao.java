package ru.yandex.practicum.filmorate.storage.filmstorage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component("dbFilm")
public class FilmDao implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaDao mpaDao;
    private final GenreDao genreDao;
    private final LikeDao likeDao;

    public FilmDao(JdbcTemplate jdbcTemplate, MpaDao mpaDao, GenreDao genreDao, LikeDao likeDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDao = mpaDao;
        this.genreDao = genreDao;
        this.likeDao = likeDao;
    }

    private void validation(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Некорректная дата релиза");
        }
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder().id(resultSet.getInt("film_id"))
                .name(resultSet.getString("film_title"))
                .description(resultSet.getString("description"))
                .releaseDate(LocalDate.parse(resultSet.getString("release_date")))
                .duration(resultSet.getInt("duration"))
                .mpa(mpaDao.getMpa(resultSet.getInt("mpa_id")))
                .rate(resultSet.getInt("film_rate"))
                .Genres((List<Genre>) genreDao.getFilmGenres(resultSet.getInt("film_id")))
                .likes(likeDao.getUsersLike(resultSet.getInt("film_id")))
                .build();
    }

    private void checkId(int filmId) {
        String sqlQuery = "select count(*) from FILMS where FILM_ID = ?";
        int result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, filmId);
        if (result == 0) {
            throw new NotFoundException(String.format("фильм с id %s не найден", filmId));
        }
    }

    private void deleteFilmGenres(int filmId) {
        String sqlQuery = "delete from FILM_GENRE where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    private void addFilmGenres(Film film) {
        if (film.getGenres() != null) {
            String sqlQuery = "insert into FILM_GENRE (FILM_ID, GENRE_ID) values (?, ?)";
            for (Genre genre : film.getGenres().stream().distinct().collect(Collectors.toList())) {
                jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
            }
        }
    }

    @Override
    public Film add(Film film) {
        validation(film);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films").usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue());
        if (film.getGenres() != null) {
            addFilmGenres(film);
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        validation(film);
        checkId(film.getId());
        if (film.getGenres() != null) {
            deleteFilmGenres(film.getId());
            addFilmGenres(film);
        }
        String sqlQuery = "update FILMS set FILM_TITLE = ?, DESCRIPTION = ?," +
                " RELEASE_DATE = ?, DURATION = ?, MPA_ID = ?, FILM_RATE = ? where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getRate(), film.getId());
        return getById(film.getId());
    }

    @Override
    public Collection<Film> getAll() {
        String sqlQuery = "select * from FILMS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film getById(int filmId) {
        checkId(filmId);
        String sqlQuery = "select * from films where film_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId);
    }

    @Override
    public void delete(int filmId) {
        checkId(filmId);
        String sqlQuery = "delete from FILMS where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public void deleteAll() {
        String sqlQuery = "delete from FILMS";
        jdbcTemplate.update(sqlQuery);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sqlQuery = "select films.* from FILMS left join FILM_LIKE FL on FILMS.FILM_ID = FL.FILM_ID" +
                " group by  FILMS.FILM_ID order by count(USER_ID) desc limit  ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }
}
