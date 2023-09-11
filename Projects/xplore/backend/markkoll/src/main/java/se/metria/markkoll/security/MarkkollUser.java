package se.metria.markkoll.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class MarkkollUser extends User {

    public MarkkollUser(String username,
                        String password,
                        Collection<? extends GrantedAuthority> authorities)
    {
        super(username, password, authorities);
    }
}
