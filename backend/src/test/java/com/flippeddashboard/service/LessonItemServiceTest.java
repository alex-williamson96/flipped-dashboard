package com.flippeddashboard.service;

import com.flippeddashboard.dto.ItemPositionResponse;
import com.flippeddashboard.dto.LessonItemRequest;
import com.flippeddashboard.dto.LessonItemResponse;
import com.flippeddashboard.dto.ReorderRequest;
import com.flippeddashboard.model.Lesson;
import com.flippeddashboard.model.LessonItem;
import com.flippeddashboard.model.LessonItemType;
import com.flippeddashboard.repository.LessonItemRepository;
import com.flippeddashboard.repository.LessonRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonItemServiceTest {

    @Mock private LessonItemRepository lessonItemRepository;
    @Mock private LessonRepository lessonRepository;

    @InjectMocks private LessonItemService lessonItemService;

    @Test
    void addItem_savesVideoItem() {
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));

        LessonItem saved = item(10L, LessonItemType.VIDEO, 1, "{\"videoUrl\":\"http://yt.com\"}");
        when(lessonItemRepository.save(any())).thenReturn(saved);

        LessonItemRequest req = new LessonItemRequest();
        req.setType("VIDEO");
        req.setVideoUrl("http://yt.com");
        req.setPosition(1);

        LessonItemResponse result = lessonItemService.addItem(1L, req);

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getType()).isEqualTo("VIDEO");
    }

    @Test
    void addItem_throwsForInvalidType() {
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));

        LessonItemRequest req = new LessonItemRequest();
        req.setType("INVALID");

        assertThatThrownBy(() -> lessonItemService.addItem(1L, req))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void addItem_throwsWhenLessonNotFound() {
        when(lessonRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonItemService.addItem(99L, new LessonItemRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void updateItem_throwsWhenNotFound() {
        when(lessonItemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonItemService.updateItem(99L, new LessonItemRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deleteItem_deletesById() {
        when(lessonItemRepository.existsById(10L)).thenReturn(true);

        lessonItemService.deleteItem(10L);

        verify(lessonItemRepository).deleteById(10L);
    }

    @Test
    void deleteItem_throwsWhenNotFound() {
        when(lessonItemRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> lessonItemService.deleteItem(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void reorderItems_reassignsPositions() {
        when(lessonRepository.existsById(1L)).thenReturn(true);
        LessonItem i1 = item(200L, LessonItemType.VIDEO, 1, "{}");
        LessonItem i2 = item(201L, LessonItemType.TEXT, 2, "{}");
        LessonItem i3 = item(202L, LessonItemType.QUIZ, 3, "{}");
        when(lessonItemRepository.findByLessonIdOrderByPositionAsc(1L)).thenReturn(List.of(i1, i2, i3));
        when(lessonItemRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ReorderRequest req = new ReorderRequest();
        req.setItemIds(List.of(202L, 200L, 201L));

        List<ItemPositionResponse> result = lessonItemService.reorderItems(1L, req);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo(202L);
        assertThat(result.get(0).getPosition()).isEqualTo(1);
        assertThat(result.get(1).getId()).isEqualTo(200L);
        assertThat(result.get(1).getPosition()).isEqualTo(2);
    }

    @Test
    void reorderItems_throwsWhenItemNotBelongToLesson() {
        when(lessonRepository.existsById(1L)).thenReturn(true);
        LessonItem i1 = item(200L, LessonItemType.VIDEO, 1, "{}");
        when(lessonItemRepository.findByLessonIdOrderByPositionAsc(1L)).thenReturn(List.of(i1));

        ReorderRequest req = new ReorderRequest();
        req.setItemIds(List.of(200L, 999L));

        assertThatThrownBy(() -> lessonItemService.reorderItems(1L, req))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void reorderItems_throwsWhenLessonNotFound() {
        when(lessonRepository.existsById(99L)).thenReturn(false);

        ReorderRequest req = new ReorderRequest();
        req.setItemIds(List.of(1L));

        assertThatThrownBy(() -> lessonItemService.reorderItems(99L, req))
                .isInstanceOf(EntityNotFoundException.class);
    }

    private LessonItem item(Long id, LessonItemType type, int position, String content) {
        LessonItem i = new LessonItem();
        i.setId(id);
        i.setType(type);
        i.setPosition(position);
        i.setContent(content);
        return i;
    }
}
