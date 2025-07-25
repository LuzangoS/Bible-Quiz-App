// This document involves the notes taken on 4th March, 2025.
 class Person {
        String name = "John Does";
        // can be overriden because it is instance
        public void walk() {
            System.out.println(name+"walks"); 
        }
        //the following cannot be overidden
        public static void walking(){
            System.out.println(" is walking");
        }
    }

 class Student extends Person{
        String name = "Jane Does";
        // You cannot reduce the visbility.
        @Override // enforce that this method exists in the super class
        // The instances must be the same
        public void walk() {
            System.out.println(name+"walks"); 
        }

        public static void walking(){
            System.out.println(" is walking");
        }
    }
    
    public class Main2 {

    public static void main(String[] args) {
        // In Java a class can only extend one other class
        // Is it possible for class to have several sub classes ? TRUE OR FALSE; True
        // Everything (members) non private is inherited.
        // if a constructor calling the parent, the code should be in the first class.
        // If a superclass has fields, if it has non instance fields, and sub class
        // exaclty the same members then the sub class is said to be hiding those the
        // supperclass
        // If you have an instance method in the super class and it is also appearing in
        // the sub class then is called METHOD OVERIDING.
        // You OVERIDE to change the implementation
        // If the name same and the signature is the same the it causes an error.
        // If you have a method in the superclass and the you overide in the subclass
        // you cannnot reduce its visibility
        // - This means that if you are overide a publiic method in the superclass, you
        // cannot be it private but you can make it private then public becasue then you
        // are not reducing the visibility
        // A static method rewritten in the subclass, it is not method overiding it is method hiding. Overiding doesn't apply to static members
      
        Person person = new Person();
        Student student = new Student();
        Person person2 = new Student();
        if(person2 instanceof Person){
            System.out.println("Yes");
        }else{
            System.out.println("No!");
        }
        person.walk();
        person2.walking();
        student.walking();


        System.out.println(((Person)student).name);
        System.out.println(person2.getClass());
    }
}