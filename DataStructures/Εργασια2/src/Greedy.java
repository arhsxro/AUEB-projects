import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Greedy {


    private static Scanner scanner;

	public static void main(String[] args) throws IOException {
        
        String fileName = args[0];
        int[] folders = readInputData(fileName);

        Greedy1(folders);

    }
    
    public static int[] readInputData(String f) throws FileNotFoundException {
    	ArrayList<Integer> temp = new ArrayList<Integer>();
    	scanner = new Scanner(new File(f));
    	
    	while(scanner.hasNextInt())
        {
            temp.add(scanner.nextInt());
        }
        
    	int [] folders = new int [temp.size()];
    	
        for(int i = 0; i < folders.length; i++) {
            folders[i] = temp.get(i);
            //System.out.println(folders[i]);
            if(folders[i] < 0 || folders[i] > 1000000){
                System.out.println("Error,the programm will quit");
                System.exit(0);
            }

        }
        return folders;
    	
    }
    
    public static void Greedy1(int[] folders) {
    	int sumd = 0; //summary of the disks that we will use.
        int sumf = 0; //summary of the folders.
        
    	//Calculating the sum of all folders
        int i =0;
        for(i = 0; i < folders.length; i++)
            sumf+= folders[i];
   
        Disk x = new Disk(0);
        x.insertFolder(folders[0]);

        sumd++;
        MaxPQ y = new MaxPQ( new IntegerComparator());
        y.add(x);

        for(i = 1; i < folders.length; i++){
            if(y.peek().getFreeSpace() >= folders[i]){
                Disk temp1 = y.getMax();
                temp1.insertFolder(folders[i]);
                y.add(temp1);
            } else {
                Disk z = new Disk(i);
                z.insertFolder(folders[i]);
                y.add(z);
                sumd++;
            }
        }

        System.out.println("Sum of all folders = " + sumf + " TB");
        System.out.println("Total number of disks used = " + sumd);


        for(i=0; i < sumd; i++) {
            Disk k = y.getMax();
            System.out.println("id " + k.getId() + " " + k.getFreeSpace() + " : " + k.getFolders());
        }
    	
    	
    }
}