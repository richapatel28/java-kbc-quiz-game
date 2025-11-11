package com.kbc.game;

import com.kbc.auth.Authenticator;
import com.kbc.model.Question;
import com.kbc.model.QuestionBank;
import com.kbc.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController {
    private final User user;
    private final QuestionBank questionBank;
    private int index;
    private int currentEarnings;
    private int correctCount;
    private boolean lifeline5050Used;
    private boolean lifelineAudienceUsed;
    private final Random random;
    private final Authenticator authenticator;

    public GameController(User user, Authenticator authenticator) {
        this.user = user;
        this.authenticator = authenticator;
        this.questionBank = new QuestionBank();
        this.index = 0;
        this.currentEarnings = 0;
        this.correctCount = 0;
        this.random = new Random();
    }

    public boolean hasNext() {
        return index < questionBank.getQuestions().size();
    }

    public Question getCurrentQuestion() {
        if (!hasNext()) return null;
        return questionBank.getQuestions().get(index);
    }

    public boolean submitAnswer(String selectedOptionLetter) {
        Question q = getCurrentQuestion();
        if (q == null) return false;
        boolean correct = q.getAnswer().equalsIgnoreCase(selectedOptionLetter);
        if (correct) {
            currentEarnings = q.getPrize();
            correctCount++;
            index++;
        } else {
            // game over
        }
        return correct;
    }

    public void quitGame() {
        // keep currentEarnings
    }

    public int getCurrentEarnings() { return currentEarnings; }
    public int getCorrectCount() { return correctCount; }

    public boolean isLifeline5050Used() { return lifeline5050Used; }
    public boolean isLifelineAudienceUsed() { return lifelineAudienceUsed; }

    public List<String> use5050() {
        if (lifeline5050Used) return new ArrayList<>();
        lifeline5050Used = true;
        Question q = getCurrentQuestion();
        if (q == null) return new ArrayList<>();
        List<String> remove = new ArrayList<>();
        String correct = q.getAnswer().toUpperCase();
        List<String> options = List.of("A", "B", "C", "D");
        List<String> wrongs = new ArrayList<>();
        for (String o : options) {
            if (!o.equals(correct)) wrongs.add(o);
        }
        // remove two random wrong options
        while (remove.size() < 2 && !wrongs.isEmpty()) {
            int idx = random.nextInt(wrongs.size());
            remove.add(wrongs.remove(idx));
        }
        return remove;
    }

    public int[] useAudiencePoll() {
        if (lifelineAudienceUsed) return new int[]{0,0,0,0};
        lifelineAudienceUsed = true;
        Question q = getCurrentQuestion();
        if (q == null) return new int[]{0,0,0,0};
        String correct = q.getAnswer().toUpperCase();
        int correctBias = 50 + random.nextInt(31); // 50..80
        int remaining = 100 - correctBias;
        int x = random.nextInt(remaining + 1);
        int y = random.nextInt(remaining - x + 1);
        int z = remaining - x - y;
        int a=0,b=0,c=0,d=0;
        switch (correct) {
            case "A": a = correctBias; b = x; c = y; d = z; break;
            case "B": b = correctBias; a = x; c = y; d = z; break;
            case "C": c = correctBias; a = x; b = y; d = z; break;
            default: d = correctBias; a = x; b = y; c = z; break;
        }
        return new int[]{a,b,c,d};
    }

    public void persistHighScoreIfNeeded() {
        if (user.getHighScore() < currentEarnings) {
            authenticator.updateHighScore(user.getUsername(), currentEarnings);
            user.setHighScore(currentEarnings);
        }
    }
}


