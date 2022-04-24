package io.github.lisaiundralandi.user.library;

import io.github.lisaiundralandi.user.library.entity.GameInLibrary;
import io.github.lisaiundralandi.user.library.entity.GameInLibraryId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLibraryRepository extends CrudRepository<GameInLibrary, GameInLibraryId> {

    List<GameInLibrary> findByUserId(String userId);
}
