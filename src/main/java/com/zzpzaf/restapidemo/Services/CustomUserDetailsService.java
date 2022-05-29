package com.zzpzaf.restapidemo.Services;

import com.zzpzaf.restapidemo.Repositories.UsersRepo;
import com.zzpzaf.restapidemo.dataObjects.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UsersRepo userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        User user = userRepository.findByName(s);

        org.springframework.security.core.userdetails.User springUser = null;

        if (user != null) {

            springUser = new org.springframework.security.core.userdetails.User(
                    user.getUSERNAME(),
                    user.getPASSWORD(),
                    user.getAuthorities());
            return springUser;
        } else {
            throw new UsernameNotFoundException(String.format("Username not found"));

        }
    }


}
