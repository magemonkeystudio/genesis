package studio.magemonkey.genesis.misc;

import java.lang.reflect.Method;

public class EnumUT {
    @SuppressWarnings("unchecked")
    public static <T> T[] values(Class<T> clazz) {
        try {
            Method method = clazz.getMethod("values");
            return (T[]) method.invoke(null);
        } catch (Exception e) {
            return (T[]) new Object[0];
        }
    }

    public static String name(Object obj) {
        // Attempt to find and call the name method
        try {
            Method method = obj.getClass().getMethod("name");
            return (String) method.invoke(obj);
        } catch (Exception e) {
            // try the super class if applicable
            try {
                Method method = obj.getClass().getSuperclass().getMethod("name");
                return (String) method.invoke(obj);
            } catch (Exception e2) {
                return null;
            }
        }
    }
}
