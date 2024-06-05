package com.surgee.ScandaPay.model;

public enum Role {
    USER(0),
    ADMIN(1);

    private final int value;

        Role(final int newValue) {
            value = newValue;
        }

        public int getValue() { return value; }
}
