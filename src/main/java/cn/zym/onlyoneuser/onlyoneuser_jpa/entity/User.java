package cn.zym.onlyoneuser.onlyoneuser_jpa.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName User
 * @Description TODO
 * @Author zhengym
 * @Date 2020/5/6 16:14
 * @Version 1.0
 */
@Entity
@Table(name = "t_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "isAccountNonExpired")
    private boolean isAccountNonExpired;//账户是否没有过期

    @Column(name = "isAccountNonLocked")
    private boolean isAccountNonLocked;//账户是否没有被锁定

    @Column(name = "isCredentialsNonExpired")
    private boolean isCredentialsNonExpired;//密码是否没有过期

    @Column(name = "isEnabled")
    private boolean isEnabled;//账户是否可用

    //@ManyToMany多对多关系
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    private List<Role> roles;//会生成一张关联表

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //返回用户的角色
       List<SimpleGrantedAuthority> authorities = new ArrayList<>();
       for (Role role:roles) {
           authorities.add(new SimpleGrantedAuthority(role.getName()));
       }
       return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        isCredentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object obj) {
        if (this==obj) return true;
        if (obj==null || getClass()!=obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(username,user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
