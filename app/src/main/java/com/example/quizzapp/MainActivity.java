package com.example.quizzapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private String[] questions = {
            "What is the capital of France?",
            "Which planet is known as the Red Planet?",
            "What is the largest mammal?",
            "Who wrote 'Romeo and Juliet'?",
            "What is the square root of 64?",
            "Which element has the chemical symbol 'O'?",
            "What is the tallest mountain in the world?",
            "Which country is home to the kangaroo?",
            "Who painted the Mona Lisa?",
            "What is the hardest natural substance on Earth?"
    };

    private String[][] options = {
            {"Berlin", "Madrid", "Paris", "Rome"},
            {"Earth", "Mars", "Jupiter", "Venus"},
            {"Elephant", "Blue Whale", "Shark", "Giraffe"},
            {"Shakespeare", "Tolstoy", "Hemingway", "Fitzgerald"},
            {"6", "7", "8", "9"},
            {"Oxygen", "Gold", "Osmium", "Oganesson"},
            {"Mount Everest", "K2", "Mount Kilimanjaro", "Mount Fuji"},
            {"Australia", "USA", "India", "Brazil"},
            {"Leonardo da Vinci", "Picasso", "Van Gogh", "Michelangelo"},
            {"Diamond", "Gold", "Iron", "Graphite"}
    };

    private int[] correctanswers = {2, 1, 1, 0, 2, 0, 0, 0, 0, 0};
    private int score = 0;
    private int currentQuestion = 0;
    private int[] selectedAnswers = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1};

    private TextView questionText, scoreText, hintText, timerText, gameovertext;
    private RadioGroup optionGroup;
    private RadioButton option1, option2, option3, option4;
    private Button nextButton, previousButton, hint, start;
    private View quizLayout, gameOver;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 120000;
    private static final long TIMER_INTERVAL = 1000;

    // Track if the hint button has been used
    private boolean hintUsed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        questionText = findViewById(R.id.Question_text);
        optionGroup = findViewById(R.id.option_group);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        scoreText = findViewById(R.id.score_text);
        hintText = findViewById(R.id.hint_text);
        timerText = findViewById(R.id.timer_text);
        nextButton = findViewById(R.id.next_button);
        previousButton = findViewById(R.id.previous_button);
        hint = findViewById(R.id.hint);
        start = findViewById(R.id.start);
        quizLayout = findViewById(R.id.quiz_layout);
        gameOver = findViewById(R.id.game_over);
        gameovertext = findViewById(R.id.gameoverText);

        quizLayout.setVisibility(View.GONE);
        gameOver.setVisibility(View.GONE);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start.setVisibility(View.GONE);
                quizLayout.setVisibility(View.VISIBLE);
                resetQuiz();
                startTimer();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer();
                hintText.setText("");
                if (currentQuestion < questions.length - 1) {
                    currentQuestion++;
                    setQuestion(currentQuestion);
                    hintUsed = false;
                } else {
                    endQuiz();
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintText.setText("");
                if (currentQuestion > 0) {
                    currentQuestion--;
                    setQuestion(currentQuestion);
                    hintUsed = false;
                }
            }
        });

        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hintUsed) {
                    hintText.setText(options[currentQuestion][correctanswers[currentQuestion]]);
                    score--;
                    scoreText.setText("Score: " + score);
                    hintUsed = true;
                }
            }
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, TIMER_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerDisplay();
            }

            @Override
            public void onFinish() {
                timerText.setText("Time's up!");
                endQuiz();
            }
        }.start();
    }

    private void updateTimerDisplay() {
        long minutes = timeLeftInMillis / 60000;
        long seconds = (timeLeftInMillis % 60000) / 1000;
        timerText.setText(String.format("Time: %02d:%02d", minutes, seconds));
    }

    private void setQuestion(int qIndex) {
        questionText.setText("Question: " + questions[qIndex]);
        option1.setText(options[qIndex][0]);
        option2.setText(options[qIndex][1]);
        option3.setText(options[qIndex][2]);
        option4.setText(options[qIndex][3]);

        if (selectedAnswers[qIndex] != -1) {
            ((RadioButton) optionGroup.getChildAt(selectedAnswers[qIndex])).setChecked(true);
        } else {
            optionGroup.clearCheck();
        }

        // Re-enable the hint button for the new question
        hint.setEnabled(true);
        hintUsed = false;
    }

    private void checkAnswer() {
        int selectedOption = optionGroup.getCheckedRadioButtonId();
        if (selectedOption != -1) {
            int selectedAnswer = optionGroup.indexOfChild(findViewById(selectedOption));
            selectedAnswers[currentQuestion] = selectedAnswer;

            if (selectedAnswer == correctanswers[currentQuestion]) {
                score += 5;
            } else {
                score--;
            }
        }
        scoreText.setText("Score: " + score);
    }

    private void resetQuiz() {
        gameOver.setVisibility(View.GONE);
        score = 0;
        currentQuestion = 0;
        selectedAnswers = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        scoreText.setText("Score: 0");
        timeLeftInMillis = 120000;
        setQuestion(currentQuestion);
        nextButton.setEnabled(true);
        previousButton.setEnabled(true);
    }

    private void endQuiz() {
        countDownTimer.cancel();
        long timeTakenMillis = 120000 - timeLeftInMillis;
        long timeTakenSeconds = timeTakenMillis / 1000; // time taken in seconds
        long minutes = timeTakenSeconds / 60; // calculaing minutes
        long seconds = timeTakenSeconds % 60; // Calculating  seconds

        String timeTakenFormatted;
        if (minutes > 0) {
            timeTakenFormatted = String.format("%d minutes and %d seconds", minutes, seconds);
        } else {
            timeTakenFormatted = String.format("%d seconds", seconds);
        }

        quizLayout.setVisibility(View.GONE);
        gameOver.setVisibility(View.VISIBLE);
        gameovertext.setText("Quiz Over! Your score is: " + score + "\nTime taken: " + timeTakenFormatted);

        nextButton.setEnabled(false);
        previousButton.setEnabled(false);

        start.setVisibility(View.VISIBLE);
    }
}
