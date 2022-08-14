package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.filmstorage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.filmstorage.LikeDao;

import java.util.List;

@Service("filmDbService")
@Getter
public class FilmServiceDb implements FilmService {
    private final FilmStorage filmStorage;
    private final LikeDao likeDao;

    public FilmServiceDb(@Qualifier("dbFilm") FilmStorage filmStorage, LikeDao likeDao) {
        this.filmStorage = filmStorage;
        this.likeDao = likeDao;
    }

    @Override
    public void addLike(int filmId, int userId) {
        likeDao.addLike(filmId, userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        likeDao.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

}
