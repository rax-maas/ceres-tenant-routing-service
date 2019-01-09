package com.rackspacecloud.metrics.tenantroutingservice.services;

import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationInput;
import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationOutput;
import com.rackspacecloud.metrics.tenantroutingservice.domain.TenantRoutingInformation;
import com.rackspacecloud.metrics.tenantroutingservice.repositories.ITenantRoutingInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class RoutingService implements IRoutingService {
    @Autowired
    ITenantRoutingInformationRepository routingInformationRepository;

    @Override
    public TenantRoutingInformation setIngestionRoutingInformation(
            String tenantId, IngestionRoutingInformationInput tenantInfo) {

        if(StringUtils.isEmpty(tenantId) || StringUtils.isEmpty(tenantId.trim()))
            throw new IllegalArgumentException("'tenantId' is null, empty or contains all whitespaces.");

        if(tenantInfo == null) throw new IllegalArgumentException("'tenantInfo' is null.");

        Optional<TenantRoutingInformation> oRoutingInformation = routingInformationRepository.findById(tenantId);

        TenantRoutingInformation routingInformation;
        if(oRoutingInformation.isPresent()){
            routingInformation = oRoutingInformation.get();
        }
        else{
            routingInformation = new TenantRoutingInformation(tenantInfo);
        }

        routingInformation = new TenantRoutingInformation(tenantInfo);
        // NOTE: following line will update the value for the key if it's already existing in the map.
        // It's assumed that for a given tenant, there will be ONLY ONE database in a given influxdb instance
        routingInformation.setTenantId(tenantId);

        TenantRoutingInformation routingInfo = routingInformationRepository.save(routingInformation);
        return routingInfo;
    }

    @Override
    public IngestionRoutingInformationOutput getIngestionRoutingInformation(String tenantId) {

        if(StringUtils.isEmpty(tenantId) || StringUtils.isEmpty(tenantId.trim()))
            throw new IllegalArgumentException("'tenantId' is null, empty or contains all whitespaces.");

        Optional<TenantRoutingInformation> routingInfo = routingInformationRepository.findById(tenantId);
        if(routingInfo.isPresent()){
            IngestionRoutingInformationOutput out = new IngestionRoutingInformationOutput();
            //out.setPath(routingInfo.get().getIngestionPath());
            return out;
        }
        return null;
    }
}
