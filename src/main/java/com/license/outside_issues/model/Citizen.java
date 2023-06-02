package com.license.outside_issues.model;

import com.license.outside_issues.dto.RegisterCitizenDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "citizens")
public class Citizen implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String password;

    @ManyToMany
    @JoinTable(
            name = "citizens_roles",
            joinColumns = @JoinColumn(name = "citizen_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "citizen")
    Set<CitizenReactions> citizenReactions;

    @OneToOne(mappedBy = "citizen")
    private Blacklist blacklist;

    @OneToOne(mappedBy = "citizen")
    private CitizenImage citizenImage;

//    @OneToMany(
//            mappedBy = "citizen",
//            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
//    )
//    private Set<Issue> issues = new HashSet<>();

    public Citizen() {}

    public Citizen(RegisterCitizenDTO registerCitizenDTO) {
        this.firstName = registerCitizenDTO.getFirstName();
        this.lastName = registerCitizenDTO.getLastName();
        this.email = registerCitizenDTO.getEmail();
        this.phoneNumber = registerCitizenDTO.getPhoneNumber();
        this.password = registerCitizenDTO.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role ->
                authorities.add(new SimpleGrantedAuthority(role.getName()))
        );
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
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

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<CitizenReactions> getCitizenReactions() {
        return citizenReactions;
    }

    public void setCitizenReactions(Set<CitizenReactions> citizenReactions) {
        this.citizenReactions = citizenReactions;
    }

    public Blacklist getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(Blacklist blacklist) {
        this.blacklist = blacklist;
    }

    public CitizenImage getCitizenImage() {
        return citizenImage;
    }

    public void setCitizenImage(CitizenImage citizenImage) {
        this.citizenImage = citizenImage;
    }

    @Override
    public String toString() {
        return "Citizen{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", citizenReactions=" + citizenReactions +
                ", blacklist=" + blacklist +
                ", citizenImage=" + citizenImage +
                '}';
    }
}
