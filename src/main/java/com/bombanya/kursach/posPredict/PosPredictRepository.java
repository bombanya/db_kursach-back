package com.bombanya.kursach.posPredict;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface PosPredictRepository extends Repository<PosPredictData, Integer> {

    @Query("select * from get_all_sat_data_for_position_prediction where norad_id = :noradId")
    Optional<PosPredictData> findByNoradId(@Param("noradId") int noradId);

    @Query("select * from get_all_sat_data_for_position_prediction where planet = :planet")
    List<PosPredictData> getAllByPlanet(@Param("planet") String planet);
}
