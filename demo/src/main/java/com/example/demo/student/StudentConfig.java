package com.example.demo.student;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class StudentConfig {
    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository) {
        return args -> {
            Student Furkan = new Student(
                    "Furkan",
                    "Sezgin@gmail.com",
                    LocalDate.of(2002, 02, 03));
            Student Muhammet = new Student(
                    "Muhammet",
                    "Sezgin@gmail.com",
                    LocalDate.of(2000, 02, 03));
            Student ahmet2 = new Student(
                    "Ahmet2",
                    "Sezgin@gmail.com",
                    LocalDate.of(2000, 02, 03));

            repository.saveAll(List.of(Furkan, Muhammet,ahmet2));
        };

    }
}
