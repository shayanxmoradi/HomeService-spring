package org.example.homeservice.service.user;

import org.example.homeservice.domain.BaseUser;
import org.example.homeservice.repository.user.BaseUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Qualifier("baseUserRepo")
    @Autowired
    private BaseUserRepo userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BaseUser foundedUser = (BaseUser) userRepository.findByEmail(username).get();

        if (foundedUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(foundedUser.getEmail(), foundedUser.getPassword(),
                List.of(new SimpleGrantedAuthority(foundedUser.getUserRole().toString())));
    }
}