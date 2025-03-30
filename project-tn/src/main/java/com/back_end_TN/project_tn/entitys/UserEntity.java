package com.back_end_TN.project_tn.entitys;

import com.back_end_TN.project_tn.enums.Active;
import com.back_end_TN.project_tn.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
@FilterDef(name = "activeFilter", parameters = @ParamDef(name = "activeStatus", type = String.class))
@Filter(name = "activeFilter", condition = "active = :activeStatus")
public class UserEntity extends BaseEntity<Long> implements UserDetails, Serializable {

     @Column(name = "user_name")
     private String username;

     @Column(name = "password")
     private String password;

     @Column(name = "email", unique = true)
     private String email;

     private Gender gender;

     @Temporal(TemporalType.DATE)
     private Date birthday;

     private Long point;

     @Column(name = "avatar", columnDefinition = "TEXT")
     private String avatar;

     @Column
     @Min(value = 10)
     @Max(value = 10)
     private String phoneNumber;

     @OneToMany(mappedBy = "userId", orphanRemoval = true, fetch = FetchType.EAGER)
     private List<UserRoleEntity> userRoles;

     @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
     private List<AuthProvider> authProviders;

     @OneToMany(mappedBy = "userId")
     private List<Address> addresses;

     @Override
     public Collection<? extends GrantedAuthority> getAuthorities() {
          return userRoles.stream()
                  .map(userRole -> (GrantedAuthority) () -> userRole.getRoleId().getName().name())
                  .collect(Collectors.toList());
     }

     @Override
     public boolean isAccountNonExpired() {
          return true;
     }

     /**
      * Tài khoản chỉ được coi là "không bị khóa" nếu trạng thái active là HOAT_DONG.
      * Với các truy vấn thông thường, Hibernate Filter sẽ tự động lọc theo active = HOAT_DONG.
      * Còn với admin, ta có thể tắt filter để xem tất cả các tài khoản.
      */
     @Override
     public boolean isAccountNonLocked() {
          return super.getActive() == Active.HOAT_DONG;
     }

     @Override
     public boolean isCredentialsNonExpired() {
          return true;
     }

     /**
      * Tài khoản chỉ được kích hoạt nếu active = HOAT_DONG.
      * Với admin, có thể tạm tắt Hibernate Filter để truy vấn tất cả.
      */
     @Override
     public boolean isEnabled() {
          return super.getActive() == Active.HOAT_DONG;
     }
}
