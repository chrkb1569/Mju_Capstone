package mju.capstone.project.config.jwt;

import lombok.RequiredArgsConstructor;
import mju.capstone.project.domain.user.User;
import mju.capstone.project.exception.user.UserNotFoundException;
import mju.capstone.project.repository.user.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username)
                .map(this::createUserDetailsByUsername)
                .orElseThrow(UserNotFoundException::new);
    }

    public UserDetails createUserDetailsByUsername(User user) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getAuthority().toString());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(authority));
    }
}
