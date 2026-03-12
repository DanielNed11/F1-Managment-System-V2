package application.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "app_users")
public class AppUser{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    @Getter
    private String username;

    @Column(nullable = false)
    @Getter
    private String password;
}
