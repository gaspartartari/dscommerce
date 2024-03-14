package com.learning.dscommerce.test;

import java.util.ArrayList;
import java.util.List;

import com.learning.dscommerce.projections.UserDetailsProjection;

public class UserDetailsFactory{

    public static List<UserDetailsProjection> createCustomClientUser(String username){

        List<UserDetailsProjection> result = new ArrayList<>();
        result.add(new UserDetailImpl(username, "12345", 1L, "ROLE_CLIENT"));
        return result;
    }

    public static List<UserDetailsProjection> createCustomUserAdmin(String username){

        List<UserDetailsProjection> result = new ArrayList<>();
        result.add(new UserDetailImpl(username, "12345", 1L, "ROLE_ADMIN"));
        return result;
    }

    public static List<UserDetailsProjection> createCustomUserAdminClient(String username){

        List<UserDetailsProjection> result = new ArrayList<>();
        result.add(new UserDetailImpl(username, "12345", 1L, "ROLE_CLIENT"));
        result.add(new UserDetailImpl(username, "12345", 1L, "ROLE_ADMIN"));
        return result;
    }

}

class UserDetailImpl implements UserDetailsProjection{

    private String username;
    private String password;
    private Long roleId;
    private String authority;

    public UserDetailImpl(){

    }

    public UserDetailImpl(String username, String password, Long roleId, String authority) {
        this.username = username;
        this.password = password;
        this.roleId = roleId;
        this.authority = authority;
    }


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Long getRoleId() {
        return roleId;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

}