package recipes.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "recipe")
@JsonIgnoreProperties({"id", "user"})
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "name")
    @NotNull
    @NotEmpty
    @NotBlank
    private String name;

    @Column(name = "description")
    @NotNull
    @NotEmpty
    @NotBlank
    private String description;

    @Column(name = "category")
    @NotNull
    @NotEmpty
    @NotBlank
    private String category;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "ingredients")
    @NotNull
    @Size(min = 1)
    private String[] ingredients;

    @Column(name = "directions")
    @NotNull
    @Size(min = 1)
    private String[] directions;
}
