package ru.job4j.passport.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.passport.domain.Passport;

import java.time.LocalDate;

/**
 * 3. Мидл
 * 3.5. Микросервисы
 * 3.5.3. Межпроцессорное взаимодействие
 * 1. Синхронный обмен сообщений [#458496]
 * PassportStore хранилище данных.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 07.09.2022
 */
@Repository
public interface PassportStore extends CrudRepository<Passport, Integer> {

    Iterable<Passport> findAllBySerial(int serial);

    Iterable<Passport> findAllByExpirationAfter(LocalDate date);
}
