package com.flippeddashboard.controller;

import com.flippeddashboard.dto.*;
import com.flippeddashboard.service.LessonItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class LessonItemController {

    private final LessonItemService lessonItemService;

    public LessonItemController(LessonItemService lessonItemService) {
        this.lessonItemService = lessonItemService;
    }

    @PostMapping("/lessons/{lessonId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<LessonItemResponse> addItem(@PathVariable Long lessonId,
                                                    @Valid @RequestBody LessonItemRequest req) {
        return ApiResponse.ok(lessonItemService.addItem(lessonId, req));
    }

    @PutMapping("/lesson-items/{id}")
    public ApiResponse<LessonItemResponse> updateItem(@PathVariable Long id,
                                                       @Valid @RequestBody LessonItemRequest req) {
        return ApiResponse.ok(lessonItemService.updateItem(id, req));
    }

    @DeleteMapping("/lesson-items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable Long id) {
        lessonItemService.deleteItem(id);
    }

    @PutMapping("/lessons/{lessonId}/items/reorder")
    public ApiResponse<List<ItemPositionResponse>> reorderItems(@PathVariable Long lessonId,
                                                                  @Valid @RequestBody ReorderRequest req) {
        return ApiResponse.ok(lessonItemService.reorderItems(lessonId, req));
    }
}
