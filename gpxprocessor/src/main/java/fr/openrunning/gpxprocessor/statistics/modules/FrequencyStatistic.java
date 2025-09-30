package fr.openrunning.gpxprocessor.statistics.modules;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import fr.openrunning.gpxprocessor.statistics.StatisticModule;
import fr.openrunning.gpxprocessor.statistics.StatisticModuleName;
import fr.openrunning.gpxprocessor.track.GpxTrack;
import fr.openrunning.model.database.frequency.Frequency;
import fr.openrunning.model.exception.OpenRunningException;
import fr.openrunning.model.type.FrequencyType;
import lombok.Getter;

public class FrequencyStatistic extends StatisticModule<Frequency> {
    @Getter
    private long dayTimestamp = 0L;
    @Getter
    private long weekTimestamp = 0L;
    @Getter
    private long monthTimestamp = 0L;
    @Getter
    private long yearTimestamp = 0L;
    @Getter
    private int timeInSeconds = 0;
    @Getter
    private int distanceInMeters = 0;

    @Autowired
    public FrequencyStatistic() {
        super(StatisticModuleName.FREQUENCY);
    }

    @Override
    public void compute(GpxTrack track) throws OpenRunningException {
        setDistanceInMeters(track);
        setTimeInSeconds(track);
        Calendar trackDate = Calendar.getInstance();
        trackDate.setTimeInMillis(track.getFirstTime() * 1000);
        setDayTimestamp(trackDate);
        setWeekTimestamp(trackDate);
        setMonthTimestamp(trackDate);
        setYearTimestamp(trackDate);
    }

    private void setDayTimestamp(Calendar date) {
        date.set(Calendar.MILLISECOND, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.HOUR_OF_DAY, 0);
        dayTimestamp = date.getTimeInMillis() / 1000;
    }

    private void setWeekTimestamp(Calendar date) {
        date.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        weekTimestamp = date.getTimeInMillis() / 1000;
    }

    private void setMonthTimestamp(Calendar date) {
        date.set(Calendar.DAY_OF_MONTH, 1);
        monthTimestamp = date.getTimeInMillis() / 1000;
    }

    private void setYearTimestamp(Calendar date) {
        date.set(Calendar.DAY_OF_YEAR, 1);
        yearTimestamp = date.getTimeInMillis() / 1000;
    }

    private void setTimeInSeconds(GpxTrack track) {
        timeInSeconds = track.getTimeInSeconds();
    }

    private void setDistanceInMeters(GpxTrack track) {
        distanceInMeters = track.getDistanceInMeters();
    }

    @Override
    public List<Frequency> toDatabaseObject(int userId) {
        List<Frequency> databaseObjects = new ArrayList<>();
        databaseObjects.add(buildDayFrequency(userId));
        databaseObjects.add(buildWeekFrequency(userId));
        databaseObjects.add(buildMonthFrequency(userId));
        databaseObjects.add(buildYearFrequency(userId));
        return databaseObjects;
    }

    private Frequency buildDayFrequency(int userId) {
        Frequency dayFrequency = buildGenericFrequency(userId);
        dayFrequency.setFrequency(FrequencyType.DAILY);
        dayFrequency.setTimestamp(dayTimestamp);
        return dayFrequency;
    }

    private Frequency buildWeekFrequency(int userId) {
        Frequency weekFrequency = buildGenericFrequency(userId);
        weekFrequency.setFrequency(FrequencyType.WEEKLY);
        weekFrequency.setTimestamp(weekTimestamp);
        return weekFrequency;
    }

    private Frequency buildMonthFrequency(int userId) {
        Frequency monthFrequency = buildGenericFrequency(userId);
        monthFrequency.setFrequency(FrequencyType.MONTHLY);
        monthFrequency.setTimestamp(monthTimestamp);
        return monthFrequency;
    }

    private Frequency buildYearFrequency(int userId) {
        Frequency yearFrequency = buildGenericFrequency(userId);
        yearFrequency.setFrequency(FrequencyType.YEARLY);
        yearFrequency.setTimestamp(yearTimestamp);
        return yearFrequency;
    }

    private Frequency buildGenericFrequency(int userId) {
        Frequency genericFrequency = new Frequency();
        genericFrequency.setTotalDistanceInMeters(distanceInMeters);
        genericFrequency.setTotalTimeInSeconds(timeInSeconds);
        genericFrequency.setUserId(userId);
        return genericFrequency;
    }
}
