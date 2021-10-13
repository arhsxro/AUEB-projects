import java.io.IOException;
import java.lang.String;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;

public class Thiseas {
    private static boolean[][] visited;
    


    public static void main(String[] args) throws IOException {


        String fileName = args[0];
        int rows = 0;
        int columns = 0;
        int entry_row = 0;
        int entry_column = 0;

        String[][] maze;
        
        BufferedReader br;

        br = new BufferedReader(new FileReader(fileName));

        String line = br.readLine();
        String[] first = line.split("\\s+");
        
        rows = Integer.parseInt(first[0]);
        columns = Integer.parseInt(first[1]);
        //System.out.println(rows);
        //System.out.println(columns);

        String line2 = br.readLine();
        String[] second = line2.split("\\s+");
        entry_row = Integer.parseInt(second[0]);
        entry_column = Integer.parseInt(second[1]);
        //System.out.println(entry_row);
        //System.out.println(entry_column);

        maze = new String[rows][columns];
        int roww = 0;
        line = br.readLine();
        while (line != null) {
            //System.out.println(line);

            if (line != null) {
                String[] l = line.split("\\s+");
                for (int i = 0; i < columns; i++) {
                    maze[roww][i] =(l[i]);
                }
            }
            line = br.readLine();
            roww++;
        }
 
        //print array
        /**
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(maze[i][j]);
            }
            System.out.println();
        }**/

        maze[entry_row][entry_column] = "0";

        br.close();

        visited = new boolean[rows][columns];
            

        StringStackImpl s = new StringStackImpl();
        s.push(maze[entry_row][entry_column]);

        int x1,x2,y1,y2;
        x1 = 0;
        x2 = maze.length - 1;
        y1 = 0;
        y2 = maze[0].length;
        int temp1 = entry_row;
        int temp2 = entry_column;
        int temprow = 0;
        int tempcolumn = 0;
        boolean in = false;

        setVisited(true);

        Random rand = new Random();
        // 0 = up, 1 = left, 2 = right, 3 = down.
        int hint = 0;
        int hint1 = 0;

        visited[entry_row][entry_column] = false;
        while(!s.isEmpty()) {
            int r = rand.nextInt(4);
            int p;



            if (hint >= 1 && maze[entry_row][entry_column].equals("0") && in == true) {
                hint++;

            }



            if (r == 0 && entry_row - 1 >= 0) {
                p = Integer.parseInt(maze[entry_row - 1][entry_column]);
                if (p == 0 && visited[entry_row - 1][entry_column]) {
                    if ((maze[entry_row + 1][entry_column].equals("0") && visited[entry_row + 1][entry_column]) || (maze[entry_row][entry_column - 1].equals("0") && visited[entry_row][entry_column - 1]) || (maze[entry_row][entry_column + 1].equals("0") && visited[entry_row][entry_column + 1])) {
                        hint = 0;
                        ++hint;
                        temprow = entry_row;
                        tempcolumn = entry_column;
                    }
                    in = true;
                    s.push(maze[entry_row - 1][entry_column]);
                    visited[entry_row - 1][entry_column] = false;

                    entry_row -= 1;
                }
            } else if (r == 1 && entry_column - 1 >= 0) {
                p = Integer.parseInt(maze[entry_row][entry_column - 1]);
                if (p == 0 && visited[entry_row][entry_column - 1]) {
                    if ((maze[entry_row + 1][entry_column].equals("0") && visited[entry_row + 1][entry_column]) || (maze[entry_row - 1][entry_column].equals("0") && visited[entry_row - 1][entry_column]) || (maze[entry_row][entry_column + 1].equals("0") && visited[entry_row][entry_column + 1])) {
                        hint = 0;
                        ++hint;
                        temprow = entry_row;
                        tempcolumn = entry_column;
                    }
                    in = true;
                    s.push(maze[entry_row][entry_column - 1]);
                    visited[entry_row][entry_column - 1] = false;

                    entry_column -= 1;
                }
            } else if (r == 2 && entry_column + 1 <= maze[0].length - 1) {
                p = Integer.parseInt(maze[entry_row][entry_column + 1]);
                if (p == 0 && visited[entry_row][entry_column + 1]) {
                    if ((maze[entry_row + 1][entry_column].equals("0") && visited[entry_row + 1][entry_column]) || (maze[entry_row - 1][entry_column].equals("0") && visited[entry_row - 1][entry_column]) || (maze[entry_row][entry_column - 1].equals("0") && visited[entry_row][entry_column - 1])) {
                        hint = 0;
                        ++hint;
                        temprow = entry_row;
                        tempcolumn = entry_column;
                    }
                    in = true;
                    s.push(maze[entry_row][entry_column + 1]);
                    visited[entry_row][entry_column + 1] = false;

                    entry_column += 1;
                }

            } else if (r == 3 && entry_row + 1 <= maze.length - 1) {
                p = Integer.parseInt(maze[entry_row + 1][entry_column]);
                if (p == 0 && visited[entry_row + 1][entry_column]) {
                    if (entry_row - 1 >= 0) {
                        if (maze[entry_row - 1][entry_column].equals("0") && visited[entry_row - 1][entry_column]) {
                            ++hint1;
                            if (hint1 > 1) {
                                hint = 0;
                                ++hint;
                                temprow = entry_row;
                                tempcolumn = entry_column;
                            } else if (hint1 == 1) {
                                hint = 0;
                                ++hint;
                                temprow = entry_row;
                                tempcolumn = entry_column;
                            }
                        }

                    }
                    if (entry_column + 1 <= maze[0].length - 1) {
                        if (maze[entry_row][entry_column + 1].equals("0") && visited[entry_row][entry_column + 1]) {
                            ++hint1;
                            if (hint1 > 1) {
                                hint = 0;
                                ++hint;
                                temprow = entry_row;
                                tempcolumn = entry_column;
                            } else if (hint1 == 1) {
                                hint = 0;
                                ++hint;
                                temprow = entry_row;
                                tempcolumn = entry_column;
                            }
                        }
                    }
                    if (entry_column - 1 >= 0) {
                        if (maze[entry_row][entry_column - 1].equals("0") && visited[entry_row][entry_column - 1]) {
                            ++hint1;
                            if (hint1 > 1) {
                                hint = 0;
                                ++hint;
                                temprow = entry_row;
                                tempcolumn = entry_column;
                            } else if (hint1 == 1) {
                                hint = 0;
                                ++hint;
                                temprow = entry_row;
                                tempcolumn = entry_column;
                            }
                        }
                    }



                    hint1 = 0;
                    in = true;

                    s.push(maze[entry_row + 1][entry_column]);
                    visited[entry_row + 1][entry_column] = false;
                    entry_row += 1;


                }

            }



            if ((entry_row == x1 && entry_row != temp1) || (entry_row == x2 && entry_row != temp1) || (entry_column == y1 && entry_column != temp2) || (entry_column == y2 && entry_column != temp2)) {
                System.out.println("Path Found! At (" + entry_row + "," + entry_column+"). ");
                break;
            }

            /**
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    System.out.print(visited[i][j]);
                }
                System.out.println();
            }**/


            if((((visited[entry_row + 1][entry_column] == false) || (maze[entry_row + 1][entry_column].equals("1"))) && ((visited[entry_row][entry_column + 1] == false) || (maze[entry_row][entry_column + 1].equals("1"))) && ((visited[entry_row][entry_column - 1] == false) || (maze[entry_row][entry_column - 1].equals("1"))) && ((visited[entry_row - 1][entry_column] == false) || (maze[entry_row - 1][entry_column].equals("1"))))){
                entry_row = temprow;
                entry_column = tempcolumn;
                for(int i = 0; i < hint; i++) {
                    s.pop();
                }
                hint = 0;


            }

            in = false;



        }
    }

    private static void setVisited(boolean b)
    {
        for (int i = 0; i < visited.length; i++)
        {
            for (int j = 0; j < visited[i].length; j++)
            {
                visited[i][j] = b;
            }
        }

    }
}

