package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    private int incrementId() {
        return id++;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        film.setId(incrementId());
        validation(film);
        films.put(film.getId(), film);
        return film;
    }

    public void validation(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Некорректная дата релиза");
        }
        if (film.getId() <= 0) {
            throw new ValidationException("Неккоректный id");
        }
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        validation(film);
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Нет фильма с таким id");
        }
        films.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }
}
