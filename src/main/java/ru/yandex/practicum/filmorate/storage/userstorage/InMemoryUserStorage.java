package ru.yandex.practicum.filmorate.storage.userstorage;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component("userInMemoryStorage")
@Getter
public class InMemoryUserStorage implements UserStorage {
    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    private int incrementId() {
        return id++;
    }

    private void validation(User user) {
        if (user.getName().isBlank() || user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getId() <= 0) {
            throw new NotFoundException("Неккоректный id");
        }
    }

    @Override
    public User create(User user) {
        user.setId(incrementId());
        validation(user);
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        validation(user);
        checkUserId(user.getId());
        if (user.getFriends() == null || user.getFriends().isEmpty()) {
            user.setFriends(users.get(user.getId()).getFriends());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User getById(int userId) {
        checkUserId(userId);
        return users.get(userId);
    }

    @Override
    public void delete(int userId) {
        checkUserId(userId);
        users.remove(userId);
    }

    @Override
    public void deleteAll() {
        users.clear();
    }

    private void checkUserId(int userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException(String.format("Пользователь с id %s не найден", userId));
        }
    }
}
