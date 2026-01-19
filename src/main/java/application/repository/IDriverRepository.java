package application.repository;

import application.domain.Driver;

import java.util.List;

public interface IDriverRepository extends IRepository<Driver> {

    List<Driver> findByWorldChampionshipsGreaterThan(Integer championships);
    List<Driver> findByTeamId(Integer teamId);
}
