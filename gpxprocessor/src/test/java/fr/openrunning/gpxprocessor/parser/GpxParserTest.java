package fr.openrunning.gpxprocessor.parser;

import java.io.File;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fr.openrunning.gpxprocessor.gpxparser.GpxParser;
import fr.openrunning.gpxprocessor.track.GpxTrack;

@SpringBootTest
public class GpxParserTest {
    @Autowired
    private GpxParser parser;

    @Test
    public void parseTest() {
        String filepath = "src/test/resources/activity_598628562.gpx";
        try {
            File gpxFile = new File(filepath);
            if (gpxFile.isFile()) {
                GpxTrack track = parser.parse(gpxFile);
                Assertions.assertEquals(track.getFilename(), gpxFile.getName());
                Assertions.assertEquals(478, track.getGpxPoints().size());
            } else {
                Assertions.fail("file " + filepath + " does not exist!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }
}
