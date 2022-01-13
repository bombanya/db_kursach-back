package com.bombanya.kursach.gp;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GPRepository extends CrudRepository<GPData, Integer> {

    @Query("select find_dangerous_satellites as norad_id from find_dangerous_satellites(:norad)")
    List<GPData> findDangerousSats(@Param("norad") Integer norad);
}
