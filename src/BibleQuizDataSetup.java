import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

// Question class to represent each Bible quiz question
class Question {
    private String question;
    private String option_a;
    private String option_b;
    private String option_c;
    private String option_d;
    private String answer; // Should be 'A', 'B', 'C', or 'D'
    
    // Constructor
    public Question(String question, String option_a, String option_b, 
                   String option_c, String option_d, String answer) {
        this.question = question;
        this.option_a = option_a;
        this.option_b = option_b;
        this.option_c = option_c;
        this.option_d = option_d;
        this.answer = answer;
    }
    
    // Getters
    public String getQuestion() { return question; }
    public String getOption_a() { return option_a; }
    public String getOption_b() { return option_b; }
    public String getOption_c() { return option_c; }
    public String getOption_d() { return option_d; }
    public String getAnswer() { return answer; }
    
    // Setters
    public void setQuestion(String question) { this.question = question; }
    public void setOption_a(String option_a) { this.option_a = option_a; }
    public void setOption_b(String option_b) { this.option_b = option_b; }
    public void setOption_c(String option_c) { this.option_c = option_c; }
    public void setOption_d(String option_d) { this.option_d = option_d; }
    public void setAnswer(String answer) { this.answer = answer; }
    
    // Get option by letter (A, B, C, D)
    public String getOptionByLetter(String letter) {
        switch (letter.toUpperCase()) {
            case "A": return option_a;
            case "B": return option_b;
            case "C": return option_c;
            case "D": return option_d;
            default: return "";
        }
    }
    
    // Check if given answer is correct
    public boolean isCorrect(String userAnswer) {
        return answer.equalsIgnoreCase(userAnswer);
    }
    
    @Override
    public String toString() {
        return "Question: " + question + "\n" +
               "A) " + option_a + "\n" +
               "B) " + option_b + "\n" +
               "C) " + option_c + "\n" +
               "D) " + option_d + "\n" +
               "Answer: " + answer;
    }
}

public class BibleQuizDataSetup {
    
    public static void main(String[] args) {
        // Step 1: Create sample questions
        List<Question> questions = createSampleQuestions();
        
        // Step 2: Save questions to JSON file
        saveQuestionsToJSON(questions, "bible_questions.json");
        
        // Step 3: Test loading questions from JSON file
        List<Question> loadedQuestions = loadQuestionsFromJSON("bible_questions.json");
        
        // Step 4: Display results
        if (loadedQuestions != null) {
            System.out.println("Successfully loaded " + loadedQuestions.size() + " questions!");
            System.out.println("\nFirst 3 questions:");
            for (int i = 0; i < Math.min(3, loadedQuestions.size()); i++) {
                System.out.println("\n" + (i + 1) + ". " + loadedQuestions.get(i));
            }
        }
    }
    
    // Create sample Bible questions (you can expand this to 200+)
    public static List<Question> createSampleQuestions() {
        List<Question> questions = new ArrayList<>();
        
        questions.add(new Question(
            "Who was the first man created by God?",
            "Moses", "Adam", "Abraham", "Noah", "B"
        ));
        
        questions.add(new Question(
            "How many days did God take to create the world?",
            "5 days", "6 days", "7 days", "8 days", "B"
        ));
        
        questions.add(new Question(
            "Who built the ark to save animals from the flood?",
            "Moses", "Abraham", "Noah", "David", "C"
        ));
        
        questions.add(new Question(
            "What was the first plague in Egypt?",
            "Locusts", "Water turned to blood", "Frogs", "Darkness", "B"
        ));
        
        questions.add(new Question(
            "Who was swallowed by a great fish?",
            "Jonah", "Job", "Joshua", "Jeremiah", "A"
        ));
        
        questions.add(new Question(
            "How many disciples did Jesus have?",
            "10", "11", "12", "13", "C"
        ));
        
        questions.add(new Question(
            "Who betrayed Jesus?",
            "Peter", "Judas Iscariot", "Thomas", "Matthew", "B"
        ));
        
        questions.add(new Question(
            "What did Moses use to part the Red Sea?",
            "His hands", "A sword", "His staff", "A prayer", "C"
        ));
        
        questions.add(new Question(
            "Who was known for his great strength?",
            "Samson", "Solomon", "Samuel", "Saul", "A"
        ));
        
        questions.add(new Question(
            "What did David use to defeat Goliath?",
            "A sword", "A spear", "A sling and stone", "An arrow", "C"
        ));
        
        questions.add(new Question(
            "Who was the mother of Jesus?",
            "Martha", "Mary", "Sarah", "Ruth", "B"
        ));
        
        questions.add(new Question(
            "In which city was Jesus born?",
            "Nazareth", "Jerusalem", "Bethlehem", "Capernaum", "C"
        ));
        
        questions.add(new Question(
            "What are the first four books of the New Testament called?",
            "Epistles", "Gospels", "Acts", "Revelations", "B"
        ));
        
        questions.add(new Question(
            "Who wrote most of the letters in the New Testament?",
            "Peter", "John", "Paul", "James", "C"
        ));
        
        questions.add(new Question(
            "How many books are in the Bible?",
            "64", "65", "66", "67", "C"
        ));
        
        questions.add(new Question(
            "What was the original language of most of the Old Testament?",
            "Greek", "Latin", "Aramaic", "Hebrew", "D"
        ));
        
        questions.add(new Question(
            "Who led the Israelites out of Egypt?",
            "Aaron", "Moses", "Joshua", "Caleb", "B"
        ));
        
        questions.add(new Question(
            "What did Jesus turn water into at the wedding?",
            "Wine", "Milk", "Oil", "Honey", "A"
        ));
        
        questions.add(new Question(
            "How many loaves and fish did Jesus use to feed 5000 people?",
            "3 loaves, 1 fish", "5 loaves, 2 fish", "7 loaves, 3 fish", "2 loaves, 5 fish", "B"
        ));
        
        questions.add(new Question(
            "Who was the wisest king of Israel?",
            "David", "Saul", "Solomon", "Hezekiah", "C"
        ));
        
        System.out.println("Created " + questions.size() + " sample questions.");
        return questions;
    }
    
    // Save questions to JSON file
    public static void saveQuestionsToJSON(List<Question> questions, String filename) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter(filename);
            gson.toJson(questions, writer);
            writer.close();
            System.out.println("Questions saved to " + filename);
        } catch (IOException e) {
            System.err.println("Error saving questions: " + e.getMessage());
        }
    }
    
    // Load questions from JSON file
    public static List<Question> loadQuestionsFromJSON(String filename) {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(filename);
            
            // Define the type for List<Question>
            Type questionListType = new TypeToken<List<Question>>(){}.getType();
            
            List<Question> questions = gson.fromJson(reader, questionListType);
            reader.close();
            
            System.out.println("Questions loaded from " + filename);
            return questions;
            
        } catch (IOException e) {
            System.err.println("Error loading questions: " + e.getMessage());
            return null;
        }
    }
    
    // Utility method to test a single question
    public static void testQuestion(Question q) {
        System.out.println("\n" + q);
        System.out.println("Correct answer text: " + q.getOptionByLetter(q.getAnswer()));
        System.out.println("Is 'A' correct? " + q.isCorrect("A"));
        System.out.println("Is '" + q.getAnswer() + "' correct? " + q.isCorrect(q.getAnswer()));
    }
}