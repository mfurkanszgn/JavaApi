package com.example.demo.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
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
        then(studentRepository).should().save(student);
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
    void itShouldThrowIllegalArrgumentExceptionWhenStudentNotFoundOnDelete() {
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
        String newMail = "furkan@sezgin.com";

        //given
        given(studentRepository.findById(student.getId())).willReturn(Optional.of(student));
        underTestService.updateStudent(student.getId(), newMail, student.getEmail());

    }

//    @Test
//    void whenGivenIdshouldReturnIfUserFound() {
//        String email = "Fursez212@gmail.com";
//        Student student = new Student("Furkan122",
//                email,
//                LocalDate.of(2000, 02, 03));
//        given(studentRepository.findById(student.getId())).willReturn(Optional.ofNullable(null));
//
//
//    }

    @Test
    void itShouldNotUpdateUserNameWhenNewUserNameIsNull() {
        String email = "Fursez212@gmail.com";
        Student student = new Student("Furkan122",
                email,
                LocalDate.of(2000, 02, 03));
        String name = null;
        //When
        given(studentRepository.findById(student.getId())).willReturn(Optional.of(student));
        underTestService.updateStudent(student.getId(), name, student.getEmail());
        //Then
        assertThat(student.getName()).isNotEqualTo(name);


    }

    @Test
    void itShouldNotUpdateUserNameWhenNewUserNameSameNameWithStudent() {
        String email = "Fursez212@gmail.com";
        Student student = new Student("Furkan122",
                email,
                LocalDate.of(2000, 02, 03));
        String name = "Furkan122";
        given(studentRepository.findById(student.getId())).willReturn(Optional.of(student));
        underTestService.updateStudent(student.getId(), name, student.getEmail());
        assertThat(student.getName()).isEqualTo(name);
        //method cagırılıp cagırılmadıgını konrtol edecek miyim.
    }

    @Test
    void itShouldNotUpdateUserNameWhenNewUserNameLenghtNotGreaterThanZero() {
        String email = "Fursez212@gmail.com";
        Student student = new Student("Furkan122",
                email,
                LocalDate.of(2000, 02, 03));
        String name = "";
        given(studentRepository.findById(student.getId())).willReturn(Optional.of(student));
        underTestService.updateStudent(student.getId(), name, student.getEmail());
        assertThat(student.getName().length()).isNotEqualTo(0);

    }


    @Test
    void itShouldNotUpdateUserNameWhenNewUserEmailLenghtIsNotGreaterThanZero() {
        String email = "Fursez212@gmail.com";
        Student student = new Student("Furkan122",
                email,
                LocalDate.of(2000, 02, 03));
        String newEmail = "";
        given(studentRepository.findById(student.getId())).willReturn(Optional.of(student));
        underTestService.updateStudent(student.getId(), newEmail, student.getEmail());
        assertThat(student.getEmail()).isNotEqualTo(newEmail);

    }

    @Test
    void itShouldNotUpdateUserNameWhenNewUserEmailIsNull() {
        String email = "Fursez212@gmail.com";
        Student student = new Student("Furkan122",
                email,
                LocalDate.of(2000, 02, 03));
        String newEmail = null;
        given(studentRepository.findById(student.getId())).willReturn(Optional.of(student));
        underTestService.updateStudent(student.getId(), newEmail, student.getEmail());
        assertThat(student).isNotEqualTo(newEmail);

    }

    @Test
    void itShouldNotUpdateUserWhenNotFoundWithId() {
        String email = "Fursez212@gmail.com";

        Student student = new Student("Furkan122",
                email,
                LocalDate.of(2000, 02, 03));

        given(studentRepository.findById(student.getId())).willReturn(Optional.ofNullable(null));

        assertThatThrownBy(() -> underTestService.updateStudent(student.getId(), student.getName(), student.getEmail()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("student not exist");

    }

    @Test
    void itShouldNotUpdateUserNameWhenNewUserEmailIsNotGreaterThanZero() {
        String email = "Fursez212@gmail.com";
        Student student = new Student("Furkan122",
                email,
                LocalDate.of(2000, 02, 03));
        String newEmail = null;
        given(studentRepository.findById(student.getId())).willReturn(Optional.of(student));
        underTestService.updateStudent(student.getId(), newEmail, student.getEmail());
        assertThat(student.getEmail().length()).isNotEqualTo(0);

    }

    @Test
    void itShouldNotUpdateUserNameWhenNewUserEmailIsValidButTaken() {
        String email = "Fursez212@gmail.com";
        Student student = new Student("Furkan122",
                email,
                LocalDate.of(2000, 02, 03));
        given(studentRepository.findById(student.getId())).willReturn(Optional.of(student));
        given(studentRepository.selectExistsEmail(email)).willReturn(true);
        // given(studentRepository.findStudentsByEmail(email)).willReturn(Optional.of(student));


        assertThatThrownBy(() -> underTestService.updateStudent(student.getId(), student.getName(), student.getEmail()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("email taken");


    }
}