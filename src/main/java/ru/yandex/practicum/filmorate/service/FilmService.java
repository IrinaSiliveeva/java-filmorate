package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.filmstorage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Getter
public class FilmService {
    private final FilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public void addLike(int filmId, int userId) {
        checkTransmittedId(filmId, userId);
        inMemoryFilmStorage.getById(filmId).getLikes().add(userId);
    }

    public void deleteLike(int filmId, int userId) {
        checkTransmittedId(filmId, userId);
        if (!inMemoryFilmStorage.getById(filmId).getLikes().contains(userId)) {
            throw new NotFoundException(String.format("Пользователь с id %s не ставил лайк фильму", userId));
        }
        inMemoryFilmStorage.getById(filmId).getLikes().remove(userId);
    }

    public List<Film> getPopularFilms(int count) {
        return inMemoryFilmStorage.getAll().stream()
                .sorted((p0, p1) -> p1.getLikes().size() - p0.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    private void checkTransmittedId(int filmId, int userId) {
        if (!inMemoryFilmStorage.getFilms().containsKey(filmId)) {
            throw new NotFoundException(String.format("Фильм с id %s не найден", filmId));
        }
        if (userId <= 0) {
            throw new NotFoundException(String.format("Не корректный id %s пользователя ", userId));
        }
    }
}
