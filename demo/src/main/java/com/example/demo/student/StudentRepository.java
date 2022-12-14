package com.example.demo.student;

import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// JpaRepository<Student,Long> long verdik cunku student id long turunde
@Repository
public interface StudentRepository  extends JpaRepository<Student,Long> {

   // @Query("SELECT s FROM Student s where s.email = ?1")
    Optional<Student> findStudentsByEmail(String email);
}
