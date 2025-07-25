class Doggy {
    private String name;          // Instance variable
    private static int count = 0; // Static (class) variable

    // Constructor
    public Doggy(String name) {
        this.name = name;
        count++;  // Increase shared dog count
    }

    // Static method to get total count
    public static int getTotalDogs() {
        return count;
    }
}

public class Dog{
    public static void main(String[] args) {
        Doggy d1 = new Doggy("Rex");
        Doggy d2 = new Doggy("Luna");
    
        System.out.println(Doggy.getTotalDogs());  // Outputs: 2
    }
}

