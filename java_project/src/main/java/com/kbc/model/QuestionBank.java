package com.kbc.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionBank {
    private final List<Question> questions;

    public QuestionBank() {
        this.questions = new ArrayList<>();
        loadFromCsv("questions.csv");
    }

    private void loadFromCsv(String resourceName) {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName);
            if (is == null) return;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty() || line.startsWith("#")) continue;
                    // CSV: level,prize,question,A,B,C,D,answer
                    String[] parts = line.split(",", -1);
                    if (parts.length < 8) continue;
                    int level = Integer.parseInt(parts[0].trim());
                    int prize = Integer.parseInt(parts[1].trim());
                    String q = parts[2].trim();
                    String a = parts[3].trim();
                    String b = parts[4].trim();
                    String c = parts[5].trim();
                    String d = parts[6].trim();
                    String ans = parts[7].trim();
                    questions.add(new Question(q, a, b, c, d, ans, level, prize));
                }
            }
            Collections.sort(questions, (q1, q2) -> Integer.compare(q1.getLevel(), q2.getLevel()));
        } catch (Exception ignored) {}
    }

    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }
}


