package ru.yandex.practicum.filmorate.exeption;

public class UserServiceConflict extends RuntimeException {

    public UserServiceConflict(String message) {
        super(message);
    }
}
