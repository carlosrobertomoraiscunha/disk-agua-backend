package com.diskagua.api.util;

import com.diskagua.api.models.Role;

public class UrlConstants {

    public final static String USER_URL = "/api/v1/usuarios";
    public final static String CUSTOMER_REGISTRATION_URL = USER_URL + "/" + Role.CUSTOMER.getValue();
    public final static String VENDOR_REGISTRATION_URL = USER_URL + "/" + Role.VENDOR.getValue();
    public final static String USER_LOGIN_URL = "/api/v1/usuarios/auth";
}
