package data;

import entity.Student;
import java.util.Collection;

public interface StudentRepository {
    void addStudent(Student student);
    Student getStudent(String studentID);
    Collection<Student> getAllStudents();
    void removeStudent(String studentID);
    void saveStudents(String filePath);
    void loadStudents(String filePath);
}
