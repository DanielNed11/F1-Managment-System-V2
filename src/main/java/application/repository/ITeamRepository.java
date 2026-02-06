package application.repository;

import application.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITeamRepository extends JpaRepository<Team, Integer> {

    void add(Team team);

    void update(Team team);

    void delete(Team team);
}
