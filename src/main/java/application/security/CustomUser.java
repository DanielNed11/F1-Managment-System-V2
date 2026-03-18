package application.security;

import application.domain.AppUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUser extends User {

    @Getter
    @Setter
    private Integer appUserId;

    private CustomUser(String username,
                      String password,
                      Collection<? extends GrantedAuthority> authorities,
                      final int appUserId) {
        super(username, password, authorities);
        this.appUserId = appUserId;
    }

    public static CustomUser buildUser(AppUser appUser) {
        return new CustomUser(
                appUser.getUsername(),
                appUser.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + appUser.getRole().name())),
                appUser.getId()
        );
    }

}
