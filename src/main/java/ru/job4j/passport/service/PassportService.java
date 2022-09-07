package ru.job4j.passport.service;

import org.springframework.stereotype.Service;
import ru.job4j.passport.domain.Passport;
import ru.job4j.passport.repository.PassportStore;

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
    private final PassportStore passportStore;
    private static final int THREE_MONTH_REPLACEABLE = 3;

    public PassportService(PassportStore passportStore) {
        this.passportStore = passportStore;
    }

    /**
     * Сохранить данные паспорта
     *
     * @param passport Passport
     * @return Passport
     */
    public Passport save(Passport passport) {
        return this.passportStore.save(passport);
    }

    /**
     * Обновить данные паспорта
     *
     * @param id       ID passport
     * @param passport Passport
     * @return boolean
     */
    public boolean update(int id, Passport passport) {
        Optional<Passport> rsl = this.passportStore.findById(id);
        rsl.ifPresent(p -> {
            passport.setId(p.getId());
            this.passportStore.save(passport);
        });
        return rsl.isPresent();
    }

    /**
     * Удалить данные паспорта по его ID
     *
     * @param id ID Passport
     * @return boolean
     */
    public boolean delete(int id) {
        Optional<Passport> rsl = this.passportStore.findById(id);
        rsl.ifPresent(p -> this.passportStore.deleteById(p.getId()));
        return rsl.isPresent();
    }

    /**
     * Загрузить все паспорта
     *
     * @return Iterable
     */
    public Iterable<Passport> findAll() {
        return this.passportStore.findAll();
    }

    /**
     * Загрузить паспорта с заданной серией
     *
     * @param serial int Serial passport
     * @return Iterable
     */
    public Iterable<Passport> findAllSerial(int serial) {
        return this.passportStore.findAllBySerial(serial);
    }

    /**
     * Загрузить паспорта чей срок вышел
     *
     * @return Iterable
     */
    public Iterable<Passport> findUnavaliabe() {
        return this.passportStore.findAllByExpirationAfter(LocalDate.now());
    }

    /**
     * Загрузить паспорта, которые нужно заменить в ближайшие 3 месяца
     *
     * @return Iterable
     */
    public Iterable<Passport> findReplaceableThreeMonth() {
        return this.passportStore.findAllByExpirationAfter(LocalDate.now().minusMonths(THREE_MONTH_REPLACEABLE));
    }
}
