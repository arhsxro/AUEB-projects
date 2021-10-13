public class Rectangle {
	private int xmin,xmax,ymin,ymax;
	
	public Rectangle() {
		
	}
	
	public Rectangle(int xmin, int xmax, int ymin, int ymax) {
		this.xmin = xmin;
		this.xmax = xmax;
		this.ymin = ymin;
		this.ymax = ymax;
	}
	
    public int xmin() {
        return this.xmin;
    }
    public int ymin() {
        return this.ymin;
    }
    public int xmax() {
        return this.xmax;
    }

    public int ymax(){
        return this.ymax;
    }
    
    public void set_xmin(int xmin) {
    	this.xmin = xmin;
    }
    public void set_xmax(int xmax) {
    	this.xmax = xmax;
    }
    public void set_ymin(int ymin) {
    	this.ymin = ymin;
    }
    public void set_ymax(int ymax) {
    	this.ymax = ymax;
    }
    
    public boolean contains(Point p){
        if(p.x >= this.xmin && p.x <= this.xmax && p.y >= this.ymin && p.y <= this.ymax){
            return true;
        } else {
            return false;
        }

    }
    
    public boolean intersects(Rectangle that) {
    	if(that.xmin <= this.xmax || this.xmin <= that.xmax) {
    		return false;
    	}else {
    		return true;
    	}
    }
    
    public double distanceTo(Point p) {
    	double[] temp = new double[8];

        temp[0] = Math.sqrt((p.y - p.y) * (p.y - p.y) + (this.xmax - p.x) * (this.xmax - p.x));
        temp[1] = Math.sqrt((p.y - p.y) * (p.y - p.y) + (this.xmax - p.x) * (this.xmax - p.x));
        temp[2] = Math.sqrt((this.ymin - p.y) * (this.ymin - p.y) + (p.x - p.x) * (p.x - p.x));
        temp[3] = Math.sqrt((this.ymax - p.y) * (this.ymax - p.y) + (p.x - p.x) * (p.x - p.x));    
        double min = temp[0];
        for (int i = 1; i < 8; i++){
            if(temp[i] < min)
                min = temp[i];
        }

        return min;
    }
    
    public int squareDistanceTo(Point p) {
    	 int[] temp = new int[8];

         temp[0] = ((p.y - p.y) * (p.y - p.y) + (this.xmax - p.x) * (this.xmax - p.x));
         temp[1] = ((p.y - p.y) * (p.y - p.y) + (this.xmax - p.x) * (this.xmax - p.x));
         temp[2] = ((this.ymin - p.y) * (this.ymin - p.y) + (p.x - p.x) * (p.x - p.x));
         temp[3] = ((this.ymax - p.y) * (this.ymax - p.y) + (p.x - p.x) * (p.x - p.x));
         int min = temp[0];
         for (int i = 1; i < 8; i++){
             if(temp[i] < min)
                 min = temp[i];
         }

         return min;
    }
    
   
}
