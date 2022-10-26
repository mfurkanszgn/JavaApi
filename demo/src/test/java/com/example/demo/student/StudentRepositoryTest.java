package com.example.demo.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.spel.ast.FunctionReference;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentRepositoryTest {
    @Autowired
    StudentRepository underTest;


    @Test
    void itShouldSaveStudent() {
        //Given
        Student student = new Student("Furkan", "Furkan.sez@gmail.com", LocalDate.of(2000, 02, 03));
        //When
        underTest.save(student);
        //Then

        Optional<Student> optionalStudent = underTest.findStudentsByEmail(student.getEmail());
        assertThat(optionalStudent).isPresent()
                .hasValueSatisfying(c->{
                    assertThat(c.getId()).isEqualTo(student.getId());
                    assertThat(c.getName()).isEqualTo(student.getName());
                    assertThat(c.getAge()).isEqualTo(student.getAge());
                    assertThat(c).isEqualToComparingFieldByField(student);

                });

    }
}