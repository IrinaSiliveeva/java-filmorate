package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    private int incrementId() {
        return id++;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        user.setId(incrementId());
        validation(user);
        users.put(user.getId(), user);
        return user;
    }

    public void validation(User user) {
        if (user.getName().isBlank() || user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getId() <= 0) {
            throw new ValidationException("Неккоректный id");
        }
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        validation(user);
        users.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }
}
