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
        Optional<Student> studentOptional =
                studentRepository.findStudentsByEmail(student.getEmail());
        if (studentOptional.isPresent()) {

            throw new IllegalArgumentException("email taken");
        }
        System.out.println(student);
        studentRepository.save(student);
    }


    public void deleteStudent(Long studentId) {
        {
            boolean exist = studentRepository.existsById(studentId);
            if (!exist) {
                throw new IllegalArgumentException("student id " + studentId + " not exist");


            }
            studentRepository.deleteById(studentId);
        }

    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
        Student student = studentRepository.findById(studentId).
                orElseThrow(() -> new IllegalArgumentException(
                        "student with in" + studentId + "not exist"));
        if (name != null && name.length() > 0 && !Objects.equals(student.getName(), name)) {
            student.setName(name);
        }
        if (email != null && email.length() > 0 && !Objects.equals(student.getEmail(), name)) {
            Optional<Student> studentOptional =
                    studentRepository.findStudentsByEmail(student.getEmail());
            if (studentOptional.isPresent()) {

                throw new IllegalArgumentException("email taken");
            }
            student.setEmail(email);
        }


    }
}
