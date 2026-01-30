package io.numberrun.Component;

public class NamedValue<T> implements Component {

    private final T value;
    private final String name;

    public NamedValue(T value) {
        this.value = value;
        this.name = "";
    }

    public NamedValue(String name, T value) {
        this.value = value;
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
