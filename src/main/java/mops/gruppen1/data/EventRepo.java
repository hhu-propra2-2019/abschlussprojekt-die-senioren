package mops.gruppen1.data;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository to fetch event data from database
 */
@Repository
public interface EventRepo extends PagingAndSortingRepository<EventDTO, Long> {
    List<EventIdOnly> findTopByOrderByIdDesc();

    List<GroupIdOnly> findDistinctByIdAfter(Long id);

}