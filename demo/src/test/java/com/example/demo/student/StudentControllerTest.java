package com.example.demo.student;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = StudentController.class)
@AutoConfigureMockMvc
class StudentControllerTest {
    @MockBean
    private StudentService studentService;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentRepository studentRepository;//= mock(StudentRepository.class);
    @Autowired
    private MockMvc mockMvc;
    private StudentController underStudentControl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underStudentControl = new StudentController(studentService);


    }

    @Test
    void itShouldCreateMockMvc() {
        assertNotNull(mockMvc);
    }

    @Test
    void itShouldGetStudents() throws Exception {
        //Given

        //When
        when(studentService.getStudents()).thenReturn(List.of(
                new Student(
                        "Furkan",
                        "Furka2n@gmail.com",
                        LocalDate.of(2000, 02, 03))));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].name").value("Furkan"))
                .andExpect(jsonPath("$[0].email").value("Furka2n@gmail.com"))
                .andExpect(jsonPath("$[0].dob").value("2000-02-03"));


        //Then
        verify(studentService, times(1)).getStudents();

    }

    @Test
    void itShouldRegisterNewStudent() throws Exception {
        //Given
        //given(studentService.addNewStudent(any(Student.class))).willAnswer((invocation)-> invocation.getArgument(0));

        Student student = new Student("Furkan", "Furkan.gmail", LocalDate.of(2000, 02, 03));
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        ResultActions resultActions = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/student/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student))
                )
                .andExpect(status().isCreated());
        verify(studentService, times(1)).addNewStudent(studentArgumentCaptor.capture());
        verify(studentService).addNewStudent(any(Student.class));
        assertThat(studentArgumentCaptor.getValue().getName()).isEqualTo("Furkan");
        assertThat(studentArgumentCaptor.getValue().getEmail()).isEqualTo("Furkan.gmail");
        assertThat(studentArgumentCaptor.getValue().getDob()).isEqualTo(LocalDate.of(2000, 02, 03));
        //When

        //Then


    }

    public class JsonUtil {
        public static byte[] toJson(Object object) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return mapper.writeValueAsBytes(object);
        }
    }

    @Test
    void itShouldDeleteStudent() throws Exception {
        //Given
        Long studentId = 1L;
        Student student = new Student("Furkan",
                "Furkan.gmail",
                LocalDate.of(2000, 02, 03));

        given(studentRepository.findById(student.getId())).willReturn(Optional.of(student));
        student.setId(studentId);
        //When
        doNothing().when(studentService).deleteStudent(student.getId());
        //Then
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/student/" + student.getId().toString())
                        .content(objectMapper.writeValueAsString(student))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

    @Test
    void itShouldThrowExceptionWhenUserDoenstExist() throws Exception {
        //given
        // Long studentId = 1L;
        Student student = new Student("Furkan",
                "Furkan.gmail",
                LocalDate.of(2000, 02, 03));
        student.setId(null);
        //Student Repo student bulmaya calıstıgında  geriye student donmemeli
        given(studentRepository.findById(student.getId())).willReturn(null);
        // when delete student
        underStudentControl.deleteStudent(student.getId());

        // then
        Assertions.assertThat(student.getId()).isEqualTo(null);
        verify(studentService, never()).deleteStudent(any(Long.TYPE));
    }

//    @Test
//    void itShouldUpdateStudent() throws Exception {
//        //Given
//        Long studentId = 1L;
//        Student student = new Student("Furkan",
//                "Furkan.gmail",
//                LocalDate.of(2000, 02, 03));
//        student.setId(studentId);
//        String newName="Mfs";
//        String newEmail="Mfs@gmail.com";
//
//        given(studentService.updateStudent(student.getId(), newName, newEmail)).willReturn(student);
//        //When
//        String inputInJson = objectMapper.writeValueAsString(student);
//        System.out.println(inputInJson);
//        //Then
//        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/student/" + student.getId().toString())
//                        .content(objectMapper.writeValueAsString(student))
//                        .accept(MediaType.APPLICATION_JSON).content(inputInJson))
//                .andExpect(jsonPath("$.name").value("Furkan"))
//                .andExpect(status().isOk());
//
//
//
//        verify(studentService, times(1)).updateStudent(studentId, newName, newEmail);
//
//
//    }

}