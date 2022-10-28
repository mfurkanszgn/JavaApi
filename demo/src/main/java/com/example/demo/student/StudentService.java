package com.example.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Boolean existsEmail = studentRepository
                .selectExistsEmail(student.getEmail());
        if (existsEmail) {
            throw new IllegalArgumentException("email taken");
        }
        studentRepository.save(student);
    }


    public void deleteStudent(Long studentId) {
        {
            boolean exist = studentRepository.existsById(studentId);
            if (!exist) {
                throw new IllegalArgumentException("student id  not exist");
            }
            studentRepository.deleteById(studentId);
        }

    }

    @Transactional
    public Student updateStudent(Long studentId, String name, String email) {
        Student student = studentRepository.findById(studentId).
                orElseThrow(() -> new IllegalArgumentException(
                        "student not exist"));
        if (name != null && name.length() > 0 && !Objects.equals(student.getName(), name)) {
            student.setName(name);
        }
        if (email != null && email.length() > 0 && !Objects.equals(student.getEmail(), name)) {
            boolean emailExist = studentRepository.selectExistsEmail(email);
            if (emailExist) {

                throw new IllegalArgumentException("email taken");
            }
            student.setEmail(email);
        }
        return student;


    }
}
