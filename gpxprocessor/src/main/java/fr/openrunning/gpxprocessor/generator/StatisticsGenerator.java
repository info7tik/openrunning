package fr.openrunning.gpxprocessor.generator;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import fr.openrunning.gpxprocessor.track.GpxTrack;

@Component
public class StatisticsGenerator {
    private List<Statistics> statistics = new LinkedList<>();

    public void addStatistics(Statistics newStatistics) {
        this.statistics.add(newStatistics);
    }

    public void generateStatistics(GpxTrack track) {
        this.statistics.forEach((statistics) -> {
            statistics.compute(track);
        });
    }
}
