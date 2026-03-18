package application.security;

import application.domain.AppUser;
import application.repository.IAppUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppUserServiceImpl implements UserDetailsService {

    private final IAppUserRepository appUserRepository;

    public AppUserServiceImpl(IAppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return appUserRepository
                .findByUsername(username)
                .map(CustomUser::buildUser)
                .orElseThrow(() -> new UsernameNotFoundException(username));

    }
}
