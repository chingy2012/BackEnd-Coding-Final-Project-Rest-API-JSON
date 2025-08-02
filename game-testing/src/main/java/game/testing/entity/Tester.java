package game.testing.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity // Marks this class as a JPA entity mapped to a database table
@Data // Lombok annotation to generate boilerplate code (getters, setters, toString, etc.)
public class Tester {

	@Id // Marks this field as the primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the ID using database identity column
	private Long testerId;

	// Basic information fields for the Tester
	private String testerName;
	private String testerEmail;
	private String testerPhone;

	@EqualsAndHashCode.Exclude // Prevents infinite loops during equality checks
	@ToString.Exclude // Prevents recursive printing in toString (avoids stack overflow)
	@ManyToMany(mappedBy = "testers") // Defines the inverse side of the many-to-many relationship with Game
	private Set<Game> games = new HashSet<>(); // Games that this tester is assigned to
}