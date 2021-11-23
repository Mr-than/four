import java.util.Iterator;

public class Main{
    public static void main(String[] args) {

       MyArrayList<String> m =new MyArrayList<>();
       m.add("1");
       m.add("2");
       m.add("3");
       m.add("4");
       m.add(0,"0");


        IteratorPlus<String> i =m.iteratorPlus();

        while (i.hasNext()){
            System.out.println(i.next());
        }
    }
}