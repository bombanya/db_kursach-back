package com.bombanya.kursach;

import com.bombanya.kursach.collisions.CollisionsChecker;
import com.bombanya.kursach.gp.GPFetcher;
import com.bombanya.kursach.gp.GPRepository;
import com.bombanya.kursach.posPredict.PosPredictData;
import com.bombanya.kursach.posPredict.PosPredictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServicesStarter {

    private final CollisionsChecker collisionsChecker;
    private final GPFetcher fetcher;
    private final GPRepository gpRepository;
    private final PosPredictRepository posPredictRepository;

    @Value("${services.updater.on}")
    private boolean updaterStatus;

    @PostConstruct
    public void init(){
        collisionsChecker.sendNotifyUpdateHappened();
        collisionsChecker.startChecker();
    }

    @Scheduled(fixedDelay = 3, timeUnit = TimeUnit.HOURS)
    public void updateSatData(){
        if (updaterStatus) {
            List<PosPredictData> satData = posPredictRepository.getAllByPlanet("earth");
            gpRepository.saveAll(satData.stream()
                    .map((a) -> fetcher.fetch(a.getNoradId()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
            collisionsChecker.sendNotifyUpdateHappened();
        }
    }
}
