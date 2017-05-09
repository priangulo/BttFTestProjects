package PrologDB;

import java.util.LinkedList;
import java.util.List;

public class MyList {

    /**
     * generic utility to reverse a list by creating a new list and not changing
     * the input list
     *
     * @param <T> list element type
     * @param list to reverse
     * @return
     */
    public static <T> List<T> reverse(List<T> list) {
        LinkedList<T> revlist = new LinkedList<>();
        for (T e : list) {
            revlist.add(0, e);
        }
        return revlist;
    }
}
