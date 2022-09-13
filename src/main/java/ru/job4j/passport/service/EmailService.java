package ru.job4j.passport.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.job4j.passport.dto.PassportDTO;

/**
 * 3. Мидл
 * 3.5. Микросервисы
 * 3.5.3. Межпроцессорное взаимодействие
 * 1. Синхронный обмен сообщений [#458496]
 * 2. Асинхронное взаимодействие [#458497]
 * EmailService сервис для отправки сообщений.
 *
 * @author Dmitry Stepanov, user Dima_Nout
 * @since 13.09.2022
 */
@Service
public class EmailService {

    @KafkaListener(topics = {"unavaliabe"})
    public void orderListener(ConsumerRecord<Integer, PassportDTO> input) {
        System.out.println(input.partition());
        System.out.println(input.key());
        System.out.println(input.value());
    }
}
