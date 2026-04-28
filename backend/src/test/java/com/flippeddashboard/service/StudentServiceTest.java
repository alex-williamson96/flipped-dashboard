package com.flippeddashboard.service;

import com.flippeddashboard.dto.LessonContentDto;
import com.flippeddashboard.dto.LessonSummaryDto;
import com.flippeddashboard.dto.StudentCourseDto;
import com.flippeddashboard.dto.StudentDto;
import com.flippeddashboard.dto.StudentQuizChoiceDto;
import com.flippeddashboard.dto.StudentQuizQuestionDto;
import com.flippeddashboard.model.*;
import com.flippeddashboard.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock private StudentRepository studentRepository;
    @Mock private CourseRepository courseRepository;
    @Mock private LessonRepository lessonRepository;
    @Mock private LessonItemRepository lessonItemRepository;
    @Mock private LessonEventRepository lessonEventRepository;
    @Mock private QuizService quizService;
    @Mock private SurveyService surveyService;

    @InjectMocks
    private StudentService studentService;

    private Student makeStudent(Long id, String name, Section section) {
        Student s = new Student();
        s.setId(id);
        s.setName(name);
        s.setSection(section);
        return s;
    }

    private Section makeSection(Long id, String name, Course course) {
        Section sec = new Section();
        sec.setId(id);
        sec.setName(name);
        sec.setCourse(course);
        return sec;
    }

    private Course makeCourse(Long id, String name) {
        Course c = new Course();
        c.setId(id);
        c.setName(name);
        return c;
    }

    private Lesson makeLesson(Long id, String title, int position) {
        Lesson l = new Lesson();
        l.setId(id);
        l.setTitle(title);
        l.setPosition(position);
        return l;
    }

    @Test
    void getAllStudents_returnsMappedList() {
        Course course = makeCourse(1L, "Bio");
        Section section = makeSection(1L, "Period 1", course);
        when(studentRepository.findAll()).thenReturn(List.of(makeStudent(1L, "Alice", section)));

        List<StudentDto> result = studentService.getAllStudents();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getName()).isEqualTo("Alice");
    }

    @Test
    void getCoursesForStudent_mismatch_throwsIllegalArgument() {
        assertThatThrownBy(() -> studentService.getCoursesForStudent(2L, 1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student ID mismatch");
    }

    @Test
    void getCoursesForStudent_studentNotFound_throwsIllegalArgument() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getCoursesForStudent(1L, 1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student not found");
    }

    @Test
    void getCoursesForStudent_happyPath_returnsCourse() {
        Course course = makeCourse(10L, "Introduction to Biology");
        Section section = makeSection(3L, "Period 2", course);
        Student student = makeStudent(1L, "Alice", section);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        List<StudentCourseDto> result = studentService.getCoursesForStudent(1L, 1L, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCourseId()).isEqualTo(10L);
        assertThat(result.get(0).getTitle()).isEqualTo("Introduction to Biology");
        assertThat(result.get(0).getSectionId()).isEqualTo(3L);
        assertThat(result.get(0).getSectionName()).isEqualTo("Period 2");
    }

    @Test
    void getLessonsForStudentCourse_mismatch_throwsIllegalArgument() {
        assertThatThrownBy(() -> studentService.getLessonsForStudentCourse(2L, 1L, 10L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student ID mismatch");
    }

    @Test
    void getLessonsForStudentCourse_studentNotFound_throwsIllegalArgument() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getLessonsForStudentCourse(1L, 1L, 10L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student not found");
    }

    @Test
    void getLessonsForStudentCourse_courseNotFound_throwsEntityNotFound() {
        Course course = makeCourse(10L, "Bio");
        Section section = makeSection(3L, "Period 2", course);
        Student student = makeStudent(1L, "Alice", section);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getLessonsForStudentCourse(1L, 1L, 99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void getLessonsForStudentCourse_notEnrolled_throwsEntityNotFound() {
        Course course = makeCourse(10L, "Bio");
        Section section = makeSection(3L, "Period 2", course);
        Student student = makeStudent(1L, "Alice", section);
        Course other = makeCourse(99L, "Other");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(99L)).thenReturn(Optional.of(other));

        assertThatThrownBy(() -> studentService.getLessonsForStudentCourse(1L, 1L, 99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void getLessonsForStudentCourse_happyPath_returnsLessonsWithCompletedFlag() {
        Course course = makeCourse(10L, "Bio");
        Section section = makeSection(3L, "Period 2", course);
        Student student = makeStudent(1L, "Alice", section);
        Lesson lesson = makeLesson(12L, "Cell Structure", 1);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(10L)).thenReturn(Optional.of(course));
        when(lessonRepository.findByCourseIdOrderByPositionAsc(10L)).thenReturn(List.of(lesson));
        when(lessonEventRepository.existsByStudentIdAndLessonIdAndEventType(1L, 12L, LessonEventType.COMPLETED))
                .thenReturn(true);

        List<LessonSummaryDto> result = studentService.getLessonsForStudentCourse(1L, 1L, 10L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLessonId()).isEqualTo(12L);
        assertThat(result.get(0).isCompleted()).isTrue();
    }

    @Test
    void getLessonContent_studentNotFound_throwsIllegalArgument() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getLessonContent(1L, 12L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student not found");
    }

    @Test
    void getLessonContent_lessonNotFound_throwsEntityNotFound() {
        Student student = makeStudent(1L, "Alice", makeSection(1L, "P1", makeCourse(1L, "Bio")));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(lessonRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getLessonContent(1L, 99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void getLessonContent_quizItem_doesNotExposeIsCorrect() {
        Student student = makeStudent(1L, "Alice", makeSection(1L, "P1", makeCourse(1L, "Bio")));
        Lesson lesson = makeLesson(12L, "Cell Structure", 1);

        LessonItem quizItem = new LessonItem();
        quizItem.setId(103L);
        quizItem.setPosition(1);
        quizItem.setType(LessonItemType.QUIZ);

        StudentQuizChoiceDto choiceDtoFixture = new StudentQuizChoiceDto(201L, "Energy production");
        StudentQuizQuestionDto questionDtoFixture =
                new StudentQuizQuestionDto(44L, "What is ATP?", List.of(choiceDtoFixture));

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(lessonRepository.findById(12L)).thenReturn(Optional.of(lesson));
        when(lessonItemRepository.findByLessonIdOrderByPositionAsc(12L)).thenReturn(List.of(quizItem));
        when(quizService.getStudentQuestions(103L)).thenReturn(List.of(questionDtoFixture));

        LessonContentDto content = studentService.getLessonContent(1L, 12L);

        assertThat(content.getItems()).hasSize(1);
        Object questionDto = ((List<?>) content.getItems().get(0).getQuestions()).get(0);
        assertThat(questionDto).isInstanceOf(StudentQuizQuestionDto.class);

        StudentQuizQuestionDto qqDto = (StudentQuizQuestionDto) questionDto;
        StudentQuizChoiceDto choiceDto = qqDto.getChoices().get(0);
        assertThat(choiceDto.getQuizChoiceId()).isEqualTo(201L);
        assertThat(choiceDto.getText()).isEqualTo("Energy production");

        assertThat(choiceDto.getClass().getDeclaredFields())
                .noneMatch(f -> f.getName().equals("isCorrect") || f.getName().equals("correct"));
    }

    @Test
    void getLessonContent_videoItem_extractsUrl() throws Exception {
        Student student = makeStudent(1L, "Alice", makeSection(1L, "P1", makeCourse(1L, "Bio")));
        Lesson lesson = makeLesson(12L, "Cell Structure", 1);

        LessonItem videoItem = new LessonItem();
        videoItem.setId(101L);
        videoItem.setPosition(1);
        videoItem.setType(LessonItemType.VIDEO);
        videoItem.setContent("{\"url\": \"https://youtube.com/watch?v=abc\"}");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(lessonRepository.findById(12L)).thenReturn(Optional.of(lesson));
        when(lessonItemRepository.findByLessonIdOrderByPositionAsc(12L)).thenReturn(List.of(videoItem));

        LessonContentDto content = studentService.getLessonContent(1L, 12L);

        assertThat(content.getItems().get(0).getVideoUrl()).isEqualTo("https://youtube.com/watch?v=abc");
    }
}
