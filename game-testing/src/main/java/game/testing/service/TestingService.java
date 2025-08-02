package game.testing.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import game.testing.controller.model.PublisherData;
import game.testing.controller.model.PublisherData.GameData;
import game.testing.controller.model.PublisherData.TesterData;
import game.testing.dao.PublisherDao;
import game.testing.dao.TesterDao;
import game.testing.dao.GameDao;
import game.testing.entity.Game;
import game.testing.entity.Publisher;
import game.testing.entity.Tester;

@Service // Marks this class as a Spring service component
public class TestingService {

	@Autowired
	private PublisherDao publisherDao;

	@Autowired
	private GameDao gameDao;

	@Autowired
	private TesterDao testerDao;

	/**
	 * Saves a publisher to the database.
	 * Converts DTO to entity, persists it, and returns the saved DTO.
	 */
	@Transactional(readOnly = false)
	public PublisherData savePublisher(PublisherData publisherData) {
		Publisher publisher = publisherData.toPublisher();
		Publisher dbPublisher = publisherDao.save(publisher);
		return new PublisherData(dbPublisher);
	}

	/**
	 * Retrieves a single publisher by ID and converts to DTO.
	 */
	@Transactional(readOnly = true)
	public PublisherData retrievePublisherById(Long publisherId) {
		Publisher publisher = findPublisherById(publisherId);
		return new PublisherData(publisher);
	}

	/**
	 * Helper method to find a Publisher or throw exception if not found.
	 */
	private Publisher findPublisherById(Long publisherId) {
		return publisherDao.findById(publisherId)
			.orElseThrow(() -> new NoSuchElementException(
				"Publisher with ID=" + publisherId + " was not found."));
	}

	/**
	 * Retrieves all publishers, sorted by name, and maps them to DTOs.
	 */
	@Transactional(readOnly = true)
	public List<PublisherData> retrieveAllPublishers() {
		List<Publisher> publisherEntities = publisherDao.findAll();
		List<PublisherData> publisherDtos = new LinkedList<>();

		// Sort publishers alphabetically by name
		publisherEntities.sort((pub1, pub2) ->
			pub1.getPublisherName().compareTo(pub2.getPublisherName()));

		// Convert each entity to DTO
		for (Publisher publisher : publisherEntities) {
			publisherDtos.add(new PublisherData(publisher));
		}
		return publisherDtos;
	}

	/**
	 * Deletes a publisher and its associated games (due to cascade).
	 */
	@Transactional(readOnly = false)
	public void deletePublisher(Long publisherId) {
		Publisher publisher = findPublisherById(publisherId);
		publisherDao.delete(publisher);
	}

	/**
	 * Saves or updates a game under a specific publisher.
	 */
	@Transactional(readOnly = false)
	public GameData saveGame(Long publisherId, GameData gameData) {
		Publisher publisher = findPublisherById(publisherId);
		Long gameId = gameData.getGameId();
		Game game = findOrCreateGame(publisherId, gameId);

		copyGameFields(game, gameData); // Sets basic fields
		game.setPublisher(publisher); // Links game to the publisher
		publisher.getGames().add(game); // Adds to publisher's game set

		Game dbGame = gameDao.save(game);
		return new GameData(dbGame);
	}

	/**
	 * Copies game fields from DTO to entity.
	 */
	private void copyGameFields(Game game, GameData gameData) {
		game.setGameName(gameData.getGameName());
		game.setGenre(gameData.getGenre());
		game.setPlatforms(gameData.getPlatforms());
	}

	/**
	 * Helper method to return a new Game if ID is null,
	 * or find an existing one and validate publisher match.
	 */
	private Game findOrCreateGame(Long publisherId, Long gameId) {
		if (gameId == null) {
			return new Game(); // New game
		} else {
			return findGameById(publisherId, gameId);
		}
	}

	/**
	 * Helper method to retrieve a Game by ID and validate its publisher.
	 */
	private Game findGameById(Long publisherId, Long gameId) {
		Game game = gameDao.findById(gameId)
			.orElseThrow(() -> new NoSuchElementException(
				"Game with ID=" + gameId + " not found."));

		// Ensure the game belongs to the specified publisher
		if (!game.getPublisher().getPublisherId().equals(publisherId)) {
			throw new IllegalArgumentException(
				"Game with ID=" + gameId +
				" is not published by ID=" + publisherId);
		}

		return game;
	}

	/**
	 * Retrieves a tester by ID and converts to DTO.
	 */
	@Transactional(readOnly = true)
	public TesterData retrieveTesterById(Long testerId) {
		Tester tester = findTesterById(testerId);
		return new TesterData(tester);
	}

	/**
	 * Helper method to find a Tester or throw exception if not found.
	 */
	private Tester findTesterById(Long testerId) {
		return testerDao.findById(testerId)
			.orElseThrow(() -> new NoSuchElementException(
				"Tester with ID=" + testerId + " was not found."));
	}

	/**
	 * Assigns a tester to a game (bidirectional many-to-many relationship).
	 */
	public GameData addTesterToGame(Long gameId, Long testerId) {
		Game game = gameDao.findById(gameId)
			.orElseThrow(() -> new NoSuchElementException(
				"Game with ID=" + gameId + " not found"));

		Tester tester = testerDao.findById(testerId)
			.orElseThrow(() -> new NoSuchElementException(
				"Tester with ID=" + testerId + " not found"));

		// Establish mutual relationship
		game.getTesters().add(tester);
		tester.getGames().add(game);

		Game dbGame = gameDao.save(game); // Save the relationship

		return new GameData(dbGame);
	}

	/**
	 * Retrieves all testers associated with a specific game.
	 */
	@Transactional(readOnly = true)
	public List<TesterData> retrieveTestersByGameId(Long gameId) {
		Game game = gameDao.findById(gameId)
			.orElseThrow(() -> new NoSuchElementException(
				"Game with ID=" + gameId + " not found."));

		List<TesterData> testers = new LinkedList<>();
		for (Tester tester : game.getTesters()) {
			testers.add(new TesterData(tester));
		}
		return testers;
	}

	/**
	 * Saves a new tester to the database.
	 */
	@Transactional(readOnly = false)
	public TesterData saveTester(TesterData testerData) {
		Tester tester = testerData.toTester();
		Tester dbTester = testerDao.save(tester);
		return new TesterData(dbTester);
	}
}