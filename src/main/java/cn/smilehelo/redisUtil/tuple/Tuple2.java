package cn.smilehelo.redisUtil.tuple;

import java.util.Objects;


public class Tuple2<T0, T1> extends Tuple {

    private static final long serialVersionUID = -7612359054365408316L;

    public T0 field0;
    public T1 field1;

    public Tuple2() {
    }

    public Tuple2(T0 field0, T1 field1) {
        this.field0 = field0;
        this.field1 = field1;
    }

    public static <T0, T1> Tuple2<T0, T1> of(T0 field0, T1 field1) {
        return new Tuple2<>(field0, field1);
    }

    public T0 getField0() {
        return field0;
    }

    public T1 getField1() {
        return field1;
    }

    @Override
    public String toString() {
        return "(" + arrayAwareToString(this.field0)
                + "," + arrayAwareToString(this.field1)
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
        if (!(o instanceof Tuple2)) {
            return false;
        }
        @SuppressWarnings("rawtypes")
        Tuple2 tuple = (Tuple2) o;
        if (!Objects.equals(field0, tuple.field0)) {
            return false;
        }
        return Objects.equals(field1, tuple.field1);
    }

    @Override
    public int hashCode() {
        int result = field0 != null ? field0.hashCode() : 0;
        result = 31 * result + (field1 != null ? field1.hashCode() : 0);
        return result;
    }

    /**
     * Shallow tuple copy.
     *
     * @return A new Tuple with the same fields as this.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Tuple2<T0, T1> copy() {
        return new Tuple2<>(this.field0, this.field1);
    }
}
