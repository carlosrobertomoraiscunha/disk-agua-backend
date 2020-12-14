package com.diskagua.api.models;

import java.util.HashMap;
import java.util.Map;

public enum Role {
    CUSTOMER("cliente"), VENDOR("vendedor");

    private final String value;
    private static final Map<String, Role> roleMap = new HashMap<>();

    static {
        for (Role role : Role.values()) {
            roleMap.put(role.value, role);
        }
    }

    private Role(final String value) {
        this.value = value;
    }

    public static Role getRoleByValue(String value) {
        Role role = roleMap.get(value);

        if (role == null) {
            throw new IllegalArgumentException("Esse cargo de usuário não existe");
        } else {
            return role;
        }
    }

    public String getValue() {
        return value;
    }

}
