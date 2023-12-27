package com.livraison.admins.service;
import com.livraison.admins.entity.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserInfoDetails implements UserDetails {

    private String name;
    private String password;
    private String role;
    private String nom;
    private Long livreurId;
    private Long commercantId;


    private List<GrantedAuthority> authorities;


  public UserInfoDetails(UserInfo userInfo, String roles) {
      name = userInfo.getName();
      password = userInfo.getPassword();
      role = userInfo.getRoles();
      nom = userInfo.getName();
      livreurId = userInfo.getLivreurId();
      commercantId = userInfo.getCommercantId();


      authorities = Arrays.stream(roles.split(","))
              .map(SimpleGrantedAuthority::new)
              .collect(Collectors.toList());
  }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    public String getRole() {
        return role;
    }

    public String getNom() {
        return nom;
    }
    public Long getLivreurId() {
        return livreurId;
    }
    public Long getLCommercantId() {
        return commercantId;
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



      /*  public UserInfoDetails(UserInfo userInfo) {
        name = userInfo.getName();
        password = userInfo.getPassword();
        authorities = Arrays.stream(userInfo.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }*/
}
