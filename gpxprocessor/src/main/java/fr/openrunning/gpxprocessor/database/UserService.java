package fr.openrunning.gpxprocessor.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openrunning.gpxprocessor.finder.FileEntry;
import fr.openrunning.model.database.gpxfiles.GpxFile;
import fr.openrunning.model.database.gpxfiles.GpxFilesRepository;
import fr.openrunning.model.database.user.User;
import fr.openrunning.model.database.user.UserRepository;
import fr.openrunning.model.type.FileStatus;

public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final GpxFilesRepository filesRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserService(GpxFilesRepository filesRepository, UserRepository userRepository) {
        this.filesRepository = filesRepository;
        this.userRepository = userRepository;
    }

    public List<FileEntry> buildFileEntries() {
        List<FileEntry> results = new ArrayList<>();
        List<GpxFile> checkingFiles = filesRepository.findByStatus(FileStatus.CHECKING);
        for (GpxFile file : checkingFiles) {
            Optional<User> user = this.userRepository.findById(file.getUsedId());
            if (user.isPresent()) {
                results.add(new FileEntry(file.getFilename(), user.get().getEmail(), user.get().getId()));
            } else {
                logger.error("can not find user with id '" + file.getUsedId() + "'");
            }
        }
        return results;
    }
}
