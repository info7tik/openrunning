package fr.openrunning.gpxprocessor.gpxparser;

import java.time.Instant;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import fr.openrunning.gpxprocessor.track.GpxPoint;
import fr.openrunning.gpxprocessor.track.GpxTrack;

public class GpxHandler extends DefaultHandler {
    private StringBuffer buffer = new StringBuffer();
    private double latitude;
    private double longitude;
    private double altitude;
    private Instant timestamp;
    private GpxTrack track = new GpxTrack();

    @Override
    public void startElement(final String URI, final String LOCAL_NAME, final String Q_NAME,
            final Attributes ATTRIBUTES) throws SAXException {
        buffer.setLength(0);
        if (Q_NAME.equals("trkpt")) {
            latitude = Double.parseDouble(ATTRIBUTES.getValue("lat"));
            longitude = Double.parseDouble(ATTRIBUTES.getValue("lon"));
        }
    }

    @Override
    public void endElement(final String URI, final String LOCAL_NAME, final String Q_NAME) throws SAXException {
        if (Q_NAME.equals("trkpt")) {
            track.addGpxPoint(new GpxPoint(latitude, longitude, altitude, timestamp));
        } else if (Q_NAME.equals("alt")) {
            altitude = Double.parseDouble(buffer.toString());
        } else if (Q_NAME.equals("time")) {
            timestamp = Instant.parse(buffer.toString());
        } else if (Q_NAME.equals("name")) {
            track.setName(buffer.toString());
        }
    }

    @Override
    public void characters(final char[] CHARS, final int START, final int LENGTH) throws SAXException {
        buffer.append(CHARS, START, LENGTH);
    }

    public GpxTrack getTrack() {
        return track;
    }
}
