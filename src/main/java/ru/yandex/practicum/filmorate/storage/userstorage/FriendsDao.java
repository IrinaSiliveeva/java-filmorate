package ru.yandex.practicum.filmorate.storage.userstorage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Component
public class FriendsDao {
    private final JdbcTemplate jdbcTemplate;

    public FriendsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private void checkId(int userId, int friendId) {
        String sqlQuery = "select count(*) from USERS where USER_ID = ?";
        Integer result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, userId);
        if (result == 0) {
            throw new NotFoundException(String.format("Пользователя с id %s не найден", userId));
        }
        sqlQuery = "select count(*) from USERS where USER_ID = ?";
        result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, friendId);
        if (result == 0) {
            throw new NotFoundException(String.format("Друга id %s не найден", friendId));
        }
    }

    public void addFriend(int userId, int friendId) {
        checkId(userId, friendId);
        String sqlQuery = "insert into FRIENDS (USER_ID, FRIEND_ID, FRIENDS_STATUS) VALUES (?, ?,false)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        changeFriendStatus(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        checkId(userId, friendId);
        String sqlQuery = "delete from FRIENDS where USER_ID = ? and FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    public List<User> getUserFriends(int userId) {
        String sqlQuery = "select u.* from USERS u, FRIENDS f where f.USER_ID =? and u.USER_ID = FRIEND_ID";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId);
    }

    public Collection<User> getCommonFriends(int userId, int otherUserId) {
        String sqlQuery = "select u.* from USERS u, FRIENDS l, FRIENDS r " +
                "where u.USER_ID = l.FRIEND_ID and u.USER_ID = r.FRIEND_ID and l.USER_ID = ? and r.USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId, otherUserId);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder().id(resultSet.getInt("user_id"))
                .name(resultSet.getString("name"))
                .login(resultSet.getString("login"))
                .email(resultSet.getString("email"))
                .birthday(LocalDate.parse(resultSet.getString("birthday"))).build();
    }

    private void changeFriendStatus(int userId, int friendsId) {
        String sqlQuery = "select count(*) from FRIENDS where FRIEND_ID = ? and USER_ID = ?";
        Integer result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, userId, friendsId);
        if (result == 1) {
            sqlQuery = "update FRIENDS set FRIENDS_STATUS = true where USER_ID = ? and FRIEND_ID = ?";
            jdbcTemplate.update(sqlQuery, userId, friendsId);
        }
    }
}
