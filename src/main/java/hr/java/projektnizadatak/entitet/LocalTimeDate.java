package hr.java.projektnizadatak.entitet;

public class LocalTimeDate<T, U> {
    private final T first;
    private final U second;

    public LocalTimeDate(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getLocalDate() {
        return first;
    }

    public U getLocalTime() {
        return second;
    }




}