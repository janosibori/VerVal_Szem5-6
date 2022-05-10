package service;

import domain.Grade;
import domain.Homework;
import domain.Student;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import repository.GradeXMLRepository;
import repository.HomeworkXMLRepository;
import repository.StudentXMLRepository;
import validation.GradeValidator;
import validation.HomeworkValidator;
import validation.StudentValidator;
import validation.Validator;

import java.util.stream.StreamSupport;


import static org.junit.jupiter.api.Assertions.assertThrows;


public class ServiceTest {
    private static Service service;
    private static StudentXMLRepository studentXMLRepository;
    private static HomeworkXMLRepository homeworkXMLRepository;
    private static GradeXMLRepository gradeXMLRepository;

    @BeforeAll

    public static void setUp() throws Exception {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Homework> homeworkValidator = new HomeworkValidator();
        Validator<Grade> gradeValidator = new GradeValidator();

        studentXMLRepository = new StudentXMLRepository(studentValidator, "students.xml");
        homeworkXMLRepository = new HomeworkXMLRepository(homeworkValidator, "homework.xml");
        gradeXMLRepository = new GradeXMLRepository(gradeValidator, "grades.xml");

        service = new Service(studentXMLRepository, homeworkXMLRepository, gradeXMLRepository);
    }

    @Test
    public void testFindAllStudents() {
        Assertions.assertNotNull(service.findAllStudents());
        Iterable<Student> students = service.findAllStudents();
        long count = StreamSupport.stream(students.spliterator(), false).count();
        service.saveStudent("199", "Peter", 532);
        Assertions.assertEquals(count + 1, StreamSupport.stream(service.findAllStudents().spliterator(), false).count());
    }

    public void testFindAllHomework() {
    }

    public void testFindAllGrades() {
    }

    public void testSaveStudent() {
    }

    public void testSaveHomework() {
    }

    public void testSaveGrade() {
    }

    @Test
    public void testDeleteStudentWithExistingId() {
        service.saveStudent("1999", "Peter", 532);
        Iterable<Student> students = service.findAllStudents();
        long count = StreamSupport.stream(students.spliterator(), false).count();
        service.deleteStudent("1999");
        Assertions.assertEquals(count - 1, StreamSupport.stream(service.findAllStudents().spliterator(), false).count());
    }

    @ParameterizedTest
    @ValueSource(strings = {"9999", "7777", "88888", "ttttt"})
    public void testDeleteHomeworkWithNonexistentId(String id) {
        Assertions.assertNull(homeworkXMLRepository.findOne(id));
        Iterable<Homework> homeworks = service.findAllHomework();
        long count = StreamSupport.stream(homeworks.spliterator(), false).count();
        service.deleteHomework("9999");
        Assertions.assertEquals(count, StreamSupport.stream(service.findAllHomework().spliterator(), false).count());
    }

    @Test
    public void testUpdateStudentWithExistingId() {
        Assertions.assertNotNull(studentXMLRepository.findOne("1"));
        service.updateStudent("1", "Pet", 532);
        Assertions.assertEquals("Pet", studentXMLRepository.findOne("1").getName());
    }

    public void testUpdateHomework() {
    }

    @Test
    public void testExtendDeadlineWithNonExistingId() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.extendDeadline("199999", 4);
        });
    }

    public void testCreateStudentFile() {
    }
}