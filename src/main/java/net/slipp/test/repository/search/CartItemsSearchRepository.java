package net.slipp.test.repository.search;

import net.slipp.test.domain.CartItems;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CartItems entity.
 */
public interface CartItemsSearchRepository extends ElasticsearchRepository<CartItems, Long> {
}
