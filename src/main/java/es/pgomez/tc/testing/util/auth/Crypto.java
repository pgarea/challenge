package es.pgomez.tc.testing.util.auth;

import io.quarkus.runtime.util.HashUtil;

public class Crypto {

    private Crypto() {
    }

    public static String hashPassword(String password) {
        return HashUtil.sha1(password.trim());
    }
}
