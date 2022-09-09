package ru.job4j.passport.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;


import java.util.Collections;

import ru.job4j.passport.domain.Passport;

/**
 * 3. Мидл
 * 3.5. Микросервисы
 * 3.5.3. Межпроцессорное взаимодействие
 * 1. Синхронный обмен сообщений [#458496]
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 09.09.2022
 */
@Repository
public class PassportAPIStore {
    @Value("${api-url}")
    private String url;

    private final RestTemplate client;

    public PassportAPIStore(RestTemplate restTemplate) {
        this.client = restTemplate;
    }

    public Passport save(Passport passport) {
        return client.postForEntity(
                        String.format("%s/save", url),
                        passport,
                        Passport.class)
                .getBody();
    }

    public boolean update(int id, Passport passport) {
        return client.exchange(
                String.format("%s/update?id=%d", url, id),
                HttpMethod.PUT,
                new HttpEntity<>(passport),
                Void.class
        ).getStatusCode() != HttpStatus.NOT_FOUND;
    }

    public boolean delete(int id) {
        return client.exchange(
                String.format("%s/delete?id=%d", url, id),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class
        ).getStatusCode() != HttpStatus.NOT_FOUND;
    }

    public Iterable<Passport> findAll() {
        Iterable<Passport> body = getIterablePassport(String.format("%s/find", url));
        return body == null ? Collections.emptyList() : body;
    }

    public Iterable<Passport> findAllSerial(int serial) {
        Iterable<Passport> body = getIterablePassport(String.format("%s/find?seria=%d", url, serial));
        return body == null ? Collections.emptyList() : body;
    }

    public Iterable<Passport> findUnavaliabe() {
        Iterable<Passport> body = getIterablePassport(String.format("%s/unavaliabe", url));
        return body == null ? Collections.emptyList() : body;
    }

    public Iterable<Passport> findReplaceableThreeMonth() {
        Iterable<Passport> body = getIterablePassport(String.format("%s/find-replaceable", url));
        return body == null ? Collections.emptyList() : body;
    }

    private Iterable<Passport> getIterablePassport(String restUrl) {
        Iterable<Passport> body = client.exchange(
                restUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Iterable<Passport>>() {
                }
        ).getBody();
        return body == null ? Collections.emptyList() : body;
    }
}
