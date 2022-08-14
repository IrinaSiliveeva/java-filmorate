package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.userstorage.FriendsDao;
import ru.yandex.practicum.filmorate.storage.userstorage.UserStorage;

import java.util.List;

@Service("userDbService")
@Getter
public class UserServiceDb implements UserService {
    private final UserStorage userStorage;
    private final FriendsDao friendsDao;

    @Autowired
    public UserServiceDb(@Qualifier("userDbStorage") UserStorage userStorage, FriendsDao friendsDao) {
        this.userStorage = userStorage;
        this.friendsDao = friendsDao;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        friendsDao.addFriend(userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        friendsDao.deleteFriend(userId, friendId);
    }

    @Override
    public List<User> getFriends(int userId) {
        return friendsDao.getUserFriends(userId);
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherUserId) {
        return (List<User>) friendsDao.getCommonFriends(userId, otherUserId);
    }

    @Override
    public UserStorage getInMemoryUserStorage() {
        return userStorage;
    }
}
