package com.flippeddashboard.service;

import com.flippeddashboard.dto.*;
import com.flippeddashboard.model.*;
import com.flippeddashboard.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LessonItemService {

    private final LessonItemRepository lessonItemRepository;
    private final LessonRepository lessonRepository;

    public LessonItemService(LessonItemRepository lessonItemRepository,
                              LessonRepository lessonRepository) {
        this.lessonItemRepository = lessonItemRepository;
        this.lessonRepository = lessonRepository;
    }

    @Transactional
    public LessonItemResponse addItem(Long lessonId, LessonItemRequest req) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found: " + lessonId));
        if (req.getType() == null) {
            throw new IllegalArgumentException("type is required");
        }

        LessonItemType type = LessonItemType.valueOf(req.getType());

        LessonItem item = new LessonItem();
        item.setLesson(lesson);
        item.setType(type);
        if (req.getPosition() != null) item.setPosition(req.getPosition());
        item.setContent(buildContent(type, req));

        LessonItem saved = lessonItemRepository.save(item);
        return toResponse(saved);
    }

    @Transactional
    public LessonItemResponse updateItem(Long id, LessonItemRequest req) {
        LessonItem item = lessonItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LessonItem not found: " + id));
        if (req.getPosition() != null) item.setPosition(req.getPosition());
        if (req.getType() != null || req.getTitle() != null || req.getVideoUrl() != null || req.getBody() != null) {
            if (req.getType() != null) {
                item.setType(LessonItemType.valueOf(req.getType()));
            }
            Map<String, Object> existing = ContentSerializer.parse(item.getContent());
            if (req.getTitle() != null) existing.put("title", req.getTitle());
            if (req.getVideoUrl() != null) existing.put("videoUrl", req.getVideoUrl());
            if (req.getBody() != null) existing.put("body", req.getBody());
            item.setContent(ContentSerializer.write(existing));
        }
        return toResponse(lessonItemRepository.save(item));
    }

    @Transactional
    public void deleteItem(Long id) {
        if (!lessonItemRepository.existsById(id)) {
            throw new EntityNotFoundException("LessonItem not found: " + id);
        }
        lessonItemRepository.deleteById(id);
    }

    @Transactional
    public List<ItemPositionResponse> reorderItems(Long lessonId, ReorderRequest req) {
        if (!lessonRepository.existsById(lessonId)) {
            throw new EntityNotFoundException("Lesson not found: " + lessonId);
        }
        List<Long> itemIds = req.getItemIds();
        List<LessonItem> items = lessonItemRepository.findByLessonIdOrderByPositionAsc(lessonId);
        Set<Long> belongToLesson = items.stream().map(LessonItem::getId).collect(Collectors.toSet());

        for (Long itemId : itemIds) {
            if (!belongToLesson.contains(itemId)) {
                throw new IllegalArgumentException("Item " + itemId + " does not belong to lesson " + lessonId);
            }
        }

        Map<Long, LessonItem> itemMap = items.stream()
                .collect(Collectors.toMap(LessonItem::getId, i -> i));

        List<ItemPositionResponse> result = new ArrayList<>();
        for (int i = 0; i < itemIds.size(); i++) {
            LessonItem item = itemMap.get(itemIds.get(i));
            item.setPosition(i + 1);
            lessonItemRepository.save(item);
            ItemPositionResponse pr = new ItemPositionResponse();
            pr.setId(item.getId());
            pr.setPosition(item.getPosition());
            result.add(pr);
        }
        return result;
    }

    private String buildContent(LessonItemType type, LessonItemRequest req) {
        Map<String, Object> map = new LinkedHashMap<>();
        switch (type) {
            case VIDEO -> {
                if (req.getVideoUrl() != null) map.put("videoUrl", req.getVideoUrl());
                if (req.getTitle() != null) map.put("title", req.getTitle());
            }
            case TEXT -> {
                if (req.getBody() != null) map.put("body", req.getBody());
                if (req.getTitle() != null) map.put("title", req.getTitle());
            }
            case QUIZ, SURVEY -> {
                if (req.getTitle() != null) map.put("title", req.getTitle());
            }
        }
        return ContentSerializer.write(map);
    }

    private LessonItemResponse toResponse(LessonItem item) {
        LessonItemResponse resp = new LessonItemResponse();
        resp.setId(item.getId());
        resp.setType(item.getType().name());
        resp.setPosition(item.getPosition());
        Map<String, Object> content = ContentSerializer.parse(item.getContent());
        resp.setTitle((String) content.get("title"));
        if (item.getType() == LessonItemType.VIDEO) {
            String url = content.containsKey("videoUrl")
                    ? (String) content.get("videoUrl")
                    : (String) content.get("url");
            resp.setVideoUrl(url);
        }
        if (item.getType() == LessonItemType.TEXT) {
            resp.setBody((String) content.get("body"));
        }
        return resp;
    }

}
