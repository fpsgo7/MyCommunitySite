package park.mycommunitysite.config.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import park.mycommunitysite.domain.Member;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private Member member;

    public CustomUserDetails(Member member){
        this.member = member;
    }

    public Member getMember(){
        return member;
    }

    /**
     * Member 객체의 권한을 저장하는 String 값에서
     * 권한들을 추출하여 리스트에 담는다.
     * 권환 확인이 필요한 문장이 실행될 때마다 실행된다.
     * @return 유저 권한 리스트
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        member.getRoleList().forEach(r->{
            //System.out.println(r);
            authorities.add(()->r);
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

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
