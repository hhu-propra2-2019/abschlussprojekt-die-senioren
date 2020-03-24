package mops.gruppen1.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Repository to fetch event data from database
 */
@Repository
public interface EventRepo extends CrudRepository<EventDTO, Long> {
    Collection<EventIdOnly> findFirstByOrderByEventIdDesc();
    Collection<GroupIdOnly> findByIdGreaterThan(Long id);

}