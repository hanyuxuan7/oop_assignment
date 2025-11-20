import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Library<T>{
    private final List<T> items = new ArrayList<>();
    public void add(T item) {
        items.add(item);
    }
    public boolean remove(T item) {
        return items.remove(item);
    }
    public List<T> retrieve() {
        return Collections.unmodifiableList(items);
    }
    public List<T> find(Predicate<T> filter) {
        return items.stream()
                .filter(filter)
                .collect(Collectors.toList());
    }
    public void display() {
        for (T item: items) {
            System.out.println(item);
        }
    }



}