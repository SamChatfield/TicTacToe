/**
 * Pair class useful for storing pairs of things
 *
 * @param <A> first object
 * @param <B> second object
 */
public class Pair<A, B> {

    private final A a;
    private final B b;

    /**
     * New tuple with objects a and b
     * @param a object a
     * @param b object b
     */
    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A a() {
        return a;
    }

    public B b() {
        return b;
    }

    public String toString() {
        return "(" + a + "," + b + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (!a.equals(pair.a)) return false;
        return b.equals(pair.b);

    }

    @Override
    public int hashCode() {
        int result = a.hashCode();
        result = 31 * result + b.hashCode();
        return result;
    }
}
