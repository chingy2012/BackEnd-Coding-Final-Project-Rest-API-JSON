package game.testing.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import game.testing.entity.Tester;

public interface TesterDao extends JpaRepository<Tester, Long> {

}
