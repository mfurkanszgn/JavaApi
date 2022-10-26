package com.example.demo.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    private StudentRepository studentRepository = mock(StudentRepository.class);
    private StudentService underTestService;

    @BeforeEach
    void setUp() {

        underTestService = new StudentService(studentRepository);
    }

    @Test
    void itShouldGetStudents() {
        //Given
        //When
        underTestService.getStudents();
        //Then
        verify(studentRepository).findAll();
    }

    @Test
    void itShouldAddNewStudent() {
        //Given
        String email = "Furkan.sez@gmail.com";
        Student student = new Student("Furkan", email, LocalDate.of(2000, 02, 03));
        //When
        underTestService.addNewStudent(student);
        //Then
        ArgumentCaptor<Student> studentArgumentCaptor
                = ArgumentCaptor.forClass(Student.class);

        verify(studentRepository).
                save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();

        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void willThrowWhenEmailTaken() {
        //Given
        String email = "Fursez212@gmail.com";
        Student student = new Student("Furkan122", email, LocalDate.of(2000, 02, 03));
        given(studentRepository.selectExistsEmail(student.getEmail())).willReturn(true);
        //When

        //Then


        assertThatThrownBy(() -> underTestService.addNewStudent(student))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("email taken");
        verify(studentRepository, never()).save(any());
    }

    @Test
    void itShouldDeleteStudent() {
        //Given
        String email = "Fursez212@gmail.com";
        Student student = new Student("Furkan122",
                email,
                LocalDate.of(2000, 02, 03));
        given(studentRepository.existsById(student.getId())).willReturn(true);

        //When
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));
        //Then
        underTestService.deleteStudent(student.getId());
        verify(studentRepository).deleteById(student.getId());

    }
    @Test
     void willThrowWhenIdNotFound(){
         //Given
         String email = "Fursez212@gmail.com";
         Student student = new Student("Furkan122",
                 email,
                 LocalDate.of(2000, 02, 03));
         //When
        given(studentRepository.existsById(student.getId())).willReturn(false);
        assertThatThrownBy(() -> underTestService.deleteStudent(student.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("student id  not exist");
         //Then
        verify(studentRepository, never()).deleteById(any());

     }

    @Test
    void itShouldUpdateStudent() {
        String email = "Fursez212@gmail.com";
        Student student = new Student("Furkan122",
                email,
                LocalDate.of(2000, 02, 03));
        Student temp = new Student("temp",
                "temp",
                LocalDate.of(2000, 02, 03));

        //given
        given(studentRepository.findById(student.getId())).willReturn(Optional.of(student));
        underTestService.updateStudent(student.getId(),student.getName(),student.getEmail());
    }

}