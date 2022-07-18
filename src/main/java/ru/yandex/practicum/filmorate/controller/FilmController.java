package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.getInMemoryFilmStorage().add(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.getInMemoryFilmStorage().update(film);
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmService.getInMemoryFilmStorage().getAll();
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable("filmId") int filmId) {
        return filmService.getInMemoryFilmStorage().getById(filmId);
    }

    @DeleteMapping
    public void deleteAllFilms() {
        filmService.getInMemoryFilmStorage().deleteAll();
    }

    @DeleteMapping("/{filmId}")
    public void deleteFilmById(@PathVariable("filmId") int filmId) {
        filmService.getInMemoryFilmStorage().delete(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable("filmId") int filmId,
                        @PathVariable("userId") int userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable("filmId") int filmId,
                           @PathVariable("userId") int userId) {
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(value = "count", defaultValue = "10", required = false) int count) {
        return filmService.getPopularFilms(count);
    }
}
