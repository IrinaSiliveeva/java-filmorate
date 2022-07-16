package ru.yandex.practicum.filmorate.storage.userstorage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user);

    Collection<User> getAllUsers();

    User getUserById(int userId);

    void deleteUser(int userId);

    void deleteAllUsers();
}
