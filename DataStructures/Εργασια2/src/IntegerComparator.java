

import java.util.Comparator;

public class IntegerComparator implements Comparator<Disk>{
    @Override
    public int compare(Disk t1, Disk t2) {

        return  t1.getFreeSpace() -  t2.getFreeSpace();
    }
}
