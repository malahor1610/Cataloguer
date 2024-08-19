package com.github.malahor.videoteka.repository;

import com.github.malahor.videoteka.domain.ShowEntity;
import com.github.malahor.videoteka.domain.ShowType;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource(collectionResourceRel = "shows", path = "shows")
public interface ShowRepository extends CrudRepository<ShowEntity, Long>, PagingAndSortingRepository<ShowEntity, Long> {

  @RestResource(path = "byType", rel = "byType")
  List<ShowEntity> findByTypeOrderByPosition(ShowType type);

  @Query(value = "SELECT MAX(s.position) FROM ShowEntity s")
  Optional<Integer> findMaxPosition();

  @Transactional
  @Modifying
  @Query("update ShowEntity s set s.position = ?1 where s.id = ?2")
  void updatePosition(int position, long id);
}
