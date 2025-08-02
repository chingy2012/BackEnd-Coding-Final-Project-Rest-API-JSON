package game.testing.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import game.testing.entity.Game;

public interface GameDao extends JpaRepository<Game, Long> {

}
