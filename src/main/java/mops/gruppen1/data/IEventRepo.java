package mops.gruppen1.data;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository to fetch event data from database.
 */
@Repository
public interface IEventRepo extends PagingAndSortingRepository<EventDTO, Long> {
    List<IEventIdOnly> findTopByOrderByIdDesc();

    List<IGroupIdOnly> findDistinctByIdAfter(Long id);

}