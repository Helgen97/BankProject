package com.task.bank.entities;

public enum Roles {
    ADMIN, USER, MODERATOR;


    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
