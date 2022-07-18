package ru.yandex.practicum.filmorate.storage.filmstorage;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
@Getter
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    private int incrementId() {
        return id++;
    }

    private void validation(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Некорректная дата релиза");
        }
    }

    @Override
    public Film add(Film film) {
        film.setLikes(new HashSet<>());
        validation(film);
        film.setId(incrementId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        validation(film);
        checkId(film.getId());
        if (film.getLikes() == null || film.getLikes().isEmpty()) {
            film.setLikes(films.get(film.getId()).getLikes());
        }
        films.put(film.getId(), film);
        return film;
    }

    private void checkId(int filmId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException(String.format("Фильм с id %s не найден", filmId));
        }
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film getById(int filmId) {
        checkId(filmId);
        return films.get(filmId);
    }

    @Override
    public void delete(int filmId) {
        checkId(filmId);
        films.remove(filmId);
    }

    @Override
    public void deleteAll() {
        films.clear();
    }
}
