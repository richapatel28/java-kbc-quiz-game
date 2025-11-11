package com.kbc.model;

public class Question {
    private final String text;
    private final String optionA;
    private final String optionB;
    private final String optionC;
    private final String optionD;
    private final String answer; // A/B/C/D
    private final int level; // 1..n
    private final int prize;

    public Question(String text, String optionA, String optionB, String optionC, String optionD, String answer, int level, int prize) {
        this.text = text;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.answer = answer;
        this.level = level;
        this.prize = prize;
    }

    public String getText() { return text; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public String getAnswer() { return answer; }
    public int getLevel() { return level; }
    public int getPrize() { return prize; }
}


