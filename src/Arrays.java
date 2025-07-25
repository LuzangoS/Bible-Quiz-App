import java.util.ArrayList;
import java.util.Scanner;

public class Arrays{
    public static void main(String[] args) {
        Scanner read=new Scanner(System.in);
        
        ArrayList<Integer> numbers= new ArrayList<>();
        numbers.add(3);
        numbers.add(45);
         System.out.println("what number do u want to add");
         while (!read.hasNextInt()){
             System.out.println("That is not interger add an interger");
             read.next();
         }
        int a=read.nextInt();
        numbers.add(a);

        //numbers.add(4,a);//this gives an error of ArrayIndexOutofBound because it can only add to the index within the range it has in this case 3 is the maximum
        numbers.add(1,a);//adds the user entered number in the Arraylist at index 1 and pushes what was at that index to the next index

        System.out.println(numbers);//outputs all the elements in an Arraylist

        numbers.remove(3);//deletes what is at index 3
        numbers.set(1,4);//replaces what is at index 1 with 4

        System.out.println(numbers);

       System.out.println("Guess a number in the array");
         while (!read.hasNextInt()){
            //read.hasNextInt() checks if what the user has entered is an interger
             System.out.println("That is not interger add an interger");
             read.next();//removes the wrong value the user entered and adds prevents infinite loops
         }
          int b=read.nextInt();
        if (numbers.contains(b))//you can use "nameofarraylist.contains(element u are check ifit contains)"in ArrayList
         System.out.println("The number you have entered exists in this array");
        else{
         System.out.println("Does not exist");
        }
      
    read.close();
    }
}