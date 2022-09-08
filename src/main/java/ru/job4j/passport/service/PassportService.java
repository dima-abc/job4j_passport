package ru.job4j.passport.service;

import org.springframework.stereotype.Service;
import ru.job4j.passport.domain.Passport;
import ru.job4j.passport.repository.PassportRepository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * 3. Мидл
 * 3.5. Микросервисы
 * 3.5.3. Межпроцессорное взаимодействие
 * 1. Синхронный обмен сообщений [#458496]
 * PassportService слой бизнес логики управления моделью Passport.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 07.09.2022
 */
@Service
public class PassportService {
    private final PassportRepository passportRepository;
    private static final LocalDate THREE_MONTH_REPLACEABLE = LocalDate.now().plusMonths(3);
    private static final LocalDate UNAVALIADE = LocalDate.now();

    public PassportService(PassportRepository passportRepository) {
        this.passportRepository = passportRepository;
    }

    /**
     * Сохранить данные паспорта
     *
     * @param passport Passport
     * @return Passport
     */
    public Passport save(Passport passport) {
        return this.passportRepository.save(passport);
    }

    /**
     * Обновить данные паспорта
     *
     * @param id       ID passport
     * @param passport Passport
     * @return boolean
     */
    public boolean update(int id, Passport passport) {
        Optional<Passport> rsl = this.passportRepository.findById(id);
        rsl.ifPresent(p -> {
            passport.setId(p.getId());
            this.passportRepository.save(passport);
        });
        return rsl.isPresent();
    }

    /**
     * Поиск passport по ID
     *
     * @param id ID passport
     * @return Optional Passport
     */
    public Optional<Passport> findById(int id) {
        return this.passportRepository.findById(id);
    }

    /**
     * Удалить данные паспорта по его ID
     *
     * @param id ID Passport
     * @return boolean
     */
    public boolean delete(int id) {
        Optional<Passport> rsl = this.passportRepository.findById(id);
        rsl.ifPresent(p -> this.passportRepository.deleteById(p.getId()));
        return rsl.isPresent();
    }

    /**
     * Загрузить все паспорта
     *
     * @return Iterable
     */
    public Iterable<Passport> findAll() {
        return this.passportRepository.findAll();
    }

    /**
     * Загрузить паспорта с заданной серией
     *
     * @param seria int Serial passport
     * @return Iterable
     */
    public Iterable<Passport> findAllSeria(int seria) {
        return this.passportRepository.findAllBySeria(seria);
    }

    /**
     * Загрузить паспорта чей срок вышел
     *
     * @return Iterable
     */
    public Iterable<Passport> findUnavaliabe() {
        return this.passportRepository.findAllByExpirationBefore(UNAVALIADE);
    }

    /**
     * Загрузить паспорта, которые нужно заменить в ближайшие 3 месяца
     *
     * @return Iterable
     */
    public Iterable<Passport> findReplaceableThreeMonth() {
        return this.passportRepository.findAllByExpirationBetween(UNAVALIADE, THREE_MONTH_REPLACEABLE);
    }
}
