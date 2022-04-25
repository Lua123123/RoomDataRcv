package com.example.roomdatabasercv.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.roomdatabasercv.model.Student;

import java.util.List;

@Dao
public interface StudentDAO {
    //insert vao Database
    @Insert
    void insertStudent(Student student);

    @Query("SELECT * FROM student")
    //get list Student tu Database ra
    List<Student> getListStudent();

    //check Student da ton tai chua
    @Query("SELECT * FROM student where studentName= :studentName")
    List<Student> checkStudent(String studentName);

    //edit student
    @Update
    void editStudent(Student student);

    //delete student
    @Delete
    void deleteStudent(Student student);

    //delete all student
    @Query("DELETE FROM student")
    void deleteAllStudent();

    @Query("SELECT * FROM student where studentName LIKE '%' ||:name|| '%'")
    List<Student> searchStudent(String name);
}
