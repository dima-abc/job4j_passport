package ru.job4j.passport.dto;

import java.time.LocalDate;
import java.util.Objects;

/**
 * 3. Мидл
 * 3.5. Микросервисы
 * 3.5.3. Межпроцессорное взаимодействие
 * 1. Синхронный обмен сообщений [#458496]
 * PassportDTO DTO модель.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 13.09.2022
 */
public class PassportDTO {
    private Integer seria;
    private Integer number;
    private String created;
    private String expiration;

    public static PassportDTO of(Integer seria, Integer number,
                                 String created, String expiration) {
        PassportDTO passportDTO = new PassportDTO();
        passportDTO.seria = seria;
        passportDTO.number = number;
        passportDTO.created = created;
        passportDTO.expiration = expiration;
        return passportDTO;
    }

    public Integer getSeria() {
        return seria;
    }

    public void setSeria(Integer seria) {
        this.seria = seria;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
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
        PassportDTO that = (PassportDTO) o;
        return Objects.equals(seria, that.seria) && Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seria, number);
    }

    @Override
    public String toString() {
        return "PassportDTO{seria=" + seria + ", number=" + number
                + ", created=" + created + ", expiration=" + expiration + '}';
    }
}
