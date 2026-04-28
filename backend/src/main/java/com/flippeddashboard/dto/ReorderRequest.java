package com.flippeddashboard.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class ReorderRequest {
    @NotEmpty
    private List<Long> itemIds;

    public List<Long> getItemIds() { return itemIds; }
    public void setItemIds(List<Long> itemIds) { this.itemIds = itemIds; }
}
