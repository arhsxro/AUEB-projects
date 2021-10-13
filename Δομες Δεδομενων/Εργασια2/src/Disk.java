
public class Disk implements Comparable<Disk>{

    private int disk_id;
    public int free_space;
    private List folders = new List();




    public Disk() {
    }



    public Disk(int id) {
        this.disk_id = id;
        if(folders.isEmpty() == false) {

            int size=0;

            while(!(this.folders.isEmpty())) {

                size += folders.peek();

            }

            this.free_space = 1000000 - size;

        }else {

            this.free_space = 1000000;

        }

    }



    public void insertFolder(int folder) {

        this.folders.insertAtFront(folder);

    }




    public int getFreeSpace(){
        int size = 0;

            size += folders.peek();

        return 1000000 - size;
    }



    @Override

    public int compareTo(Disk B) {

        if(this.free_space > B.getFreeSpace()) {

            return 1;

        }else if(this.free_space < B.getFreeSpace()){

            return -1;

        } else {

            return 0;

        }

    }

    public int getId(){
        return this.disk_id;
    }

    public String getFolders(){
        return folders.toString();
    }



}

