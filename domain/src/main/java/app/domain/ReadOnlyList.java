package app.domain;

import java.util.Collections;
import java.util.List;

public class ReadOnlyList<T> {
    private final List<T> wrappedList;

    public ReadOnlyList(List<T> originalList) {
        this.wrappedList = Collections.unmodifiableList(originalList);
    }

    public T getElementAt(int index) {
        return wrappedList.get(index);
    }

    public int size() {
        return wrappedList.size();
    }
}
