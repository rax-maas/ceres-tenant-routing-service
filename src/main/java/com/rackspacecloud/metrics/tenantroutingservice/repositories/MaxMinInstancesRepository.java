package com.rackspacecloud.metrics.tenantroutingservice.repositories;

import com.rackspacecloud.metrics.tenantroutingservice.domain.MaxAndMinSeriesInstances;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaxMinInstancesRepository extends CrudRepository<MaxAndMinSeriesInstances, String> {
}
