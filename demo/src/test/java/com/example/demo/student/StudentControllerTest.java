package com.example.demo.student;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = StudentController.class)
class StudentControllerTest {
    @MockBean
    private StudentService studentService;
    @Autowired
    private ObjectMapper objectMapper;

    private StudentRepository studentRepository = mock(StudentRepository.class);
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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Furkan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("Furka2n@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].dob").value("2000-02-03"));


        //Then

    }

    @Test
    void itShouldRegisterNewStudent() throws Exception {
        //Given
        //given(studentService.addNewStudent(any(Student.class))).willAnswer((invocation)-> invocation.getArgument(0));

        Student student = new Student("Furkan", "Furkan.gmail", LocalDate.of(2000, 02, 03));

        ResultActions resultActions = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/v1/student/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(student))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated());
        verify(studentService).addNewStudent(any(Student.class));
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
        given(studentRepository.findById(studentId)).willReturn(Optional.of(student));

        //When
        doNothing().when(studentRepository).deleteById(student.getId());
        //Then
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/student/{studentId}",student.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(student.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(student.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dob").value(student.getDob()));

    }

    @Test
    void itShouldUpdateStudent() {
        //Given
        //When
        //Then

    }
}