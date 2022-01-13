package com.bombanya.kursach.readable;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface ReadableDataRepository extends Repository<ReadableSatData, Integer> {

    @Query("select * from get_readable_data_about_sats where planet_name = :planet")
    List<ReadableSatData> getAllByPlanet(@Param("planet") String planet);

    @Query("select * from get_readable_data_about_sats where norad_id = :noradId")
    Optional<ReadableSatData> getByNoradId(@Param("noradId") int noradId);
}
