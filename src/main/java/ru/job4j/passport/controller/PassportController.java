package ru.job4j.passport.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.passport.domain.Passport;
import ru.job4j.passport.service.PassportService;

import java.util.List;
import java.util.Optional;

/**
 * 3. Мидл
 * 3.5. Микросервисы
 * 3.5.3. Межпроцессорное взаимодействие
 * 1. Синхронный обмен сообщений [#458496]
 * PassportController rest controller модели Passport.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 07.09.2022
 */
@RestController
@RequestMapping("/api/passport")
public class PassportController {
    private final PassportService passportService;

    public PassportController(PassportService passportService) {
        this.passportService = passportService;
    }

    @PostMapping("/save")
    public Passport save(@RequestBody Passport passport) {
        return passportService.save(passport);
    }

    @PutMapping("/update")
    public ResponseEntity<Void> update(@RequestParam int id, @RequestBody Passport passport) {
        boolean status = passportService.update(id, passport);
        return ResponseEntity
                .status(status ? HttpStatus.OK : HttpStatus.NOT_FOUND)
                .build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam int id) {
        boolean status = passportService.delete(id);
        return ResponseEntity
                .status(status ? HttpStatus.OK : HttpStatus.NOT_FOUND)
                .build();
    }

    @GetMapping("/find")
    public Iterable<Passport> find(@RequestParam Optional<Integer> seria) {
        return seria.map(passportService::findAllSeria)
                .orElseGet(passportService::findAll);
    }

    @GetMapping("/unavaliabe")
    public Iterable<Passport> findUnavaliabe() {
        return passportService.findUnavaliabe();
    }

    @GetMapping("/find-replaceable")
    public Iterable<Passport> findReplaceable() {
        return passportService.findReplaceableThreeMonth();
    }
}
