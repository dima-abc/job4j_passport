package ru.job4j.passport.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.passport.domain.Passport;

import java.time.LocalDate;

/**
 * 3. Мидл
 * 3.5. Микросервисы
 * 3.5.3. Межпроцессорное взаимодействие
 * 1. Синхронный обмен сообщений [#458496]
 * PassportRepository хранилище данных.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 07.09.2022
 */
@Repository
public interface PassportRepository extends CrudRepository<Passport, Integer> {

    Iterable<Passport> findAllBySeria(int seria);

    /**
     * Поиск паспортов, дата окончания которых находится
     * до указанной даты.
     * Например, если необходима найти просроченные паспорта то указываем текущую дату.
     *
     * @param date LocalDate
     * @return Iterable
     */
    Iterable<Passport> findAllByExpirationBefore(LocalDate date);

    /**
     * Поиск паспортов дата окончания которых попадает в диапазон.
     * Например, если нужна найти паспорта, которые заканчиваются в следующие 3 месяца,
     * то: start = 'Текущая дата'
     * end = 'Текущая дата' + 3 месяца.
     *
     * @param start Start date
     * @param end   End date
     * @return Iterable
     */
    Iterable<Passport> findAllByExpirationBetween(LocalDate start, LocalDate end);

}
