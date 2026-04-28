package com.flippeddashboard.service;

import com.flippeddashboard.dto.QuizChoiceRequest;
import com.flippeddashboard.dto.QuizChoiceResponse;
import com.flippeddashboard.dto.QuizQuestionRequest;
import com.flippeddashboard.dto.QuizQuestionResponse;
import com.flippeddashboard.dto.QuizResponseRequest;
import com.flippeddashboard.model.*;
import com.flippeddashboard.repository.LessonItemRepository;
import com.flippeddashboard.repository.QuizChoiceRepository;
import com.flippeddashboard.repository.QuizQuestionRepository;
import com.flippeddashboard.repository.QuizResponseRepository;
import com.flippeddashboard.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizServiceTest {

    @Mock private StudentRepository studentRepository;
    @Mock private LessonItemRepository lessonItemRepository;
    @Mock private QuizQuestionRepository quizQuestionRepository;
    @Mock private QuizChoiceRepository quizChoiceRepository;
    @Mock private QuizResponseRepository quizResponseRepository;

    @InjectMocks private QuizService quizService;

    @Test
    void addQuestion_savesAndReturns() {
        LessonItem item = quizItem(1L);
        when(lessonItemRepository.findById(1L)).thenReturn(Optional.of(item));
        QuizQuestion saved = question(10L, "What is a cell?", 1);
        when(quizQuestionRepository.save(any())).thenReturn(saved);

        QuizQuestionRequest req = new QuizQuestionRequest();
        req.setText("What is a cell?");
        req.setPosition(1);

        QuizQuestionResponse result = quizService.addQuestion(1L, req);

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getText()).isEqualTo("What is a cell?");
    }

    @Test
    void addQuestion_setsLearningObjective() {
        LessonItem item = quizItem(1L);
        when(lessonItemRepository.findById(1L)).thenReturn(Optional.of(item));
        QuizQuestion saved = question(10L, "What is mitosis?", 1);
        saved.setLearningObjective("Understand cell division");
        when(quizQuestionRepository.save(any())).thenReturn(saved);

        QuizQuestionRequest req = new QuizQuestionRequest();
        req.setText("What is mitosis?");
        req.setLearningObjective("Understand cell division");

        QuizQuestionResponse result = quizService.addQuestion(1L, req);

        assertThat(result.getLearningObjective()).isEqualTo("Understand cell division");
    }

    @Test
    void addQuestion_throwsWhenItemNotFound() {
        when(lessonItemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> quizService.addQuestion(99L, new QuizQuestionRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void addQuestion_throwsWhenNotQuizType() {
        LessonItem item = new LessonItem();
        item.setId(1L);
        item.setType(LessonItemType.TEXT);
        when(lessonItemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> quizService.addQuestion(1L, new QuizQuestionRequest()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateQuestion_updatesNonNullFields() {
        QuizQuestion question = question(10L, "Old?", 1);
        when(quizQuestionRepository.findById(10L)).thenReturn(Optional.of(question));
        when(quizQuestionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        QuizQuestionRequest req = new QuizQuestionRequest();
        req.setText("New?");

        QuizQuestionResponse result = quizService.updateQuestion(10L, req);

        assertThat(result.getText()).isEqualTo("New?");
    }

    @Test
    void updateQuestion_updatesLearningObjective() {
        QuizQuestion question = question(10L, "Old?", 1);
        question.setLearningObjective("Old objective");
        when(quizQuestionRepository.findById(10L)).thenReturn(Optional.of(question));
        when(quizQuestionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        QuizQuestionRequest req = new QuizQuestionRequest();
        req.setLearningObjective("New objective");

        QuizQuestionResponse result = quizService.updateQuestion(10L, req);

        assertThat(result.getLearningObjective()).isEqualTo("New objective");
    }

    @Test
    void updateQuestion_throwsWhenNotFound() {
        when(quizQuestionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> quizService.updateQuestion(99L, new QuizQuestionRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deleteQuestion_deletesById() {
        when(quizQuestionRepository.existsById(10L)).thenReturn(true);

        quizService.deleteQuestion(10L);

        verify(quizQuestionRepository).deleteById(10L);
    }

    @Test
    void deleteQuestion_throwsWhenNotFound() {
        when(quizQuestionRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> quizService.deleteQuestion(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void addChoice_savesAndReturns() {
        QuizQuestion question = question(10L, "What is a cell?", 1);
        when(quizQuestionRepository.findById(10L)).thenReturn(Optional.of(question));
        QuizChoice saved = choice(100L, "Mitochondria", true);
        when(quizChoiceRepository.save(any())).thenReturn(saved);

        QuizChoiceRequest req = new QuizChoiceRequest();
        req.setText("Mitochondria");
        req.setIsCorrect(true);

        QuizChoiceResponse result = quizService.addChoice(10L, req);

        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getText()).isEqualTo("Mitochondria");
        assertThat(result.getIsCorrect()).isTrue();
    }

    @Test
    void addChoice_throwsWhenQuestionNotFound() {
        when(quizQuestionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> quizService.addChoice(99L, new QuizChoiceRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void updateChoice_updatesNonNullFields() {
        QuizChoice choice = choice(100L, "Old", false);
        when(quizChoiceRepository.findById(100L)).thenReturn(Optional.of(choice));
        when(quizChoiceRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        QuizChoiceRequest req = new QuizChoiceRequest();
        req.setText("New");
        req.setIsCorrect(true);

        QuizChoiceResponse result = quizService.updateChoice(100L, req);

        assertThat(result.getText()).isEqualTo("New");
        assertThat(result.getIsCorrect()).isTrue();
    }

    @Test
    void updateChoice_throwsWhenNotFound() {
        when(quizChoiceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> quizService.updateChoice(99L, new QuizChoiceRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deleteChoice_deletesById() {
        when(quizChoiceRepository.existsById(100L)).thenReturn(true);

        quizService.deleteChoice(100L);

        verify(quizChoiceRepository).deleteById(100L);
    }

    @Test
    void deleteChoice_throwsWhenNotFound() {
        when(quizChoiceRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> quizService.deleteChoice(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void recordResponse_studentIdMismatch_throwsIllegalArgument() {
        QuizResponseRequest req = new QuizResponseRequest();
        req.setStudentId(1L);
        req.setQuizQuestionId(44L);
        req.setQuizChoiceId(201L);

        assertThatThrownBy(() -> quizService.recordResponse(2L, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student ID mismatch");
    }

    @Test
    void recordResponse_studentNotFound_throwsIllegalArgument() {
        QuizResponseRequest req = new QuizResponseRequest();
        req.setStudentId(1L);
        req.setQuizQuestionId(44L);
        req.setQuizChoiceId(201L);
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> quizService.recordResponse(1L, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student not found");
    }

    @Test
    void recordResponse_questionNotFound_throwsIllegalArgument() {
        QuizResponseRequest req = new QuizResponseRequest();
        req.setStudentId(1L);
        req.setQuizQuestionId(44L);
        req.setQuizChoiceId(201L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student(1L)));
        when(quizQuestionRepository.findById(44L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> quizService.recordResponse(1L, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Quiz question not found");
    }

    @Test
    void recordResponse_choiceNotBelongToQuestion_throwsIllegalArgument() {
        QuizResponseRequest req = new QuizResponseRequest();
        req.setStudentId(1L);
        req.setQuizQuestionId(44L);
        req.setQuizChoiceId(201L);
        req.setSubmittedAt(OffsetDateTime.now());

        QuizQuestion question = questionWithLessonItem(44L);
        QuizQuestion otherQuestion = questionWithLessonItem(99L);
        QuizChoice choice = choice(201L, "x", false);
        choice.setQuestion(otherQuestion);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student(1L)));
        when(quizQuestionRepository.findById(44L)).thenReturn(Optional.of(question));
        when(quizChoiceRepository.findById(201L)).thenReturn(Optional.of(choice));

        assertThatThrownBy(() -> quizService.recordResponse(1L, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Quiz choice not found or does not belong to that question");
    }

    @Test
    void recordResponse_happyPath_savesResponse() {
        QuizResponseRequest req = new QuizResponseRequest();
        req.setStudentId(1L);
        req.setQuizQuestionId(44L);
        req.setQuizChoiceId(201L);
        req.setSubmittedAt(OffsetDateTime.now());

        QuizQuestion question = questionWithLessonItem(44L);
        QuizChoice choice = choice(201L, "Mitochondria", true);
        choice.setQuestion(question);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student(1L)));
        when(quizQuestionRepository.findById(44L)).thenReturn(Optional.of(question));
        when(quizChoiceRepository.findById(201L)).thenReturn(Optional.of(choice));

        quizService.recordResponse(1L, req);

        verify(quizResponseRepository).save(any(QuizResponse.class));
    }

    private LessonItem quizItem(Long id) {
        LessonItem item = new LessonItem();
        item.setId(id);
        item.setType(LessonItemType.QUIZ);
        return item;
    }

    private QuizQuestion question(Long id, String text, int position) {
        QuizQuestion q = new QuizQuestion();
        q.setId(id);
        q.setQuestionText(text);
        q.setPosition(position);
        return q;
    }

    private QuizChoice choice(Long id, String text, boolean isCorrect) {
        QuizChoice c = new QuizChoice();
        c.setId(id);
        c.setChoiceText(text);
        c.setCorrect(isCorrect);
        return c;
    }

    private Student student(Long id) {
        Student s = new Student();
        s.setId(id);
        return s;
    }

    private QuizQuestion questionWithLessonItem(Long id) {
        Course course = new Course();
        course.setArchived(false);
        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        LessonItem lessonItem = new LessonItem();
        lessonItem.setId(90L);
        lessonItem.setType(LessonItemType.QUIZ);
        lessonItem.setLesson(lesson);
        QuizQuestion q = new QuizQuestion();
        q.setId(id);
        q.setLessonItem(lessonItem);
        return q;
    }
}
