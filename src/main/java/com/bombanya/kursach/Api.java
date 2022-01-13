package com.bombanya.kursach;

import com.bombanya.kursach.collisions.*;
import com.bombanya.kursach.gp.*;
import com.bombanya.kursach.posPredict.*;
import com.bombanya.kursach.readable.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sat")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "https://bombanya.github.io"})
public class Api {

    private final PosPredictRepository posPredictRepository;
    private final GPRepository gpRepository;
    private final ReadableDataRepository readableDataRepository;
    private final EventRepository eventRepository;
    private final GPFetcher fetcher;
    private final CollisionsChecker collisionsChecker;

    @GetMapping(value = "/all")
    ResponseEntity<List<ReadableSatData>> getAllSatData(){
        return new ResponseEntity<>(readableDataRepository.getAllByPlanet("earth"), HttpStatus.OK);
    }

    @PostMapping(value = "/new/{noradId}")
    ResponseEntity<ReadableSatData> newSat(@PathVariable int noradId){
        GPData satData = fetcher.fetch(noradId);
        if (satData == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        if (!posPredictRepository.findByNoradId(satData.getNoradId()).isPresent()){
            gpRepository.save(satData);
            collisionsChecker.sendNotifyUpdateHappened();
            return new ResponseEntity<>(readableDataRepository.getByNoradId(satData.getNoradId()).orElse(null),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping(value = "/predict/moment/{noradId}/{moment}")
    ResponseEntity<GeoCoords> predictMomentPosition(@PathVariable int noradId, @PathVariable Instant moment){
        PosPredictData sat = posPredictRepository.findByNoradId(noradId).orElse(null);
        if (sat == null || moment.isBefore(Instant.now())) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(PosPredictUtils.calcSatPosition(sat.tle(), moment), HttpStatus.OK);
    }

    @GetMapping(value = "/events/new/{eventId}")
    ResponseEntity<List<Event>> getNewEvents(@PathVariable long eventId){
        return new ResponseEntity<>(eventRepository.findByEventIdGreaterThan(eventId), HttpStatus.OK);
    }

    @GetMapping(value = "/dangerous/{noradId}")
    ResponseEntity<List<Integer>> getDangerousSats(@PathVariable Integer noradId){
        return new ResponseEntity<>(gpRepository.findDangerousSats(noradId)
                .stream()
                .map(GPData::getNoradId)
                .collect(Collectors.toList()), HttpStatus.OK);
    }


}
