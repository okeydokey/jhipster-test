package net.slipp.test.repository.search;

import net.slipp.test.domain.OrderItems;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OrderItems entity.
 */
public interface OrderItemsSearchRepository extends ElasticsearchRepository<OrderItems, Long> {
}
