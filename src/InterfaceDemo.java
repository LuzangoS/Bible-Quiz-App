interface Shape {
    //methods are 
    public double findArea(double radius);
    public double findVolume(double radius, double height);
    public double findVolume(double dimensions);
    public double findPerimeter(double dimensions);
    
}
//a class can extend another class as well as implement an interface
class Circle implements Shape{
    @Override
    public double findArea(double radius){
        return Math.PI*Math.pow(radius, 2);
    }
    @Override
    public double findVolume(double radius, double height){
        return 0;
    }
    public double findVolume(double dimensions){
        return 0;
    }
    public double findPerimeter(double dimensions){
        return Math.PI*(2*dimensions);
    }
}
interface square extends Circle{

}
class Shape{
    static class Cylinder{
        double radius,height;
        public Cylinder(double radius, double height){
            this.radius=radius; this.height=height;
        }
        public String toString(){
           return "The cylinder has "+radius+" "+height;
        }
    }
}
public interface InterfaceDemo {
    //creating an object of the inner class
    //first create an object of the outer class
    Shape hello=new Shape();

    /*to create the object of the inner class now you create outerclassobject.name of the inner class then 
    when wrting new write outerobjectname then .new*/
    hello.Cylinder cyl=hello.new Cylinder();
   
    
}