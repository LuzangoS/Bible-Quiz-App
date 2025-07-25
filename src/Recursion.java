import java.util.Scanner;

public class Recursion{
    public int Power(int base,int expo){
    if(expo==0){
        return 1;
    }
    else{
        return base*Power(base, expo-1);
    }
   
}
    public static void main(String[]args){
        Recursion obj=new Recursion();
        int result=obj.Power(2,3);
        System.out.println(result);
    }
}