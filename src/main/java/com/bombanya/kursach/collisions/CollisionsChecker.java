package com.bombanya.kursach.collisions;

import com.bombanya.kursach.posPredict.GeoCoords;
import com.bombanya.kursach.posPredict.PosPredictData;
import com.bombanya.kursach.posPredict.PosPredictRepository;
import com.bombanya.kursach.posPredict.PosPredictUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CollisionsChecker {

    private final ConcurrentLinkedQueue<Boolean> notifications = new ConcurrentLinkedQueue<>();
    private final PosPredictRepository posPredictRepository;
    private final EventRepository eventRepository;

    //minutes
    @Value("${collisions.calcInterval.minutes}")
    private long calcInterval;

    //milliseconds
    @Value("${collisions.calcDelta.milliseconds}")
    private long calcDelta;

    //meters
    @Value("${collisions.criticalDistance.meters}")
    private double criticalDistance;

    public void sendNotifyUpdateHappened(){
        notifications.add(true);
    }

    @Async
    public void startChecker(){
        while (true){
            if (!notifications.isEmpty()){
                while (!notifications.isEmpty()) notifications.poll();
                checkCollisions();
            }
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    private void checkCollisions(){
        List<PosPredictData> predictData = posPredictRepository.getAllByPlanet("earth");
        List<String[]> tles = predictData.stream().map(PosPredictData::tle).collect(Collectors.toList());
        List<GeoCoords> satPositions;
        Instant calcMoment = Instant.now().plus(1, ChronoUnit.HOURS);
        Instant endOfCalcInterval = calcMoment.plus(calcInterval, ChronoUnit.MINUTES);
        double distance;

        while (notifications.isEmpty() && calcMoment.isBefore(endOfCalcInterval)){
            Instant finalCalcMoment = calcMoment;
            satPositions = tles.stream()
                    .map((a) -> PosPredictUtils.calcSatPosition(a, finalCalcMoment))
                    .collect(Collectors.toList());
            for (int i = 0; i < predictData.size(); i++){
                for (int j = i + 1; j < predictData.size(); j++){
                    if ((distance = PosPredictUtils.calcDistanceBetweenSats(satPositions.get(i), satPositions.get(j)))
                            <= criticalDistance / 1000){
                        Event newEvent = Event.builder()
                                .noradId1(predictData.get(i).getNoradId())
                                .noradId2(predictData.get(j).getNoradId())
                                .predictedEventTime(calcMoment)
                                .eventCreationTime(Instant.now())
                                .distance(distance)
                                .build();
                        eventRepository.save(newEvent);
                    }
                }
            }
            calcMoment = calcMoment.plus(calcDelta, ChronoUnit.MILLIS);

            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
