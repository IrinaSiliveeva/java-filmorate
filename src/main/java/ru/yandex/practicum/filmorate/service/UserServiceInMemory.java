package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.UserServiceConflict;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.userstorage.UserStorage;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service("userInMemoryService")
@Getter
public class UserServiceInMemory implements UserService {
    private final UserStorage inMemoryUserStorage;

    @Autowired
    public UserServiceInMemory(@Qualifier("userInMemoryStorage") UserStorage inMemoryUserStorage) {
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

    @Override
    public void addFriend(int userId, int friendId) {
        checkTransmittedId(userId, friendId);
        if (userId == friendId) {
            throw new UserServiceConflict("Нельзя самого себя добавить в друзья");
        }
        if (inMemoryUserStorage.getById(userId).getFriends().contains(friendId)) {
            throw new UserServiceConflict(String.format("Пользователь с id %s уже в друзьях", friendId));
        }
        inMemoryUserStorage.getById(userId).getFriends().add(friendId);
        inMemoryUserStorage.getById(friendId).getFriends().add(userId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        checkTransmittedId(userId, friendId);
        if (userId == friendId) {
            throw new UserServiceConflict("Нельзя удалить самого себя из друзей");
        }
        if (!inMemoryUserStorage.getById(userId).getFriends().contains(friendId)) {
            throw new NotFoundException(String.format("Друга с id %s нет у вас в друзьях", friendId));
        }
        inMemoryUserStorage.getById(userId).getFriends().remove(friendId);
        inMemoryUserStorage.getById(friendId).getFriends().remove(userId);
    }

    @Override
    public List<User> getFriends(int userId) {
        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException(String.format("Пользователя с id %s не найдено", userId));
        }
        List<User> friendsList = new ArrayList<>();
        User user = inMemoryUserStorage.getById(userId);
        for (int friendId : user.getFriends()) {
            friendsList.add(inMemoryUserStorage.getById(friendId));
        }
        return friendsList;
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherUserId) {
        checkTransmittedId(userId, otherUserId);
        List<Integer> mutualIdList = inMemoryUserStorage.getById(userId).getFriends().stream()
                .filter(inMemoryUserStorage.getById(otherUserId).getFriends()::contains)
                .collect(toList());
        List<User> mutualUserList = new ArrayList<>();
        for (Integer mutualId : mutualIdList) {
            mutualUserList.add(inMemoryUserStorage.getById(mutualId));
        }
        return mutualUserList;

    }
}
