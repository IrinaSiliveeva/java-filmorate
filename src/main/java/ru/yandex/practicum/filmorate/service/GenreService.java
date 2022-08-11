package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.filmstorage.GenreDao;

import java.util.Collection;

@Service
public class GenreService {
    private final GenreDao genreDao;

    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public Genre getGenreById(int genreId) {
        return genreDao.getGenre(genreId);
    }

    public Collection<Genre> getAllGenres() {
        return genreDao.getAllGenres();
    }
}
