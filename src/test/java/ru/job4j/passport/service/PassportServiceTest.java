package ru.job4j.passport.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import ru.job4j.passport.domain.Passport;
import ru.job4j.passport.repository.PassportRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * 3. Мидл
 * 3.5. Микросервисы
 * 3.5.3. Межпроцессорное взаимодействие
 * 1. Синхронный обмен сообщений [#458496]
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 09.09.2022
 */
@TestPropertySource(locations = "classpath:application.properties")
@DataJpaTest
class PassportServiceTest {
    @Autowired
    TestEntityManager entityManager;
    @Autowired
    PassportRepository repository;

    PassportService service;

    @BeforeEach
    public void initService() {
        service = new PassportService(repository);
    }

    @Test
    void whenSaveServiceThenOk() {
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().plusYears(5));
        service.save(passport);
        Passport passportInDb = repository.findById(passport.getId()).orElse(null);
        assertThat(passport)
                .isEqualTo(passportInDb);
    }

    @Test
    void whenSaveServiceUniqueThenFail() {
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().plusYears(5));
        Passport passport1 = Passport.of(3333, 123123,
                LocalDate.now().minusYears(1), LocalDate.now().plusYears(1));
        service.save(passport);
        service.save(passport1);
        Passport passportInDb = repository.findById(passport1.getId()).orElse(null);
        assertThat(passportInDb)
                .isNull();
    }

    @Test
    void whenUpdateThenTrue() {
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().plusYears(5));
        service.save(passport);
        passport.setSeria(7777);
        boolean result = service.update(passport.getId(), passport);
        Passport expect = service.findById(passport.getId()).orElse(null);
        assertTrue(result);
        assertThat(passport)
                .isEqualTo(expect);
    }

    @Test
    void whenUpdateThenFalse() {
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().plusYears(5));
        passport.setId(1);
        boolean result = service.update(passport.getId(), passport);
        assertFalse(result);
    }

    @Test
    void whenDeleteThenTrue() {
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().plusYears(5));
        service.save(passport);
        boolean result = service.delete(passport.getId());
        Passport passportInDb = service.findById(passport.getId()).orElse(null);
        assertTrue(result);
        assertThat(passportInDb)
                .isNull();
    }

    @Test
    void whenDeleteThenFalse() {
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().plusYears(5));
        passport.setId(1);
        boolean result = service.delete(passport.getId());
        assertFalse(result);
    }

    @Test
    void findAll() {
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().plusYears(5));
        Passport passport1 = Passport.of(7777, 123123,
                LocalDate.now().minusYears(1), LocalDate.now().plusYears(1));
        service.save(passport);
        service.save(passport1);
        Iterable<Passport> result = service.findAll();
        Iterable<Passport> expect = List.of(passport, passport1);
        assertThat(result)
                .isEqualTo(expect);
    }

    @Test
    void whenFindAllSeria() {
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().plusYears(5));
        Passport passport1 = Passport.of(7777, 123123,
                LocalDate.now().minusYears(1), LocalDate.now().plusYears(1));
        Passport passport3 = Passport.of(3333, 321321,
                LocalDate.now().minusYears(5), LocalDate.now().plusYears(5));
        service.save(passport);
        service.save(passport1);
        service.save(passport3);
        Iterable<Passport> result = service.findAllSeria(passport.getSeria());
        Iterable<Passport> expect = List.of(passport, passport3);
        assertThat(result)
                .isEqualTo(expect);
    }

    @Test
    void whenFindUnavaliabe() {
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().minusMonths(1));
        Passport passport1 = Passport.of(7777, 123123,
                LocalDate.now().minusYears(1), LocalDate.now().minusDays(1));
        Passport passport3 = Passport.of(3333, 321321,
                LocalDate.now().minusYears(5), LocalDate.now().plusYears(1));
        service.save(passport);
        service.save(passport1);
        service.save(passport3);
        Iterable<Passport> result = service.findUnavaliabe();
        Iterable<Passport> expected = List.of(passport, passport1);
        assertThat(result)
                .isEqualTo(expected);
    }

    @Test
    void whenFindReplaceableThreeMonth() {
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().minusMonths(1));
        Passport passport1 = Passport.of(7777, 777777,
                LocalDate.now().minusYears(1), LocalDate.now().plusDays(60));
        Passport passport3 = Passport.of(3333, 321321,
                LocalDate.now().minusYears(5), LocalDate.now().plusYears(1));
        service.save(passport);
        service.save(passport1);
        service.save(passport3);
        Iterable<Passport> result = service.findReplaceableThreeMonth();
        Iterable<Passport> expected = List.of(passport1);
        assertThat(result)
                .isEqualTo(expected);
    }

    @Test
    void whenFindReplaceableThreeMonthIsEmpty() {
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().minusMonths(1));
        Passport passport1 = Passport.of(7777, 777777,
                LocalDate.now().minusYears(1), LocalDate.now().plusMonths(6));
        Passport passport3 = Passport.of(3333, 321321,
                LocalDate.now().minusYears(5), LocalDate.now().plusYears(1));
        service.save(passport);
        service.save(passport1);
        service.save(passport3);
        Iterable<Passport> result = service.findReplaceableThreeMonth();
        assertThat(result)
                .isEmpty();
    }
}