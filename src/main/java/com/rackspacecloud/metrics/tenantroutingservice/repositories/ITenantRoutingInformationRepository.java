package com.rackspacecloud.metrics.tenantroutingservice.repositories;

import com.rackspacecloud.metrics.tenantroutingservice.domain.TenantRoutes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITenantRoutingInformationRepository extends CrudRepository<TenantRoutes, String> {
}
