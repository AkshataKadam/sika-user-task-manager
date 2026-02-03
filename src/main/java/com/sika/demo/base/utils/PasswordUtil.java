package com.sika.demo.base.utils;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class PasswordUtil {
    private static final Argon2 argon2 = Argon2Factory.create();

    public static String hash(String password) {
        int iterations = 3;
        int memory = 65536;
        int parallelism = 1;
        return argon2.hash(iterations, memory, parallelism, password);
    }

    public static boolean verify(String hash, String password) {
        return argon2.verify(hash, password);
    }
}
