package ourtine.domain;


import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ourtine.auth.info.OAuth2UserInfo;
import ourtine.domain.enums.UserStatus;

import java.util.ArrayList;
import java.util.Collection;


@Getter
@ToString
public class UserDetailsImpl implements UserDetails {
    private final User user;
    private final OAuth2UserInfo oAuth2UserInfo;

    public UserDetailsImpl(User user, OAuth2UserInfo oAuth2UserInfo){
        this.user = user;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    // 비밀번호 사용하지 않을 예정
    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return user.getId().toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return ! ( user.getUserStatus() == (UserStatus.NORMAL_INACTIVE) || user.getUserStatus() == UserStatus.BLOCKED_INACTIVE );
    }

    @Override
    public boolean isAccountNonLocked() {
        return ( user.getUserStatus() == UserStatus.NORMAL_ACTIVE || user.getUserStatus() == UserStatus.SIGNUP_PROGRESS );
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return user.getUserStatus() == UserStatus.NORMAL_ACTIVE;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add((GrantedAuthority) () -> user.getUserRole().toString());
        return collect;
    }
}
