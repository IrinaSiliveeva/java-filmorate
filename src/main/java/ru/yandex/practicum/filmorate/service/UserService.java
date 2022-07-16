package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.UserServiceConflict;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.userstorage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Getter
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    private void checkTransmittedId(int userId, int friendId) {
        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException(String.format("Пользователь с id %S не найден", userId));
        }
        if (!inMemoryUserStorage.getUsers().containsKey(friendId)) {
            throw new NotFoundException(String.format("Друга с id %S не найдено", userId));
        }
    }

    public void addFriend(int userId, int friendId) {
        checkTransmittedId(userId, friendId);
        if (userId == friendId) {
            throw new UserServiceConflict("Нельзя самого себя добавить в друзья");
        }
        if (inMemoryUserStorage.getUserById(userId).getFriends().contains(friendId)) {
            throw new UserServiceConflict(String.format("Пользователь с id %s уже в друзьях", friendId));
        }
        inMemoryUserStorage.getUserById(userId).getFriends().add(friendId);
        inMemoryUserStorage.getUserById(friendId).getFriends().add(userId);
    }

    public void deleteFriend(int userId, int friendId) {
        checkTransmittedId(userId, friendId);
        if (userId == friendId) {
            throw new UserServiceConflict("Нельзя удалить самого себя из друзей");
        }
        if (!inMemoryUserStorage.getUserById(userId).getFriends().contains(friendId)) {
            throw new NotFoundException(String.format("Друга с id %s нет у вас в друзьях", friendId));
        }
        inMemoryUserStorage.getUserById(userId).getFriends().remove(friendId);
        inMemoryUserStorage.getUserById(friendId).getFriends().remove(userId);
    }

    public List<User> getFriends(int userId) {
        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException(String.format("Пользователя с id %s не найдено", userId));
        }
        List<User> friendsList = new ArrayList<>();
        User user = inMemoryUserStorage.getUserById(userId);
        for (int friendId : user.getFriends()) {
            friendsList.add(inMemoryUserStorage.getUserById(friendId));
        }
        return friendsList;
    }

    public List<User> getCommonFriends(int userId, int otherUserId) {
        checkTransmittedId(userId, otherUserId);
        List<Integer> mutualIdList = inMemoryUserStorage.getUserById(userId).getFriends().stream()
                .filter(inMemoryUserStorage.getUserById(otherUserId).getFriends()::contains)
                .collect(toList());
        List<User> mutualUserList = new ArrayList<>();
        for (Integer mutualId : mutualIdList) {
            mutualUserList.add(inMemoryUserStorage.getUserById(mutualId));
        }
        return mutualUserList;

    }
}