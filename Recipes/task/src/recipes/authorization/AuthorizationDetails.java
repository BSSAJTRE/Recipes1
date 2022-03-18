package recipes.authorization;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import recipes.entities.User;

import java.util.Collection;
import java.util.List;

public class AuthorizationDetails implements UserDetails {
    @Getter
    private final String password;
    @Getter
    private final String username;
    private final List<GrantedAuthority> rolesAndAuthorities;

    public AuthorizationDetails(User user) {
        password = user.getPassword();
        username = user.getEmail();
        rolesAndAuthorities = List.of(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return rolesAndAuthorities;
    }

    // 4 remaining methods that just return true
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
