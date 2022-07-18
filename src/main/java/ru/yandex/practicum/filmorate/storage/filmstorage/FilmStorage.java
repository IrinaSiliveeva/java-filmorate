package ru.yandex.practicum.filmorate.storage.filmstorage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {

    Film add(Film film);

    Film update(Film film);

    Collection<Film> getAll();

    Film getById(int filmId);

    void delete(int filmId);

    void deleteAll();

    Map<Integer, Film> getFilms();
}
