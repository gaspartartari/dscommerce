package com.learning.dscommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.dscommerce.DTO.UserDTO;
import com.learning.dscommerce.entities.Role;
import com.learning.dscommerce.entities.User;
import com.learning.dscommerce.projections.UserDetailsProjection;
import com.learning.dscommerce.repositories.UserRepository;
import com.learning.dscommerce.utils.CustomUserUtil;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityMapperService entityMapperService;

    @Autowired
    private CustomUserUtil customUserUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> result = userRepository.searchUserAndRolesByEmail(username);
        if(result.size() == 0)
            throw new UsernameNotFoundException("username not found");

        User user = new User();
        user.setEmail(result.get(0).getUsername());
        user.setPassword(result.get(0).getPassword());
        for (UserDetailsProjection x : result){
            user.addRole(new Role(x.getRoleId(), x.getAuthority()));
        }

        return user;
    }

    protected User authenticated(){
        try {
            String username = customUserUtil.getLoggedUser();
            return userRepository.findByEmail(username).get();

        } catch (Exception e) {
            throw new UsernameNotFoundException("username not found");
        }
    }

    @Transactional(readOnly = true)
    public UserDTO getMe(){
        User user = authenticated();
        return entityMapperService.userToUserDto(user);
    }
    
}
