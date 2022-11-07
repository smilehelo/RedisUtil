package cn.smilehelo.redisUtil.tuple;

import java.util.Objects;

public class Tuple3<T0, T1, T2> extends Tuple {

    private static final long serialVersionUID = -8357898888608677841L;

    public T0 field0;
    public T1 field1;
    public T2 field2;

    public Tuple3() {
    }

    public Tuple3(T0 field0, T1 field1, T2 field2) {
        this.field0 = field0;
        this.field1 = field1;
        this.field2 = field2;
    }

    public static <T0, T1, T2> Tuple3<T0, T1, T2> of(T0 field0, T1 field1, T2 field2) {
        return new Tuple3<>(field0, field1, field2);
    }

    /**
     * Creates a string representation of the tuple in the form
     * (field0, field1, field2),
     * where the individual fields are the value returned by calling {@link Object#toString} on that field.
     *
     * @return The string representation of the tuple.
     */
    @Override
    public String toString() {
        return "(" + arrayAwareToString(this.field0)
                + "," + arrayAwareToString(this.field1)
                + "," + arrayAwareToString(this.field2)
                + ")";
    }

    /**
     * Deep equality for tuples by calling equals() on the tuple members.
     *
     * @param o the object checked for equality
     * @return true if this is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tuple3)) {
            return false;
        }
        @SuppressWarnings("rawtypes")
        Tuple3 tuple = (Tuple3) o;
        if (!Objects.equals(field0, tuple.field0)) {
            return false;
        }
        if (!Objects.equals(field1, tuple.field1)) {
            return false;
        }
        return Objects.equals(field2, tuple.field2);
    }

    @Override
    public int hashCode() {
        int result = field0 != null ? field0.hashCode() : 0;
        result = 31 * result + (field1 != null ? field1.hashCode() : 0);
        result = 31 * result + (field2 != null ? field2.hashCode() : 0);
        return result;
    }

    /**
     * Shallow tuple copy.
     *
     * @return A new Tuple with the same fields as this.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Tuple3<T0, T1, T2> copy() {
        return new Tuple3<>(this.field0, this.field1, this.field2);
    }
}
