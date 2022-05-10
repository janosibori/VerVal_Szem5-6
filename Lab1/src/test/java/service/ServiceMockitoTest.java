package service;

import domain.Homework;
import domain.Student;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import repository.GradeXMLRepository;
import repository.HomeworkXMLRepository;
import repository.StudentXMLRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiceMockitoTest {
    @Mock
    private StudentXMLRepository studentXMLRepository;
    @Mock
    private HomeworkXMLRepository homeworkXMLRepository;
    @Mock
    private GradeXMLRepository gradeXMLRepository;

    private Service service;


    @BeforeEach
    public void init() {
        studentXMLRepository = mock(StudentXMLRepository.class);
        homeworkXMLRepository = mock(HomeworkXMLRepository.class);
        gradeXMLRepository = mock(GradeXMLRepository.class);
        service = new Service(studentXMLRepository, homeworkXMLRepository, gradeXMLRepository);
    }

    @Test
    void findAllStudents() {
        List<Student> students = new java.util.ArrayList<>(Collections.emptyList());
        students.add(new Student("1", "Peter", 532));
        students.add(new Student("2", "Mari", 531));
        when(studentXMLRepository.findAll()).thenReturn(students);
        verifyNoInteractions(studentXMLRepository);
        Iterable<Student> studentList = studentXMLRepository.findAll();
        long count = StreamSupport.stream(studentList.spliterator(), false).count();
        assertEquals(2, count);
        verify(studentXMLRepository, times(1));
    }


    @ParameterizedTest
    @ValueSource(strings = {"9999", "7777", "88888", "ttttt"})
    void testDeleteHomeworkWithNonexistentId(String id) {
        List<Homework> homeworkList = new java.util.ArrayList<>(Collections.emptyList());
        homeworkList.add(new Homework("1","desc",4,5));
        when(homeworkXMLRepository.findAll()).thenReturn(homeworkList);
        Iterable<Homework> homeworks = homeworkXMLRepository.findAll();
        long count = StreamSupport.stream(homeworks.spliterator(), false).count();
        service.deleteHomework(id);
        Assertions.assertEquals(count, StreamSupport.stream(service.findAllHomework().spliterator(), false).count());
        verify(homeworkXMLRepository).delete(anyString());
    }

    @Test
    void testUpdateStudentWithExistingId() {
        Student student = new Student("1", "Pet", 532);
        when(studentXMLRepository.findOne("1")).thenReturn(student);
        verifyNoInteractions(studentXMLRepository);
        service.updateStudent("1", "Pet", 532);
        verify(studentXMLRepository).update(student);
        Assertions.assertEquals(student, studentXMLRepository.findOne("1"));
    }
}