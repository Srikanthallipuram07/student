/*
 * You can use the following import statements
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.ArrayList;
 *
 */

// Write your code here
package com.example.school.service;

import com.example.school.model.StudentRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;

import javax.validation.OverridesAttribute;

import com.example.school.model.Student;
import com.example.school.repository.StudentRepository;

@Service
public class StudentH2Service implements StudentRepository{
    @Autowired 
    private JdbcTemplate db;

    @Override 
    public void deleteStudent(int studentId){
        db.update("delete from student where id = ?", studentId);
    }
    @Override
    
    public Student updateStudent(int studentId,Student student){
        if (student.getStudentName() != null){
            db.update("update student set studentName = ? where id = ?",student.getStudentName(),studentId);
        }
        if (student.getGender() != null){
            db.update("update student set gender = ? where id = ?",student.getGender(),studentId);
        }
        if (student.getStandard() != null){
            db.update("update student set standard = ? where id = ?",student.getStandard(),studentId);
        }
        return getStudentById(studentId);
    }  
    @Override 
    public Student  addStudent(Student student){
        db.update("insert into student(studentName,gender,standard) values (?,?,?)",student.getStudentName(),student.getGender(),student.getStandard());
            Student savedStudent = db.queryForObject("select * from student where studentName = ? , gender = ? and standard = ?",new StudentRowMapper(), student.getStudentName(), student.getGender(),student.getStandard());
            return savedStudent;
    }
    @Override 
    public  Student getStudentById(int studentId){
        try {
            Student student = db.queryForObject("select * from student where id = ?", new StudentRowMapper(), studentId);
            return student;
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override 
    public ArrayList <Student> getStudents(){
        List<Student> studentList = db.query("select * from student", new StudentRowMapper());
        ArrayList <Student> students = new ArrayList<>(studentList);
        return students;
    }
}