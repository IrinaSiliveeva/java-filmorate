package ru.yandex.practicum.filmorate.storage.userstorage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component("userDbStorage")
public class UserDao implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private void validation(User user) {
        if (user.getName().isBlank() || user.getName() == null) {
            user.setName(user.getLogin());
        }
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder().id(resultSet.getInt("user_id"))
                .name(resultSet.getString("name"))
                .login(resultSet.getString("login"))
                .email(resultSet.getString("email"))
                .birthday(LocalDate.parse(resultSet.getString("birthday"))).build();
    }

    private void checkId(int userId) {
        String sqlQuery = "select count(*) from USERS where USER_ID = ?";
        Integer result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, userId);
        if (result == 0) {
            throw new NotFoundException(String.format("Пользователь с id %s не найден", userId));
        }
    }

    @Override
    public User create(User user) {
        validation(user);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users").usingGeneratedKeyColumns("user_id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue());
        return user;
    }

    @Override
    public User update(User user) {
        checkId(user.getId());
        validation(user);
        String sqlQuery = "update USERS set LOGIN = ?, NAME = ?, EMAIL = ?, BIRTHDAY = ? where USER_ID = ?";
        jdbcTemplate.update(sqlQuery, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public Collection<User> getAll() {
        String sqlQuery = "select * from USERS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User getById(int userId) {
        checkId(userId);
        String sqlQuery = "select * from USERS where USER_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, userId);
    }

    @Override
    public void delete(int userId) {
        String sqlQuery = "delete from USERS where USER_ID = ?";
        jdbcTemplate.update(sqlQuery, userId);
    }

    @Override
    public void deleteAll() {
        String sqlQuery = "delete from USERS";
        jdbcTemplate.update(sqlQuery);
    }

    @Override
    public Map<Integer, User> getUsers() {
        return null;
    }
}
