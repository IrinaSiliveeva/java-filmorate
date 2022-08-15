package ru.yandex.practicum.filmorate.storage.filmstorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
public class MpaDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder().id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("mpa")).build();
    }

    private void checkId(int mpaId) {
        String sqlQuery = "select count(*) from RATINGS where MPA_ID = ?";
        Integer result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, mpaId);
        if (result == 0) {
            throw new NotFoundException(String.format("mpa с id %s не найден", mpaId));
        }
    }

    public Mpa getMpa(int mpaId) {
        checkId(mpaId);
        String sqlQuery = "select * from RATINGS where MPA_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, mpaId);
    }

    public Collection<Mpa> getAllMpa() {
        String sqlQuery = "select * from RATINGS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }
}
