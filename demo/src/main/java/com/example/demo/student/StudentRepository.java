package com.example.demo.student;

import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// JpaRepository<Student,Long> long verdik cunku student id long turunde
@Repository
public interface StudentRepository  extends JpaRepository<Student,Long> {

    @Query(value = "select * from student where email= :Email",
    nativeQuery = true)
    Optional<Student> findStudentsByEmail(@Param("Email") String email);

    Optional<Student> findStudentsByName(String name);
}
