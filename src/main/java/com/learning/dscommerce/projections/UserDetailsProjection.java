package com.learning.dscommerce.projections;

public interface UserDetailsProjection {
    
    String getUsername();
    String getPassword();
    Long getRoleId();
    String getAuthority();
}
