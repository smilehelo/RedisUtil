package cn.smilehelo.redisUtil.tuple;

import java.io.Serializable;
import java.util.Arrays;


public abstract class Tuple implements Serializable {

    private static final long serialVersionUID = 6651491547853091142L;

    /**
     * This method calls {@link Object#toString()} on the given object, unless the
     * object is an array. In that case, it will use the {@link #arrayToString(Object)}
     * method to create a string representation of the array that includes all contained
     * elements.
     *
     * @param o The object for which to create the string representation.
     * @return The string representation of the object.
     */
    protected static String arrayAwareToString(Object o) {
        if (o == null) {
            return "null";
        }
        if (o.getClass().isArray()) {
            return arrayToString(o);
        }

        return o.toString();
    }

    /**
     * Returns a string representation of the given array. This method takes an Object
     * to allow also all types of primitive type arrays.
     *
     * @param array The array to create a string representation for.
     * @return The string representation of the array.
     *
     * @throws IllegalArgumentException If the given object is no array.
     */
    protected static String arrayToString(Object array) {
        if (array == null) {
            throw new NullPointerException();
        }

        if (array instanceof int[]) {
            return Arrays.toString((int[]) array);
        }
        if (array instanceof long[]) {
            return Arrays.toString((long[]) array);
        }
        if (array instanceof Object[]) {
            return Arrays.toString((Object[]) array);
        }
        if (array instanceof byte[]) {
            return Arrays.toString((byte[]) array);
        }
        if (array instanceof double[]) {
            return Arrays.toString((double[]) array);
        }
        if (array instanceof float[]) {
            return Arrays.toString((float[]) array);
        }
        if (array instanceof boolean[]) {
            return Arrays.toString((boolean[]) array);
        }
        if (array instanceof char[]) {
            return Arrays.toString((char[]) array);
        }
        if (array instanceof short[]) {
            return Arrays.toString((short[]) array);
        }

        if (array.getClass().isArray()) {
            return "<unknown array type>";
        } else {
            throw new IllegalArgumentException("The given argument is no array.");
        }
    }

    /**
     * Shallow tuple copy.
     *
     * @return A new Tuple with the same fields as this.
     */
    public abstract <T extends Tuple> T copy();

}
