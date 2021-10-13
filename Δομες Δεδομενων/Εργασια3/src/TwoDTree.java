
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Scanner;

public class TwoDTree {
	
	
	private class Treenode{
		Point item; Treenode l, r;
		Treenode(Point x){
			item = x;
		}
		void setRight(Treenode right){
			r = right;
		}
		void setLeft(Treenode left) {
			l = left;
		}
	};
	
    private Treenode head;
    private int size;
	private static Scanner input;

    public TwoDTree(){
    }

    public boolean isEmpty(){
        return head == null;
    }

    public int size(){
        return size;
    }

    public void insert(Point p){
    	if(this.head == null) {
    		this.head = new Treenode(p);
    	}
    	
    	Treenode current = this.head;
    	int var = p.x;
    	
    	while(true) {
	    	if(current.item.x()== p.x) { 
	    		if(current.item.y()== p.y) { //node is already in the tree
	    			System.out.println("Error: Node already exists.");
	    			return;  
	    		}
	    	}
	    	
	    	if(var == p.x) {
	    		if(current.item.x < var) {
	    			if(current.r != null) {
	    				current = current.r;
	    			}else {
	    				current.setRight(new Treenode(p));
	    				size++;
	    				return;
	    			}
	    		}else {
	    			if(current.l != null) {
	    				current = current.l;
	    			}else {
	    				current.setLeft(new Treenode(p));
	    				size++;
	    				return;
	    			}
	    		}
	    		
	    		var = p.y;
	    	}
	    	
	    	if(var == p.y) {
	    		if(current.item.x < var) {
	    			if(current.r != null) {
	    				current = current.r;
	    			}else {
	    				current.setRight(new Treenode(p));
	    				size++;
	    				return;
	    			}
	    		}else {
	    			if(current.l != null) {
	    				current = current.l;
	    			}else {
	    				current.setLeft(new Treenode(p));
	    				size++;
	    				return;
	    			}
	    		}
	    		
	    		var = p.x;
	    	}
	    	
    	}
    	
    }

    public boolean search(Point p){
    	Treenode current = this.head;
    	
    	while(true) {
    		if(current == null) {
    			return false;
    		}
    		
    		if(current.item.x()== p.x) { 
	    		if(current.item.y() ==  p.y) {
	    			return true;  
	    		}
	    	}
    		
    		//We know that left subtrees have x<x and right subtrees have y>y
    		if(current.item.x < p.x) {
    			current = current.l;
    		}else {
    			current = current.r;
    		}
    	}
    }
    
    public List rangeSearch(Rectangle rect){
    	List range_nodes = new List();
    	
    	//initialize root's rectangle
    	Treenode root = this.head;
    	Rectangle rect_root = new Rectangle(0, 100, 0, 100);
    	int count=0; 
    	range(range_nodes, root, rect_root,  rect, count); 	   	    	
    	
    	return range_nodes;
    }
    
    private void range(List range, Treenode parent, Rectangle parent_rect, Rectangle given,int count){
    	if(parent == null) {return;}
    	
    	if(given.contains(parent.item)) {
    		range.insertAtBack(parent.item);
    	}
    	
    	if(!(count%2==0)) { //we are working with x
    		count++;
    		//Determine left child
    		Treenode left = parent.l;
    		Rectangle left_rect = new Rectangle(0, parent.item.x(), parent_rect.ymin(), parent_rect.ymax()); 
    		if(left_rect.intersects(given)) {
    			range(range, left, left_rect, given, count);
    		}else {return;}
    		
    		//Determine right child
    		Treenode right = parent.r;
    		Rectangle right_rect = new Rectangle(parent.item.x(), 100, parent_rect.ymin(), parent_rect.ymax());
    		if(right_rect.intersects(given)) {
    			range(range, right, right_rect, given, count);
    		}else {return;}
    		
    		
    	}else { //we are working with y
    		count++;
    		//Determine left child
    		Treenode left = parent.l;
    		Rectangle left_rect = new Rectangle(parent_rect.xmin(), parent_rect.xmax(), 0, parent.item.y());   
    		if(left_rect.intersects(given)) {
    			range(range, left, left_rect, given, count);
    		}else {return;}
    		
    		//Determine right child
    		Treenode right = parent.r;
    		Rectangle right_rect = new Rectangle(parent_rect.xmin(), parent_rect.xmax(), parent.item.y(), 100);
    		if(right_rect.intersects(given)) {
    			range(range, right, right_rect, given, count);
    		}else {return;}
    	} 	
    	
    }
      
    
    public Point nearestNeighbor(Point p){
    	int count = 0;
    	
    	Treenode current = this.head;
    	Rectangle current_rect = new Rectangle(0, 100, 0, 100);
    	//To start the min p is the root and min distance is the one p has from the root
    	Point min_p = this.head.item;
    	double min_distance = p.distanceTo(min_p);
    	
    	while(true) {
    		if(current == null) {
    			return min_p;
    		}
    		double current_distance = p.distanceTo(current.item);
    		//If the distance of the current node is smaller that the min distance, we update the variable
    		if(current_distance < min_distance) {
    			min_distance = current_distance;
    			min_p = current.item;
    		}
    		//Check whether to go left or right
    		if(p.distanceTo(current.l.item) < p.distanceTo(current.r.item)) {
    			current = current.l;
    			current_rect = findLeftRect(current, current_rect, count);
    			if(current_rect.distanceTo(p) > min_distance) {
    				return min_p;
    			}
    		}else {
    			current = current.r;
    			current_rect = findRightRect(current, current_rect, count);
    			if(current_rect.distanceTo(p) > min_distance) {
    				return min_p;
    			}
    		}
    		count++;
    	}
    	
    	
    	
    	
    }
     public Rectangle findLeftRect(Treenode parent, Rectangle parent_rect,int count) {
    	if(!(count%2==0)) {
    		//Treenode left = parent.l;
    		Rectangle left_rect = new Rectangle(0, parent.item.x(), parent_rect.ymin(), parent_rect.ymax()); 
    		return left_rect;
    	}
    	else {
    		//Treenode left = parent.r;
    		Rectangle left_rect = new Rectangle(parent_rect.xmin(), parent_rect.xmax(), 0, parent.item.y()); 
    		return left_rect;
    	}
    
    }
     
     public Rectangle findRightRect(Treenode parent, Rectangle parent_rect,int count) {
    	 if(!(count%2==0)) {
     		//Treenode right = parent.r;
     		Rectangle right_rect = new Rectangle(parent.item.x(),100 , parent_rect.ymin(), parent_rect.ymax()); 
     		return right_rect;
     	}
     	else {
     		//Treenode left = parent.r;
     		Rectangle left_rect = new Rectangle(parent_rect.xmin(), parent_rect.xmax(), parent.item.y(), 100 ); 
     		return left_rect;
     	}
     }
     
    public static void main(String args[]) throws IOException
    { 
    	String fileName = args[0];
        int rows;
        TwoDTree ob = new TwoDTree();


        BufferedReader br;

        br = new BufferedReader(new FileReader(fileName));

        String line = br.readLine();
        String first = line;
        rows = Integer.parseInt(first);

        Integer[] array =  new Integer[rows*2];


        line = br.readLine();
        int k = 0;
        while (line != null) {
            //System.out.println(line);

            if (line != null) {
                String[] l = line.split("\\s+");
                for (int i = 0; i < 2; i++) {
                    array[k] = Integer.parseInt(l[i]);
                    if(array[k] > 100){
                        System.out.println("Error,number must be between 0 and 100.");
                        return;
                    }
                    k++;
                }
            }
            line = br.readLine();

        }
        //print array

        for (int i = 0; i < rows * 2; i++) {
            System.out.println(array[i]);

        }

        br.close();
        for(int i = 0; i<rows * 2; i+=2){
            Point temp = new Point(array[i],array[i+1]);
            ob.insert(temp);
        }

        input = new Scanner(System.in);
        int number;
        System.out.println("Please enter a number between 1-5");


        while(true){
        	System.out.println("---------------------------------------------------");
            System.out.println("1. Compute the size of the tree");
            System.out.println("2. Insert a new point");
            System.out.println("3. Search if a given point exists in the tree");
            System.out.println("4. Provide a query rectangle");
            System.out.println("5. Provide a query point");
            System.out.println("Please enter e number between 1-5");
            number = input.nextInt();
            if(number == 1){
                System.out.println("The size of the tree is: "+ob.size());
            } else if (number == 2) {
                System.out.println("Please give the coordinates for the point(x and y");
                int x = input.nextInt();
                int y = input.nextInt();
                Point p = new Point(x,y);
                ob.insert(p);
            }else if(number == 3){
                System.out.println("Please give the coordinates for the point(x and y) ");
                int x = input.nextInt();
                int y = input.nextInt();
                Point p = new Point(x,y);
                if(ob.search(p)==true) {
                	System.out.println("Point exists in the tree!");
                }else {
                	System.out.println("Point does not exist in the tree.");
                }
            }else if(number == 4){
                System.out.println("Please give the coordinates for the rectangle(xmin,xmax,ymin,ymax)");
                int number1;
                int xmin,xmax,ymin,ymax;
                xmin = input.nextInt();
                xmax = input.nextInt();
                ymin = input.nextInt();
                ymax = input.nextInt();
                Rectangle r = new Rectangle(xmin,xmax,ymin,ymax);
                ob.rangeSearch(r);
            }else if(number == 5){
                System.out.println("Please give the coordinates for the point(x and y)");
                int x = input.nextInt();
                int y = input.nextInt();
                Point p = new Point(x,y);
                Point p1;
                p1 = ob.nearestNeighbor(p);
                System.out.println("The closest point has these coordinates: x = "+ p1.x + " y = "+ p1.y);
                System.out.println("And the following distance from the point you gave : " + Math.sqrt((p1.y - y) * (p1.y - y) + (p1.x - x) * (p1.x - x)));

            }

        }
    }
 
    
}