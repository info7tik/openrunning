package fr.openrunning.gpxprocessor.database;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openrunning.model.Track;
import fr.openrunning.model.TrackId;
import fr.openrunning.model.User;

@Service
public class DatabaseService {
    private final Logger logger = LoggerFactory.getLogger(DatabaseService.class);
    private final UserRepository userRepository;
    private final TracksRepository tracksRepository;

    @Autowired
    public DatabaseService(UserRepository userRepository, TracksRepository tracksRepository) {
        this.userRepository = userRepository;
        this.tracksRepository = tracksRepository;
    }

    public int getUserIdFromEmail(String email) {
        List<User> users = userRepository.findByEmail(email);
        if (users.isEmpty()) {
            return -1;
        } else {
            if (users.size() > 1) {
                logger.error("more than one user found from " + email);
            }
            return users.get(0).getId();
        }
    }

    public boolean existsTrack(int userId, long timestamp) {
        TrackId trackId = new TrackId(timestamp, userId);
        return tracksRepository.existsById(trackId);
    }

    public void deleteTrack(int userId, long timestamp) {
        TrackId trackId = new TrackId(timestamp, userId);
        tracksRepository.deleteById(trackId);
    }

    public void insertTrack(Track track) {
        tracksRepository.save(track);
    }
}
