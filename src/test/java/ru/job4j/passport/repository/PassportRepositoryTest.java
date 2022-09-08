package ru.job4j.passport.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import ru.job4j.passport.domain.Passport;

import java.time.LocalDate;
import java.util.List;

/**
 * 3. Мидл
 * 3.5. Микросервисы
 * 3.5.3. Межпроцессорное взаимодействие
 * 1. Синхронный обмен сообщений [#458496]
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 08.09.2022
 */
@TestPropertySource(locations = "classpath:application.properties")
@DataJpaTest
class PassportRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    PassportRepository passportRepository;

    @Test
    void whenSaveThenFindIdOk() {
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().plusYears(5));
        passportRepository.save(passport);
        Passport passportInDb = passportRepository.findById(passport.getId()).orElse(new Passport());
        assertThat(passport)
                .isEqualTo(passportInDb);
    }

    @Test
    void whenFindAllIsEmpty() {
        Iterable<Passport> passports = passportRepository.findAll();
        assertThat(passports)
                .isEmpty();
    }

    @Test
    void whenFindAllThenTwo() {
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().plusYears(5));
        Passport passport1 = Passport.of(5555, 321321,
                LocalDate.now().minusYears(5), LocalDate.now().plusYears(5));
        passportRepository.save(passport);
        passportRepository.save(passport1);
        Iterable<Passport> result = passportRepository.findAll();
        Iterable<Passport> expect = List.of(passport, passport1);
        assertThat(result)
                .isEqualTo(expect);
    }

    @Test
    void whenFindAllBySeriaThenOne() {
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().plusYears(5));
        passportRepository.save(passport);
        Iterable<Passport> result = passportRepository.findAllBySeria(passport.getSeria());
        Iterable<Passport> expected = List.of(passport);
        assertThat(result)
                .isEqualTo(expected);
    }

    @Test
    void whenFindAllBySeriaIsEmpty() {
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().plusYears(5));
        passportRepository.save(passport);
        Iterable<Passport> result = passportRepository.findAllBySeria(1111);
        assertThat(result)
                .isEmpty();
    }

    @Test
    void whenFindAllByExpirationBeforeThenIsEmpty() {
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().plusDays(1));
        passportRepository.save(passport);
        Iterable<Passport> result = passportRepository.findAllByExpirationBefore(LocalDate.now());
        assertThat(result)
                .isEmpty();
    }

    @Test
    void whenFindAllByEExpirationBeforeThenOne() {
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().minusDays(1));
        Passport passport1 = Passport.of(5555, 321321,
                LocalDate.now().minusYears(5), LocalDate.now().minusMonths(1));
        passportRepository.save(passport);
        passportRepository.save(passport1);
        Iterable<Passport> result = passportRepository.findAllByExpirationBefore(LocalDate.now());
        Iterable<Passport> expect = List.of(passport, passport1);
        assertThat(result)
                .isEqualTo(expect);
    }

    @Test
    void whenFindAllByExpirationBetweenThenThreeMonth() {
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().plusMonths(5));
        Passport passport1 = Passport.of(5555, 321321,
                LocalDate.now().minusYears(5), LocalDate.now().plusMonths(1));
        passportRepository.save(passport);
        passportRepository.save(passport1);
        Iterable<Passport> result = passportRepository.findAllByExpirationBetween(LocalDate.now(), LocalDate.now().plusMonths(3));
        Iterable<Passport> expect = List.of(passport1);
        assertThat(result)
                .isEqualTo(expect);
    }

    @Test
    void whenFindAllByExpirationBetweenThenThreeMonthIsEmpty() {
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().plusMonths(5));
        Passport passport1 = Passport.of(5555, 321321,
                LocalDate.now().minusYears(5), LocalDate.now().plusDays(95));
        passportRepository.save(passport);
        passportRepository.save(passport1);
        Iterable<Passport> result = passportRepository.findAllByExpirationBetween(LocalDate.now(), LocalDate.now().plusMonths(3));
        assertThat(result)
                .isEmpty();
    }
}