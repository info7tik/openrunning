package fr.openrunning.model.database.gpxfiles;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fr.openrunning.model.type.FileStatus;

public interface GpxFilesRepository extends CrudRepository<GpxFile, String> {
    List<GpxFile> findByStatus(FileStatus status);
}
