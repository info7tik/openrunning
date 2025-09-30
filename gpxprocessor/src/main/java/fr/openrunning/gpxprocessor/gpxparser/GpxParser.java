package fr.openrunning.gpxprocessor.gpxparser;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.stereotype.Service;

import fr.openrunning.gpxprocessor.track.GpxTrack;
import fr.openrunning.model.exception.OpenRunningException;

@Service
public class GpxParser {
    private SAXParserFactory factory = SAXParserFactory.newInstance();

    public GpxTrack parse(File gpxFile) throws OpenRunningException {
        try {
            SAXParser saxParser = factory.newSAXParser();
            GpxHandler handler = new GpxHandler();
            saxParser.parse(new FileInputStream(gpxFile), handler);
            GpxTrack track = handler.getTrack();
            track.setFilename(gpxFile.getName());
            return track;
        } catch (Exception e) {
            throw new OpenRunningException("error while parsing '" + gpxFile.getAbsolutePath() + "'", e);
        }
    }
}
