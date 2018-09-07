package com.rackspacecloud.metrics.tenantroutingservice.repositories;

import com.rackspacecloud.metrics.tenantroutingservice.domain.TenantRoutingInformation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITenantRoutingInformationRepository extends CrudRepository<TenantRoutingInformation, String> {
}
