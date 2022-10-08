package ru.itm.gendata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Генератор данных для транс таблиц базы H2
 * Админка по localhost:2750
 * Таблицы должны быть созданы. Работает в режиме update.
 */
@SpringBootApplication
public class GendataApplication {
    public static void main(String[] args) {
        SpringApplication.run(GendataApplication.class, args);
    }
}
