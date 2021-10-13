
public class Point {
	public int x;
    public int y;

    public Point() {}
    
    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int x() {

        return this.x;
    }

    public int y(){

        return this.y;
    }
    //Euclidean distance between two points
    public double distanceTo(Point z){

        return Math.sqrt((z.y - this.y) * (z.y - this.y) + (z.x - this.x) * (z.x - this.x));
    }
    
    public int squareDistanceTo(Point z){

        return (z.y - this.y) * (z.y - this.y) + (z.x - this.x) * (z.x - this.x);
    }

    public String toString(){

        String p = ("( " + x + ", "+ y + ")");

        return p;

    }
}
