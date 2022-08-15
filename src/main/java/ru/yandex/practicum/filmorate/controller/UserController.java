package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(@Qualifier("userDbService") UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.getInMemoryUserStorage().create(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.getInMemoryUserStorage().update(user);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getInMemoryUserStorage().getAll();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable("userId") int userId) {
        return userService.getInMemoryUserStorage().getById(userId);
    }

    @DeleteMapping
    public void deleteAllUsers() {
        userService.getInMemoryUserStorage().deleteAll();
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") int userId) {
        userService.getInMemoryUserStorage().delete(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable("userId") int userId,
                          @PathVariable("friendId") int friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable("userId") int userId,
                             @PathVariable("friendId") int friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getUserFriendList(@PathVariable("userId") int userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("userId") int uresId,
                                       @PathVariable("otherId") int otherId) {
        return userService.getCommonFriends(uresId, otherId);
    }
}
