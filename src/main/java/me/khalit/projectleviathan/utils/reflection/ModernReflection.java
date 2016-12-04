package me.khalit.projectleviathan.utils.reflection;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ModernReflection {

    public static MethodHandle getField(Field field, boolean isGetter) {
        try {
            if (isGetter) {
                return MethodHandles.lookup().unreflectGetter(field);
            }
            return MethodHandles.lookup().unreflectSetter(field);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MethodHandle getField(Class<?> clazz, String name, Class<?> type,
                                        boolean isStatic, boolean isGetter) {
        try {
            if (isStatic && isGetter) {
                return MethodHandles.lookup().findStaticGetter(clazz, name, type);
            }
            else if (isStatic && !isGetter) {
                return MethodHandles.lookup().findStaticSetter(clazz, name, type);
            }
            else if (!isStatic && isGetter) {
                return MethodHandles.lookup().findGetter(clazz, name, type);
            }
            return MethodHandles.lookup().findSetter(clazz, name, type);
        } catch (NoSuchFieldException
                | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MethodHandle getConstructor(Class<?> clazz, Class... params) {
        try {
            MethodType methodType = MethodType.methodType(clazz, params);
            return MethodHandles.lookup().findConstructor(clazz, methodType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MethodHandle getConstructor(Constructor constructor) {
        try {
            return MethodHandles.lookup().unreflectConstructor(constructor);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MethodHandle getMethod(Method method) {
        try {
            return MethodHandles.lookup().unreflect(method);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MethodHandle getMethod(Class<?> clazz, Class<?> type, String name, boolean isStatic, Class... params) {
        try {
            MethodType methodType = MethodType.methodType(type, params);
            if (!isStatic) {
                return MethodHandles.lookup().findVirtual(clazz, name, methodType);
            }
            return MethodHandles.lookup().findStatic(clazz, name, methodType);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static MethodHandle getMethod(Class<?> clazz, Class<?> type, String name, boolean isStatic) {
        try {
            MethodType methodType = MethodType.methodType(type);
            if (!isStatic) {
                return MethodHandles.lookup().findVirtual(clazz, name, methodType);
            }
            return MethodHandles.lookup().findStatic(clazz, name, methodType);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static MethodHandle getMethod(Class<?> clazz, String name, boolean isStatic) {
        try {
            MethodType methodType = MethodType.methodType(void.class);
            if (!isStatic) {
                return MethodHandles.lookup().findVirtual(clazz, name, methodType);
            }
            return MethodHandles.lookup().findStatic(clazz, name, methodType);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static MethodHandle getMethod(Class<?> clazz, String name, boolean isStatic, Class... params) {
        try {
            MethodType methodType = MethodType.methodType(void.class, params);
            if (!isStatic) {
                return MethodHandles.lookup().findVirtual(clazz, name, methodType);
            }
            return MethodHandles.lookup().findStatic(clazz, name, methodType);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static MethodHandle getMethod(Class<?> clazz, String name) {
        try {
            MethodType methodType = MethodType.methodType(void.class);
            return MethodHandles.lookup().findVirtual(clazz, name, methodType);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static MethodHandle getMethod(Class<?> clazz, String name, Class... params) {
        try {
            MethodType methodType = MethodType.methodType(void.class, params);
            return MethodHandles.lookup().findVirtual(clazz, name, methodType);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

}
