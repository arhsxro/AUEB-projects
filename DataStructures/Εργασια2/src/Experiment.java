import java.io.*;
import java.util.Random;

public class Experiment {
    public static void main(String[] args) throws IOException {
    	
    	createFile(100, "exp1");
    	

        createFile(500, "exp2");
        
        
        createFile(1000, "exp3");
        
        //We test greedy and greedy-decreasing for N = 100
        runAlgorithm1("exp1", 100);
    	
        runAlgorithm2("exp1", 100);
        System.out.println("\n");
        
        //For N = 500
        runAlgorithm1("exp2", 500);
        
        runAlgorithm2("exp2", 500);
        System.out.println("\n");

        //For N = 1000
        runAlgorithm1("exp3", 1000);
        
        runAlgorithm2("exp3", 1000);
        

    }

    
    
    private static void createFile(int lines, String name) throws IOException {
    	Random r = new Random();
    	
    	for (int i = 0; i < 10; i++) {
            String fileName = name+"_" + i + ".txt";
            if (new File(fileName).createNewFile()) {
                // file created successfully
            } else {
                // error
            }
            FileWriter fileWriter = new FileWriter(fileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for (int j = 0; j < lines; j++) {

                printWriter.print(r.nextInt(1000000));
                if (j < lines-1)
                    printWriter.println("");


            }
            printWriter.close();
    	}
    }
    

    private static void runAlgorithm1(String Name, int lines) throws NumberFormatException, IOException{
    	int j = 0;
        int sumd = 0;
        
        for (int i = 0; i < 10; i++) {
            int[] temp = new int[lines];

            FileReader fileReader = new FileReader(Name+"_" + i + ".txt");

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                if (j == lines)
                    break;
                temp[j] = Integer.parseInt(line);
                j++;
            }
            bufferedReader.close();


            Disk x = new Disk(0);
            x.insertFolder(temp[0]);

            sumd++;
            MaxPQ y = new MaxPQ(new IntegerComparator());
            y.add(x);


            for (int k = 1; k < temp.length; k++) {
                if (y.peek().getFreeSpace() >= temp[k]) {
                    Disk temp1 = y.getMax();
                    temp1.insertFolder(temp[k]);
                    y.add(temp1);
                } else {
                    Disk z = new Disk(k);
                    z.insertFolder(temp[k]);
                    y.add(z);
                    sumd++;
                }
            }
        }
        System.out.println("Results of the greedy algorithm");
        System.out.println("Discs used for N = "+lines+": " + sumd);
        System.out.println("Average number of discs used for N = "+lines+": " + sumd / 10);
    }
    
    public static void runAlgorithm2(String Name, int lines) throws NumberFormatException, IOException {
    	int j = 0;
        int sumd = 0;
        for (int i = 0; i < 10; i++) {
            int[] temp = new int[lines];

            FileReader fileReader = new FileReader(Name+"_" + i + ".txt");

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                if (j == lines)
                    break;
                temp[j] = Integer.parseInt(line);
                j++;
            }
            bufferedReader.close();
            
            Sort ob = new Sort();
            ob.sort(temp, 0, temp.length-1);

            Disk x = new Disk(0);
            x.insertFolder(temp[0]);

            sumd++;
            MaxPQ y = new MaxPQ(new IntegerComparator());
            y.add(x);


            for (i = 1; i < temp.length; i++) {
                if (y.peek().getFreeSpace() >= temp[i]) {
                    Disk temp1 = y.getMax();
                    temp1.insertFolder(temp[i]);
                    y.add(temp1);
                } else {
                    Disk z = new Disk(i);
                    z.insertFolder(temp[i]);
                    y.add(z);
                    sumd++;
                }
            }


        }
        System.out.println("---------------------------------------------");
        System.out.println("Results of the greedy-decreasing algorithm");
        System.out.println("Discs used for N = "+lines+": " + sumd);
        System.out.println("Average number of discs used for N = "+lines+": " + sumd / 10);
    }
}



