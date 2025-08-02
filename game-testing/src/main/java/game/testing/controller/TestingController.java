package game.testing.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import game.testing.controller.model.PublisherData;
import game.testing.controller.model.PublisherData.GameData;
import game.testing.controller.model.PublisherData.TesterData;
import game.testing.service.TestingService;
import lombok.extern.slf4j.Slf4j;

@RestController // Marks this class as a REST controller
@RequestMapping("/game_testing") // Base URL path for all endpoints in this controller
@Slf4j // Enables logging via Lombok
public class TestingController {

	@Autowired
	private TestingService testingService; // Service layer to handle business logic

	// Create a new publisher
	@PostMapping("/publisher")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PublisherData createPublisher(@RequestBody PublisherData publisherData) {
		log.info("Creating publisher {}", publisherData);
		return testingService.savePublisher(publisherData);
	}

	// Update an existing publisher
	@PutMapping("/publisher/{publisherId}")
	public PublisherData updatePublisher(@PathVariable Long publisherId,
			@RequestBody PublisherData publisherData) {
		publisherData.setPublisherId(publisherId);
		log.info("Updating publisher {}", publisherData);
		return testingService.savePublisher(publisherData);
	}

	// Add a new game under a specific publisher
	@PostMapping("/{publisherId}/game")
	@ResponseStatus(code = HttpStatus.CREATED)
	public GameData addGame(@PathVariable Long publisherId, 
			@RequestBody GameData game) {
		log.info("Adding game {} to publisher with ID {}", game, publisherId);
		return testingService.saveGame(publisherId, game);
	}

	// Retrieve a tester by tester ID
	@GetMapping("/tester/{testerId}")
	public TesterData retrieveTester(@PathVariable Long testerId) {
		log.info("Retrieving tester with ID={}", testerId);
		return testingService.retrieveTesterById(testerId);
	}

	// Associate an existing tester with a game
	@PostMapping("/game/{gameId}/tester/{testerId}")
	public GameData addTesterToGame(@PathVariable Long gameId,
			@PathVariable Long testerId) {
		return testingService.addTesterToGame(gameId, testerId);
	}

	// Retrieve all testers associated with a specific game
	@GetMapping("/game/{gameId}/testers")
	public List<TesterData> retrieveTestersByGameId(@PathVariable Long gameId) {
	    log.info("Retrieving testers for game with ID={}", gameId);
	    return testingService.retrieveTestersByGameId(gameId);
	}

	// Retrieve a publisher by ID
	@GetMapping("/publisher/{publisherId}")
	public PublisherData retrievePublisher(@PathVariable Long publisherId) {
		log.info("Retrieving publisher with ID={}", publisherId);
		return testingService.retrievePublisherById(publisherId);
	}

	// Retrieve all publishers
	@GetMapping("/publisher")
	public List<PublisherData> retrieveAllPublishers() {
		log.info("Retrieving all publishers");
		return testingService.retrieveAllPublishers();
	}

	// Delete a publisher by ID
	@DeleteMapping("/publisher/{publisherId}")
	public Map<String, String> deletePublisher(@PathVariable Long publisherId) {
		log.info("Deleting publisher with ID={}" + publisherId + ".");
		testingService.deletePublisher(publisherId);

		return Map.of("message", "Publisher with ID=" + publisherId + 
				" was deleted successfully.");
	}

	// Create a new tester
	@PostMapping("/tester")
	@ResponseStatus(code = HttpStatus.CREATED)
	public TesterData createTester(@RequestBody TesterData testerData) {
		log.info("Creating tester: {}", testerData);
		return testingService.saveTester(testerData);
	}
}