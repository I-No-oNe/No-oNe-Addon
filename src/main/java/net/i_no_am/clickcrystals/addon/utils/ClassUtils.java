package net.i_no_am.clickcrystals.addon.utils;

public class ClassUtils {

    public static boolean isClassLoaded(String className) {
        try {
            Class.forName(className, false, ClassUtils.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException | LinkageError | SecurityException e) {
            return false;
        }
    }
}
