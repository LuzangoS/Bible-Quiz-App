import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SwingQuizApp extends JFrame {
    private List<JSONObject> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;

    private JLabel titleLabel;
    private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup optionsGroup;
    private JButton submitButton;
    private JLabel timerLabel;
    private Timer countdownTimer;
    private int timeLeft = 30;

    private JSONObject currentQuestion;

    public SwingQuizApp() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true); // Add this line - enables min/max buttons
        setTitle("Bible Quiz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        setLayout(new BorderLayout());
        
        // Set background color
        getContentPane().setBackground(new Color(240, 240, 240));

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        // Title
        titleLabel = new JLabel("BIBLE QUIZ", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(new Color(60, 60, 60));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Center panel for question and options
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(240, 240, 240));

        // Question panel
        questionLabel = new JLabel("Question goes here", SwingConstants.LEFT);
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        questionLabel.setForeground(new Color(80, 80, 80));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        questionLabel.setVerticalAlignment(SwingConstants.TOP);
        centerPanel.add(questionLabel, BorderLayout.NORTH);

        // Options panel
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBackground(new Color(240, 240, 240));
        
        optionButtons = new JRadioButton[4];
        optionsGroup = new ButtonGroup();
        
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton("Option" + (i + 1));
            optionButtons[i].setFont(new Font("Arial", Font.PLAIN, 18));
            optionButtons[i].setForeground(new Color(100, 100, 100));
            optionButtons[i].setBackground(new Color(240, 240, 240));
            optionButtons[i].setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
            optionButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            optionsGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }
        centerPanel.add(optionsPanel, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel for timer and submit button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(240, 240, 240));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        // Timer
        timerLabel = new JLabel("timer");
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        timerLabel.setForeground(new Color(120, 120, 120));
        bottomPanel.add(timerLabel, BorderLayout.WEST);

        // Submit button
        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.PLAIN, 18));
        submitButton.setBackground(new Color(220, 220, 220));
        submitButton.setForeground(new Color(80, 80, 80));
        submitButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        submitButton.addActionListener(this::handleAnswer);
        bottomPanel.add(submitButton, BorderLayout.CENTER);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);

        // Load questions and start
        loadQuestions();
        displayQuestion();

        setVisible(true);
    }

    private void startTimer() {
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
        timeLeft = 30;
        updateTimerDisplay();
        
        countdownTimer = new Timer(1000, e -> {
            timeLeft--;
            updateTimerDisplay();
            if (timeLeft <= 0) {
                countdownTimer.stop();
                handleTimeUp();
            }
        });
        countdownTimer.start();
    }

    private void updateTimerDisplay() {
        timerLabel.setText("Time: " + timeLeft + "s");
        if (timeLeft <= 10) {
            timerLabel.setForeground(Color.RED);
        } else {
            timerLabel.setForeground(new Color(120, 120, 120));
        }
    }

    private void handleTimeUp() {
        // When time runs out, end the quiz immediately
        showFinalScore();
    }

    private void loadQuestions() {
        try {
            String content = new String(Files.readAllBytes(new File("Questions.json").toPath()));
            JSONArray questions = new JSONArray(content);

            questionList = new ArrayList<>();
            for (int i = 0; i < questions.length(); i++) {
                questionList.add(questions.getJSONObject(i));
            }
            Collections.shuffle(questionList);
            if (questionList.size() > 15) {
                questionList = questionList.subList(0, 15);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load questions: " + e.getMessage());
            System.exit(1);
        }
    }

    private void displayQuestion() {
        if (currentQuestionIndex >= questionList.size()) {
            showFinalScore();
            return;
        }

        // Reset all option button colors and icons
        for (JRadioButton button : optionButtons) {
            button.setForeground(new Color(100, 100, 100));
            button.setIcon(null);
        }

        currentQuestion = questionList.get(currentQuestionIndex);
        questionLabel.setText("<html><body style='width: 800px'>" + 
            (currentQuestionIndex + 1) + ". " + currentQuestion.getString("question") + 
            "</body></html>");
        
        JSONArray options = currentQuestion.getJSONArray("options");

        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(options.getString(i));
            optionButtons[i].setSelected(false);
        }

        submitButton.setEnabled(true);
        startTimer();
    }

    private void handleAnswer(ActionEvent event) {
        int selected = -1;
        for (int i = 0; i < optionButtons.length; i++) {
            if (optionButtons[i].isSelected()) {
                selected = i;
                break;
            }
        }

        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Please select an option!");
            return;
        }

        // Stop the timer
        if (countdownTimer != null) {
            countdownTimer.stop();
        }

        String selectedAnswer = currentQuestion.getJSONArray("options").getString(selected);
        String correctAnswer = currentQuestion.getString("correct_answer");

        // Find the correct answer index
        int correctIndex = -1;
        JSONArray options = currentQuestion.getJSONArray("options");
        for (int i = 0; i < options.length(); i++) {
            if (options.getString(i).equals(correctAnswer)) {
                correctIndex = i;
                break;
            }
        }

        // Show visual feedback
        if (selectedAnswer.equals(correctAnswer)) {
            // Correct answer - show green tick on selected option
            score++;
            optionButtons[selected].setIcon(createTickIcon(Color.GREEN));
            optionButtons[selected].setForeground(new Color(0, 150, 0));
        } else {
            // Wrong answer - show red cross on selected, green tick on correct
            optionButtons[selected].setIcon(createCrossIcon(Color.RED));
            optionButtons[selected].setForeground(Color.RED);
            
            if (correctIndex != -1) {
                optionButtons[correctIndex].setIcon(createTickIcon(Color.GREEN));
                optionButtons[correctIndex].setForeground(new Color(0, 150, 0));
            }
        }

        submitButton.setEnabled(false);

        // Wait 2 seconds before next question
        Timer delayTimer = new Timer(2000, e -> {
            currentQuestionIndex++;
            displayQuestion();
        });
        delayTimer.setRepeats(false);
        delayTimer.start();
    }

    private Icon createTickIcon(Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(3));
                
                // Draw tick mark
                int[] xPoints = {x + 3, x + 8, x + 15};
                int[] yPoints = {y + 8, y + 13, y + 3};
                for (int i = 0; i < xPoints.length - 1; i++) {
                    g2.drawLine(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]);
                }
                g2.dispose();
            }

            @Override
            public int getIconWidth() { return 20; }

            @Override
            public int getIconHeight() { return 16; }
        };
    }

    private Icon createCrossIcon(Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(3));
                
                // Draw cross
                g2.drawLine(x + 3, y + 3, x + 13, y + 13);
                g2.drawLine(x + 13, y + 3, x + 3, y + 13);
                g2.dispose();
            }

            @Override
            public int getIconWidth() { return 16; }

            @Override
            public int getIconHeight() { return 16; }
        };
    }

    private void showFinalScore() {
        if (countdownTimer != null) {
            countdownTimer.stop();
        }

        // Clear the current content
        getContentPane().removeAll();
        
        // Create final score panel
        JPanel finalPanel = new JPanel(new BorderLayout());
        finalPanel.setBackground(new Color(240, 240, 240));
        finalPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));

      // End of Quiz title with congratulations for high scores
        JLabel endLabel;
        if (score >= 1) {
        endLabel = new JLabel("🎉 CONGRATULATIONS! 🎉", SwingConstants.CENTER);
        endLabel.setForeground(new Color(255, 215, 0)); // Gold color
        } else {
        endLabel = new JLabel("END OF QUIZ", SwingConstants.CENTER);
        endLabel.setForeground(new Color(60, 60, 60));
        }
        endLabel.setFont(new Font("Arial", Font.BOLD, 48));
        endLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        finalPanel.add(endLabel, BorderLayout.NORTH);

        // Score display with timeout message if applicable
        String scoreText;
        if (timeLeft <= 0) {
        scoreText = "<html><center>⏰ Time's Up!<br><br>Your Score: " + score + "/" + questionList.size() + "</center></html>";
        } else {
        scoreText = "Your Score: " + score + "/" + questionList.size();
        }

        // Add celebration emojis for high scores
        if (score >= 12) {
        scoreText = "🌟 " + scoreText + " 🌟";
        }
        JLabel scoreLabel = new JLabel(scoreText, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 36));
        
        // Color based on performance
        double percentage = (double) score / questionList.size();
        if (percentage >= 0.8) {
            scoreLabel.setForeground(new Color(0, 150, 0)); // Green for good score
        } else if (percentage >= 0.6) {
            scoreLabel.setForeground(new Color(255, 165, 0)); // Orange for average
        } else {
            scoreLabel.setForeground(Color.RED); // Red for low score
        }
        
        finalPanel.add(scoreLabel, BorderLayout.CENTER);

        // Button panel with Try Again and Quit options
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(new Color(240, 240, 240));
        
        JButton tryAgainButton = new JButton("Try Again");
        tryAgainButton.setFont(new Font("Arial", Font.PLAIN, 20));
        tryAgainButton.setBackground(new Color(0, 150, 0));
        tryAgainButton.setForeground(Color.WHITE);
        tryAgainButton.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        tryAgainButton.addActionListener(e -> restartQuiz());
        
        JButton quitButton = new JButton("Quit");
        quitButton.setFont(new Font("Arial", Font.PLAIN, 20));
        quitButton.setBackground(new Color(200, 50, 50));
        quitButton.setForeground(Color.WHITE);
        quitButton.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        quitButton.addActionListener(e -> System.exit(0));
        
        buttonPanel.add(tryAgainButton);
        buttonPanel.add(quitButton);
        finalPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(finalPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void restartQuiz() {
        // Reset quiz state
        currentQuestionIndex = 0;
        score = 0;
        timeLeft = 30;
        
        // Stop any running timer
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
        
        // Clear and rebuild the main interface
        getContentPane().removeAll();
        
        // Recreate the main panel (same as constructor)
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        // Title
        titleLabel = new JLabel("BIBLE QUIZ", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(new Color(60, 60, 60));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Center panel for question and options
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(240, 240, 240));

        // Question panel
        questionLabel = new JLabel("Question goes here", SwingConstants.LEFT);
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        questionLabel.setForeground(new Color(80, 80, 80));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        questionLabel.setVerticalAlignment(SwingConstants.TOP);
        centerPanel.add(questionLabel, BorderLayout.NORTH);

        // Options panel
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBackground(new Color(240, 240, 240));
        
        optionButtons = new JRadioButton[4];
        optionsGroup = new ButtonGroup();
        
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton("Option" + (i + 1));
            optionButtons[i].setFont(new Font("Arial", Font.PLAIN, 18));
            optionButtons[i].setForeground(new Color(100, 100, 100));
            optionButtons[i].setBackground(new Color(240, 240, 240));
            optionButtons[i].setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
            optionButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            optionsGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }
        centerPanel.add(optionsPanel, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel for timer and submit button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(240, 240, 240));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        // Timer
        timerLabel = new JLabel("timer");
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        timerLabel.setForeground(new Color(120, 120, 120));
        bottomPanel.add(timerLabel, BorderLayout.WEST);

        // Submit button
        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.PLAIN, 18));
        submitButton.setBackground(new Color(220, 220, 220));
        submitButton.setForeground(new Color(80, 80, 80));
        submitButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        submitButton.addActionListener(this::handleAnswer);
        bottomPanel.add(submitButton, BorderLayout.CENTER);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);
        
        // Shuffle questions again and start
        Collections.shuffle(questionList);
        if (questionList.size() > 15) {
            questionList = questionList.subList(0, 15);
        }
        
        displayQuestion();
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingQuizApp::new);
    }
}