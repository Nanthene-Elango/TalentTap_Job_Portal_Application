package com.talenttap.service;

public class ExperienceRange {
    private int min;
    private int max; // use Integer.MAX_VALUE for 10+

    public ExperienceRange(String range) {
        if (range.contains("+")) {
            this.min = Integer.parseInt(range.replace("+", "").trim());
            this.max = Integer.MAX_VALUE;
        } else if (range.contains("-")) {
            String[] parts = range.split("-");
            this.min = Integer.parseInt(parts[0].trim());
            this.max = Integer.parseInt(parts[1].trim());
        } else {
            int value = Integer.parseInt(range.trim());
            this.min = value;
            this.max = value;
        }
    }

    public boolean overlapsWith(ExperienceRange other) {
        return this.min <= other.max && other.min <= this.max;
    }
}
