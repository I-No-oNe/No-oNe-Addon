package net.i_no_am.clickcrystals.addon.utils;

public class ClassUtils {

    public static boolean isClassLoaded(String className) {
        try {
            ClassLoader.getSystemClassLoader().loadClass(className);
            return true;
        } catch (ClassNotFoundException | LinkageError | SecurityException e) {
            return false;
        }
    }
}
