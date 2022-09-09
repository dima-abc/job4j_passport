package ru.job4j.passport.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.job4j.passport.PassportApplication;
import ru.job4j.passport.domain.Passport;
import ru.job4j.passport.service.PassportService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 3. Мидл
 * 3.5. Микросервисы
 * 3.5.3. Межпроцессорное взаимодействие
 * 1. Синхронный обмен сообщений [#458496]
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 09.09.2022
 */
@SpringBootTest(classes = PassportApplication.class)
@AutoConfigureMockMvc
class PassportControllerTest {
    private static final String API_URL = "/api/passport";

    @MockBean
    private PassportService service;

    @Autowired
    private MockMvc mockMvc;

    public static String asJsonString(final Passport passport) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("seria", passport.getSeria());
        json.put("number", passport.getNumber());
        json.put("created", passport.getCreated());
        json.put("expiration", passport.getExpiration());
        return json.toString();
    }

    @Test
    void whenSave() throws Exception {
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().plusYears(5));
        when(service.save(passport)).thenReturn(passport);
        String saveUri = String.format("%s/save", API_URL);
        this.mockMvc
                .perform(post(saveUri)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(passport)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.number").value(passport.getNumber()));
    }

    @Test
    void whenUpdateThenStatusOk() throws Exception {
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().plusYears(5));
        when(service.update(1, passport)).thenReturn(true);
        String updateUrl = String.format("%s/update", API_URL);
        this.mockMvc
                .perform(MockMvcRequestBuilders.put(updateUrl)
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(passport)))
                .andExpect(status().isOk());
    }

    @Test
    void whenUpdateThenStatusNotFound() throws Exception {
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().plusYears(5));
        passport.setId(1);
        when(service.update(passport.getId(), passport)).thenReturn(false);
        String updateUri = String.format("%s/update", API_URL);
        this.mockMvc
                .perform(put(updateUri)
                        .param("id", String.valueOf(passport.getId()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(passport)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDeleteStatusOK() throws Exception {
        Passport passport = new Passport();
        passport.setId(1);
        when(service.delete(passport.getId())).thenReturn(true);
        String deleteUrl = String.format("%s/delete", API_URL);
        this.mockMvc
                .perform(delete(deleteUrl)
                        .param("id", String.valueOf(passport.getId())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void whenDeleteStatusNotFound() throws Exception {
        Passport passport = new Passport();
        passport.setId(1);
        when(service.delete(passport.getId())).thenReturn(false);
        String deleteUrl = String.format("%s/delete", API_URL);
        this.mockMvc
                .perform(delete(deleteUrl)
                        .param("id", String.valueOf(passport.getId())))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void whenFindAllMethodGetThen() throws Exception {
        String findUrl = String.format("%s/find", API_URL);
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().plusYears(5));
        passport.setId(1);
        when(service.findAll()).thenReturn(List.of(passport));
        this.mockMvc
                .perform(get(findUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].seria").value(passport.getSeria()));
    }

    @Test
    void whenFindSeriaMethodGetJsonThen() throws Exception {
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().plusYears(5));
        passport.setId(1);
        String findUrlSeria = String.format("%s/find?seria=%d", API_URL, passport.getSeria());
        when(service.findAllSeria(passport.getSeria())).thenReturn(List.of(passport));
        this.mockMvc
                .perform(get(findUrlSeria))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].number").value(passport.getNumber()));
    }

    @Test
    void whenFindUnavaliabe() throws Exception {
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().minusYears(1));
        Passport passport1 = Passport.of(7777, 321321,
                LocalDate.now().minusYears(1), LocalDate.now().minusDays(1));
        String unavaliabeUrl = String.format("%s/unavaliabe", API_URL);
        when(service.findUnavaliabe()).thenReturn(List.of(passport, passport1));
        this.mockMvc
                .perform(get(unavaliabeUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].number").value(passport.getNumber()))
                .andExpect(jsonPath("$[1].number").value(passport1.getNumber()));
    }

    @Test
    void whenFindReplaceable() throws Exception {
        Passport passport = Passport.of(3333, 123123,
                LocalDate.now().minusYears(5), LocalDate.now().plusMonths(1));
        Passport passport1 = Passport.of(7777, 321321,
                LocalDate.now().minusYears(1), LocalDate.now().plusDays(1));
        String replaceableUrl = String.format("%s/find-replaceable", API_URL);
        when(service.findReplaceableThreeMonth()).thenReturn(List.of(passport, passport1));
        this.mockMvc
                .perform(get(replaceableUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].number").value(passport.getNumber()))
                .andExpect(jsonPath("$[1].number").value(passport1.getNumber()));
    }
}