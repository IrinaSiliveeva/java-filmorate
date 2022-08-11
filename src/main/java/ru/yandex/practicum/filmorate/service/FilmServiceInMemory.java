package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.filmstorage.FilmStorage;

import java.util.List;

@Service("filmInMemoryService")
@Getter
public class FilmServiceInMemory implements FilmService {
    private final FilmStorage filmStorage;

    public FilmServiceInMemory(@Qualifier("filmInMemory") FilmStorage inMemoryFilmStorage) {
        this.filmStorage = inMemoryFilmStorage;
    }


    @Override
    public void addLike(int filmId, int userId) {
        checkTransmittedId(userId);
        filmStorage.getById(filmId).getLikes().add(userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        checkTransmittedId(userId);
        if (!filmStorage.getById(filmId).getLikes().contains(userId)) {
            throw new NotFoundException(String.format("Пользователь с id %s не ставил лайк фильму", userId));
        }
        filmStorage.getById(filmId).getLikes().remove(userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    private void checkTransmittedId(int userId) {
        if (userId <= 0) {
            throw new NotFoundException(String.format("Не корректный id %s пользователя ", userId));
        }
    }
}
