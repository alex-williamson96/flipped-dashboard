package com.flippeddashboard.dto;

public class LoAccuracyDto {
    private String objective;
    private int correctCount;
    private int totalAttempts;
    private double accuracy;

    public LoAccuracyDto(String objective, int correctCount, int totalAttempts) {
        this.objective = objective;
        this.correctCount = correctCount;
        this.totalAttempts = totalAttempts;
        this.accuracy = totalAttempts == 0 ? 0.0 : (double) correctCount / totalAttempts;
    }

    public String getObjective() { return objective; }
    public int getCorrectCount() { return correctCount; }
    public int getTotalAttempts() { return totalAttempts; }
    public double getAccuracy() { return accuracy; }
}
