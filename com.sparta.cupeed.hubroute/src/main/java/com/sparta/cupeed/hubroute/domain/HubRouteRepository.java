package com.sparta.cupeed.hubroute.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubRouteRepository {

	HubRoute save(HubRoute hubRoute);

	Optional<HubRoute> findById(UUID id);

	List<HubRoute> findAll();

	void delete(HubRoute hubRoute);

	Optional<HubRoute> findByStartHubIdAndEndHubId(UUID startHubId, UUID endHubId);
}
