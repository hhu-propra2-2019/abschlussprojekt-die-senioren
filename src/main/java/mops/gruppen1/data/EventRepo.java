package mops.gruppen1.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository to fetch event data from database
 */
@Repository
public interface EventRepo extends CrudRepository<EventDTO, Long> {

}