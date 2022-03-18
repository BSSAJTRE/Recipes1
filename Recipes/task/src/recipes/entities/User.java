package recipes.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "password")
    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 8)
    private String password;

    @Column(name = "email")
    @NotNull
    @NotEmpty
    @NotBlank
    @Pattern(regexp = ".+@.+\\..+")
    @Size(min = 8)
    private String email;

    @Column(name = "role")
    private String role;
}
