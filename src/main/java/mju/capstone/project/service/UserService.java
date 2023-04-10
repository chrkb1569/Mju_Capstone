package mju.capstone.project.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
    void registerUser(String name, String username, String email, String password);
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
