import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class SimpleJsonTest {

    public static void main(String[] args) {
        try {
            // Load JSON from file
            String content = new String(Files.readAllBytes(new File("Questions.json").toPath()));
            JSONArray questions = new JSONArray(content);

            Scanner scanner = new Scanner(System.in);
            int score = 0;

           List<JSONObject> questionList = new ArrayList<>();
           for (int i = 0; i < questions.length(); i++) {
             questionList.add(questions.getJSONObject(i));
           }
            Collections.shuffle(questionList);
            for (int i = 0; i < questionList.size(); i++)  {{
                JSONObject questionObj = questionList.get(i);
                String question = questionObj.getString("question");
                JSONArray options = questionObj.getJSONArray("options");
                String correctAnswer = questionObj.getString("correct_answer");

                // Display the question
                System.out.println((i + 1) + ". " + question);

                // Print options with A-D labels
                char label = 'A';
                for (int j = 0; j < options.length(); j++) {
                    System.out.println("  " + label + ". " + options.getString(j));
                    label++;
                }

                // Get user input
                System.out.print("Your answer (A-D): ");
                String input = scanner.nextLine().toUpperCase();

                // Convert input letter to index (A = 0, B = 1, etc.)
                int selectedIndex = input.charAt(0) - 'A';

                if (selectedIndex >= 0 && selectedIndex < options.length()) {
                    String selectedAnswer = options.getString(selectedIndex);
                    if (selectedAnswer.equals(correctAnswer)) {
                        System.out.println("✅ Correct!\n");
                        score++;
                    } else {
                        System.out.println("❌ Wrong! Correct answer: " + correctAnswer + "\n");
                    }
                } else {
                    System.out.println("❗ Invalid option. Skipping question.\n");
                }
            }
        }

            // Final score
            System.out.println("Quiz complete! Your score: " + score + "/" + questions.length());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
