package application.security;

import application.repository.IAppUserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class AppUserServiceImpl implements UserDetailsService {

    private final IAppUserRepository appUserRepository;

    private AppUserServiceImpl(IAppUserRepository appUserRepository) {
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
