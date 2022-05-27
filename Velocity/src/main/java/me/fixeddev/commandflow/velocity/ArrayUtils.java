package me.fixeddev.commandflow.velocity;

public class ArrayUtils {

    public static String[] addElementAtStart(String[] array, String element) {
        String[] newArray = new String[array.length + 1];
        newArray[0] = element;
        System.arraycopy(array, 0, newArray, 1, newArray.length - 1);
        return newArray;
    }
}
