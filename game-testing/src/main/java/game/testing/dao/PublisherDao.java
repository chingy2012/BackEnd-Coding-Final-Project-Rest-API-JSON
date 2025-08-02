package game.testing.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import game.testing.entity.Publisher;

public interface PublisherDao extends JpaRepository<Publisher, Long> {

}
