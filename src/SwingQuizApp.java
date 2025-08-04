import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SwingQuizApp extends JFrame {
    private static final Color TRANSLUCENT_WHITE = new Color(255, 255, 255, 180); // More translucent
    private List<JSONObject> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int timeLeft = 30;

    private JLabel titleLabel;
    private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup optionsGroup;
    private JButton submitButton;
    private JLabel timerLabel;
    private Timer countdownTimer;
    private JSONObject currentQuestion;
    private JPanel quizPanel;

    public SwingQuizApp() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setTitle("Bible Quiz");
        setSize(1000, 700);
        setLocationRelativeTo(null);

        ImageIcon backgroundIcon = new ImageIcon("jesus_hug.jpg");
        Image backgroundImage = backgroundIcon.getImage().getScaledInstance(1000, 700, Image.SCALE_SMOOTH);
        JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
        backgroundLabel.setLayout(new GridBagLayout());
        setContentPane(backgroundLabel);

        quizPanel = new TranslucentRoundedPanel(30);
        quizPanel.setBackground(TRANSLUCENT_WHITE); // Use translucent background
        quizPanel.setLayout(new BorderLayout());
        quizPanel.setPreferredSize(new Dimension(700, 550));

        titleLabel = new JLabel("BIBLE QUIZ", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        quizPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        questionLabel = new JLabel("Question goes here", SwingConstants.LEFT);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 28));
        questionLabel.setForeground(Color.BLACK);
        questionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        questionLabel.setVerticalAlignment(SwingConstants.TOP);
        centerPanel.add(questionLabel);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setOpaque(false);

        optionButtons = new JRadioButton[4];
        optionsGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton("Option" + (i + 1));
            optionButtons[i].setFont(new Font("Arial", Font.BOLD, 22));
            optionButtons[i].setForeground(Color.BLACK);
            optionButtons[i].setOpaque(false);
            optionButtons[i].setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
            optionButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            optionsGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }

        centerPanel.add(optionsPanel);
        quizPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        JPanel timerPanel = new JPanel();
        timerPanel.setOpaque(false);
        timerLabel = new JLabel("Time: 30s");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 30));
        timerLabel.setForeground(Color.ORANGE);
        timerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        timerPanel.add(timerLabel);
        bottomPanel.add(timerPanel, BorderLayout.NORTH);

        submitButton = new JButton("SUBMIT");
        submitButton.setFont(new Font("Arial", Font.BOLD, 20));
        submitButton.setBackground(new Color(200, 162, 200));
        submitButton.setForeground(Color.BLACK);
        submitButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        submitButton.addActionListener(this::handleAnswer);

        JPanel submitPanel = new JPanel();
        submitPanel.setOpaque(false);
        submitPanel.add(submitButton);
        bottomPanel.add(submitPanel, BorderLayout.CENTER);

        quizPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(quizPanel);

        loadQuestions();
        displayQuestion();

        setVisible(true);
    }

    private void handleAnswer(ActionEvent e) {
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

        countdownTimer.stop();
        String selectedAnswer = currentQuestion.getJSONArray("options").getString(selected);
        String correctAnswer = currentQuestion.getString("correct_answer");

        int correctIndex = -1;
        JSONArray options = currentQuestion.getJSONArray("options");
        for (int i = 0; i < options.length(); i++) {
            if (options.getString(i).equals(correctAnswer)) {
                correctIndex = i;
                break;
            }
        }

        Icon greenTick = createTickIcon(Color.GREEN);
        Icon redCross = createCrossIcon(Color.RED);

        if (selectedAnswer.equals(correctAnswer)) {
            optionButtons[selected].setIcon(greenTick);
            score++;
        } else {
            optionButtons[selected].setIcon(redCross);
            if (correctIndex != -1) {
                optionButtons[correctIndex].setIcon(greenTick);
            }
        }

        for (JRadioButton button : optionButtons) {
            button.setEnabled(false);
        }

        currentQuestionIndex++;

        Timer delayTimer = new Timer(1500, evt -> displayQuestion());
        delayTimer.setRepeats(false);
        delayTimer.start();
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
            questionList = questionList.subList(0, Math.min(15, questionList.size()));
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

        currentQuestion = questionList.get(currentQuestionIndex);
        questionLabel.setText("<html><body style='width: 600px'>" +
                (currentQuestionIndex + 1) + ". " + currentQuestion.getString("question") +
                "</body></html>");

        JSONArray options = currentQuestion.getJSONArray("options");
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(options.getString(i));
            optionButtons[i].setSelected(false);
            optionButtons[i].setIcon(null);
            optionButtons[i].setEnabled(true);
        }

        startTimer();
    }

    private void startTimer() {
        if (countdownTimer != null) countdownTimer.stop();
        timeLeft = 30;
        updateTimerDisplay();
        countdownTimer = new Timer(1000, e -> {
            timeLeft--;
            updateTimerDisplay();
            if (timeLeft <= 0) {
                countdownTimer.stop();
                // Time's up - move to next question or show final score
                showFinalScore();
            }
        });
        countdownTimer.start();
    }

    private void updateTimerDisplay() {
        timerLabel.setText("TIME: " + timeLeft + "s");
        timerLabel.setForeground(timeLeft <= 10 ? Color.RED : new Color(255, 180, 0));
    }

    private void showFinalScore() {
        // Stop any running timer
        if (countdownTimer != null) {
            countdownTimer.stop();
        }

        // Clear the quiz panel and show final score
        quizPanel.removeAll();
        quizPanel.setLayout(new BorderLayout());

        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        scorePanel.setOpaque(false);
        scorePanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel finalTitleLabel = new JLabel("QUIZ COMPLETE!", SwingConstants.CENTER);
        finalTitleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        finalTitleLabel.setForeground(new Color(0, 120, 0)); // Dark green
        finalTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        finalTitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JLabel scoreLabel = new JLabel("Your Score:", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 36));
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel actualScoreLabel = new JLabel(score + " / " + questionList.size(), SwingConstants.CENTER);
        actualScoreLabel.setFont(new Font("Arial", Font.BOLD, 72));
        actualScoreLabel.setForeground(new Color(220, 20, 60)); // Crimson
        actualScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        actualScoreLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));

        // Calculate percentage and add encouraging message
        double percentage = (score * 100.0) / questionList.size();
        String message;
        Color messageColor;
        
        if (percentage >= 90) {
            message = "Excellent! You know your Bible well!";
            messageColor = new Color(0, 150, 0);
        } else if (percentage >= 70) {
            message = "Good job! Keep studying!";
            messageColor = new Color(0, 100, 200);
        } else if (percentage >= 50) {
            message = "Not bad! There's room for improvement.";
            messageColor = new Color(200, 100, 0);
        } else {
            message = "Keep reading and studying the Bible!";
            messageColor = new Color(200, 50, 50);
        }

        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 24));
        messageLabel.setForeground(messageColor);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JButton restartButton = new JButton("PLAY AGAIN");
        restartButton.setFont(new Font("Arial", Font.BOLD, 20));
        restartButton.setBackground(new Color(100, 200, 100));
        restartButton.setForeground(Color.BLACK);
        restartButton.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButton.addActionListener(e -> restartQuiz());

        JButton exitButton = new JButton("EXIT");
        exitButton.setFont(new Font("Arial", Font.BOLD, 20));
        exitButton.setBackground(new Color(200, 100, 100));
        exitButton.setForeground(Color.BLACK);
        exitButton.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(e -> System.exit(0));

        scorePanel.add(finalTitleLabel);
        scorePanel.add(scoreLabel);
        scorePanel.add(actualScoreLabel);
        scorePanel.add(messageLabel);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(restartButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(exitButton);
        scorePanel.add(buttonPanel);

        quizPanel.add(scorePanel, BorderLayout.CENTER);
        quizPanel.revalidate();
        quizPanel.repaint();
    }

    private void restartQuiz() {
        // Reset game state
        currentQuestionIndex = 0;
        score = 0;
        timeLeft = 30;

        // Rebuild the quiz interface
        quizPanel.removeAll();
        quizPanel.setLayout(new BorderLayout());

        titleLabel = new JLabel("BIBLE QUIZ", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        quizPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        questionLabel = new JLabel("Question goes here", SwingConstants.LEFT);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 28));
        questionLabel.setForeground(Color.BLACK);
        questionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        questionLabel.setVerticalAlignment(SwingConstants.TOP);
        centerPanel.add(questionLabel);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setOpaque(false);

        optionButtons = new JRadioButton[4];
        optionsGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton("Option" + (i + 1));
            optionButtons[i].setFont(new Font("Arial", Font.BOLD, 22));
            optionButtons[i].setForeground(Color.BLACK);
            optionButtons[i].setOpaque(false);
            optionButtons[i].setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
            optionButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            optionsGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }

        centerPanel.add(optionsPanel);
        quizPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        JPanel timerPanel = new JPanel();
        timerPanel.setOpaque(false);
        timerLabel = new JLabel("Time: 30s");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 30));
        timerLabel.setForeground(Color.ORANGE);
        timerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        timerPanel.add(timerLabel);
        bottomPanel.add(timerPanel, BorderLayout.NORTH);

        submitButton = new JButton("SUBMIT");
        submitButton.setFont(new Font("Arial", Font.BOLD, 20));
        submitButton.setBackground(new Color(200, 162, 200));
        submitButton.setForeground(Color.BLACK);
        submitButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        submitButton.addActionListener(this::handleAnswer);

        JPanel submitPanel = new JPanel();
        submitPanel.setOpaque(false);
        submitPanel.add(submitButton);
        bottomPanel.add(submitPanel, BorderLayout.CENTER);

        quizPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Reload and reshuffle questions
        loadQuestions();
        displayQuestion();

        quizPanel.revalidate();
        quizPanel.repaint();
    }

    private Icon createTickIcon(Color color) {
        return new Icon() {
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(3));
                int[] xPoints = {x + 3, x + 8, x + 15};
                int[] yPoints = {y + 8, y + 13, y + 3};
                for (int i = 0; i < xPoints.length - 1; i++) {
                    g2.drawLine(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]);
                }
                g2.dispose();
            }
            public int getIconWidth() { return 20; }
            public int getIconHeight() { return 16; }
        };
    }

    private Icon createCrossIcon(Color color) {
        return new Icon() {
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(3));
                g2.drawLine(x + 3, y + 3, x + 13, y + 13);
                g2.drawLine(x + 13, y + 3, x + 3, y + 13);
                g2.dispose();
            }
            public int getIconWidth() { return 16; }
            public int getIconHeight() { return 16; }
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingQuizApp::new);
    }
}

// Updated RoundedPanel class to support translucency
class TranslucentRoundedPanel extends JPanel {
    private final int radius;

    public TranslucentRoundedPanel(int radius) {
        super();
        this.radius = radius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Paint the translucent background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        
        g2.dispose();
        super.paintComponent(g);
    }
}