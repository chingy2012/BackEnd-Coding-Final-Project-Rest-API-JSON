package game.testing.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity // Marks this class as a JPA entity mapped to a database table
@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode
public class Game {

	@Id // Declares the primary key of the entity
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the primary key value using identity strategy
	private Long gameId;

	// Fields describing the game’s properties
	@EqualsAndHashCode.Exclude // Exclude from equals/hashCode to avoid potential circular references
	private String gameName;

	@EqualsAndHashCode.Exclude
	private String genre;

	@EqualsAndHashCode.Exclude
	private String platforms;

	/**
	 * Many-to-one relationship: many games belong to one publisher.
	 * 
	 * - `@JoinColumn(name = "publisher_id")` specifies the foreign key column in the Game table.
	 * - `nullable = false` enforces that every game must be linked to a publisher.
	 * - `@ToString.Exclude` prevents circular reference in generated toString().
	 */
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToOne
	@JoinColumn(name = "publisher_id", nullable = false)
	private Publisher publisher;

	/**
	 * Many-to-many relationship: a game can have multiple testers and a tester can test multiple games.
	 * 
	 * - `@JoinTable` defines the join table `game_tester` that holds the foreign keys.
	 * - `cascade = CascadeType.PERSIST` means persisting a Game will also persist any new Testers.
	 * - `@ToString.Exclude` prevents infinite recursion in toString().
	 */
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(
		name = "game_tester", // Join table name
		joinColumns = @JoinColumn(name = "game_id"), // Foreign key to Game
		inverseJoinColumns = @JoinColumn(name = "tester_id") // Foreign key to Tester
	)
	private Set<Tester> testers = new HashSet<>(); // Testers assigned to test this game
}