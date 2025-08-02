package game.testing.controller.model;

import java.util.HashSet;
import java.util.Set;

import game.testing.entity.Game;
import game.testing.entity.Publisher;
import game.testing.entity.Tester;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Lombok annotation to generate a no-args constructor
public class PublisherData {

	// Fields representing Publisher attributes
	private Long publisherId;
	private String publisherName;
	private String publisherEmail;
	private String publisherPhone;
	private String location;
	private double rating;

	// Collection of games associated with this publisher
	private Set<GameData> games = new HashSet<>();

	/**
	 * Constructs a PublisherData DTO from a Publisher entity.
	 * Maps entity fields and converts Game entities to GameData.
	 */
	public PublisherData(Publisher publisher) {
		this.publisherId = publisher.getPublisherId();
		this.publisherName = publisher.getPublisherName();
		this.publisherEmail = publisher.getPublisherEmail();
		this.publisherPhone = publisher.getPublisherPhone();
		this.location = publisher.getLocation();
		this.rating = publisher.getRating();

		// Convert each Game entity to a GameData object
		for (Game game : publisher.getGames()) {
			this.games.add(new GameData(game));
		}
	}

	/**
	 * Manual constructor for convenience, useful for creating test or mock data.
	 */
	public PublisherData(Long publisherId, String publisherName, String publisherEmail,
			String publisherPhone, String location, double rating) {
		this.publisherId = publisherId;
		this.publisherName = publisherName;
		this.publisherEmail = publisherEmail;
		this.publisherPhone = publisherPhone;
		this.location = location;
		this.rating = rating;
	}

	/**
	 * Converts this DTO back into a Publisher entity.
	 * This is typically used when saving to the database.
	 */
	public Publisher toPublisher() {
		Publisher publisher = new Publisher();

		publisher.setPublisherId(publisherId);
		publisher.setPublisherName(publisherName);
		publisher.setPublisherEmail(publisherEmail);
		publisher.setPublisherPhone(publisherPhone);
		publisher.setLocation(location);
		publisher.setRating(rating);

		// Convert GameData DTOs to Game entities
		for (GameData gameData : games) {
			publisher.getGames().add(gameData.toGame());
		}

		return publisher;
	}

	/**
	 * Nested static class representing Game data for a publisher.
	 */
	@Data
	@NoArgsConstructor
	public static class GameData {

		// Fields representing Game attributes
		private Long gameId;
		private String gameName;
		private String genre;
		private String platforms;

		// Stores IDs of testers assigned to this game
		private Set<Long> testerId = new HashSet<>();

		/**
		 * Constructs GameData DTO from a Game entity.
		 * Maps core fields and extracts tester IDs.
		 */
		public GameData(Game game) {
			this.gameId = game.getGameId();
			this.gameName = game.getGameName();
			this.genre = game.getGenre();
			this.platforms = game.getPlatforms();

			// Extract tester IDs from the Game's testers
			for (Tester tester : game.getTesters()) {
				this.testerId.add(tester.getTesterId());
			}
		}

		/**
		 * Converts this GameData DTO back to a Game entity.
		 * Note: Tester associations must be handled separately.
		 */
		public Game toGame() {
			Game game = new Game();

			game.setGameId(gameId);
			game.setGameName(gameName);
			game.setGenre(genre);
			game.setPlatforms(platforms);

			return game;
		}
	}

	/**
	 * Nested static class representing Tester data.
	 */
	@Data
	@NoArgsConstructor
	public static class TesterData {

		// Fields representing Tester attributes
		private Long testerId;
		private String testerName;
		private String testerEmail;
		private String testerPhone;

		/**
		 * Constructs TesterData DTO from a Tester entity.
		 */
		public TesterData(Tester tester) {
			this.testerId = tester.getTesterId();
			this.testerName = tester.getTesterName();
			this.testerEmail = tester.getTesterEmail();
			this.testerPhone = tester.getTesterPhone();
		}

		/**
		 * Converts this TesterData DTO back into a Tester entity.
		 */
		public Tester toTester() {
			Tester tester = new Tester();

			tester.setTesterId(testerId);
			tester.setTesterName(testerName);
			tester.setTesterEmail(testerEmail);
			tester.setTesterPhone(testerPhone);

			return tester;
		}
	}
}