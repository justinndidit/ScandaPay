package com.surgee.ScandaPay.model;

public enum Gender {
    MALE(0),
    FEMALE(1),
    OTHERS(2);

    private final int value;

        Gender(final int newValue) {
            value = newValue;
        }

        public int getValue() { return value; }
}
