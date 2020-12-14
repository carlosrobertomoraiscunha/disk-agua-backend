package com.diskagua.api.util;

import com.diskagua.api.models.Role;
import org.springframework.core.convert.converter.Converter;

public class StringToRoleEnumConverter implements Converter<String, Role> {

    @Override
    public Role convert(String source) {
        return Role.getRoleByValue(source);
    }

}
