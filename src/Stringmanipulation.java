import java.util.Scanner;

public class Stringmanipulation {
    public static void main(String[] args) {
        Scanner input=new Scanner(System.in);
        String word;
        System.out.println("type your string input down here");
        word=input.nextLine();

      /*if (word.isEmpty()==true)
           System.out.println("you haven't typed anything");
        else{
            System.out.println(word);
        } */  
       while(word.isEmpty()==true){
        System.out.println("this string is empty please type a string");
          word=input.nextLine();
       }
         System.out.println(word.trim());
         word.toUpperCase();
         System.out.println(word.replaceAll("(?i)[aeiou]","*"));
         if(word.toLowerCase().contains("java"))
          System.out.println("your word contains one of the key words java");
          if(word.equalsIgnoreCase("I love Java")){
             System.out.println("bingo that is the key word with length" + word.length());
          }
          else{
            System.out.println(word + "is not the key word");
          }
        input.close();

    }
}