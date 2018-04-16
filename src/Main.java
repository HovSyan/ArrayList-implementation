import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    HList<Integer> list1 = new HList<>();
    HList<Integer> list2 = new HList<>();

    for(int i = 0; i < 10; i++) {
      list1.add(i);
      list2.add(i + 1);
    }
    list1.add(1);

    for(int i = 0; i < 11; i++) {
      list1.remove(0);
    }
    System.out.println(list1.isEmpty());
    list1.printObjectState();
  }
}