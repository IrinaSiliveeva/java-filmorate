package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.filmstorage.MpaDao;

import java.util.Collection;

@Service
public class MpaService {
    private final MpaDao mpaDao;

    public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public Mpa getMpaById(int mpaId) {
        return mpaDao.getMpa(mpaId);
    }

    public Collection<Mpa> getAllMpa() {
        return mpaDao.getAllMpa();
    }
}
