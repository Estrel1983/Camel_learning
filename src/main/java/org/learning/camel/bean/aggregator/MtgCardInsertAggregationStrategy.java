package org.learning.camel.bean.aggregator;

import org.learning.camel.entity.Artist;
import org.learning.camel.entity.MtgCardInsertRequest;
import org.learning.camel.entity.Set;

public class MtgCardInsertAggregationStrategy {
    public MtgCardInsertRequest aggregateCardAndArtist(MtgCardInsertRequest request, Artist artist) {
        request.setArtistID(artist.getId());
        return request;
    }
    public MtgCardInsertRequest aggregateCardAndSet(MtgCardInsertRequest request, Set set) {
        request.setSetId(set.getId());
        return request;
    }
}
