package org.zclient;

public record User(String username, String password) {
    public User {
        if (username.isEmpty() || password.isEmpty()) {
            throw new java.lang.IllegalArgumentException(
                    String.format("Invalid arguments: %s, %s", username, password));
        }
    }
}

