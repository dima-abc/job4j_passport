package ru.job4j.passport.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * 3. Мидл
 * 3.5. Микросервисы
 * 3.5.3. Межпроцессорное взаимодействие
 * 1. Синхронный обмен сообщений [#458496]
 * Passport доменная модель.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 07.09.2022
 */
@Entity
@Table(name = "passports")
public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int serial;
    private int number;
    private LocalDate created;
    private LocalDate expiration;

    public static Passport of(int serial, int number, LocalDate created, LocalDate expiration) {
        Passport passport = new Passport();
        passport.serial = serial;
        passport.number = number;
        passport.created = created;
        passport.expiration = expiration;
        return passport;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public LocalDate getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDate expiration) {
        this.expiration = expiration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Passport passport = (Passport) o;
        return id == passport.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Passport{id=" + id + ", serial=" + serial
                + ", number=" + number + ", created=" + created
                + ", expiration=" + expiration + '}';
    }
}
