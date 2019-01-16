package com.rackspacecloud.metrics.tenantroutingservice.services;

import com.rackspacecloud.metrics.tenantroutingservice.domain.RetentionPolicyEnum;
import com.rackspacecloud.metrics.tenantroutingservice.domain.TenantRoutes;
import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationInput;
import com.rackspacecloud.metrics.tenantroutingservice.repositories.ITenantRoutingInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class RoutingService implements IRoutingService {
    @Autowired
    ITenantRoutingInformationRepository routingInformationRepository;

    @Override
    public TenantRoutes setIngestionRoutingInformation(
            String tenantId, IngestionRoutingInformationInput tenantInfo) {

        if(StringUtils.isEmpty(tenantId) || StringUtils.isEmpty(tenantId.trim()))
            throw new IllegalArgumentException("'tenantId' is null, empty or contains all whitespaces.");

        if(tenantInfo == null) throw new IllegalArgumentException("'tenantInfo' is null.");

        Optional<TenantRoutes> oRoutingInformation = routingInformationRepository.findById(tenantId);

        TenantRoutes routingInformation;
        if(oRoutingInformation.isPresent()){
            routingInformation = oRoutingInformation.get();
        }
        else{
            routingInformation = new TenantRoutes(tenantInfo, createListOfDefaultRoutes());
        }

        // NOTE: following line will update the value for the key if it's already existing in the map.
        // It's assumed that for a given tenant, there will be ONLY ONE database in a given influxdb instance
        routingInformation.setTenantId(tenantId);

        TenantRoutes routingInfo = routingInformationRepository.save(routingInformation);
        return routingInfo;
    }

    @Override
    public TenantRoutes setIngestionRoutingInformation(String tenantId, TenantRoutes routingInformation) {
        if(StringUtils.isEmpty(tenantId) || StringUtils.isEmpty(tenantId.trim()))
            throw new IllegalArgumentException("'tenantId' is null, empty or contains all whitespaces.");

        if(routingInformation == null) throw new IllegalArgumentException("'routingInformation' is null.");

        routingInformation.setTenantId(tenantId);
        return routingInformationRepository.save(routingInformation);
    }

    @Override
    public TenantRoutes getIngestionRoutingInformation(String tenantId) {

        if(StringUtils.isEmpty(tenantId) || StringUtils.isEmpty(tenantId.trim()))
            throw new IllegalArgumentException("'tenantId' is null, empty or contains all whitespaces.");

        Optional<TenantRoutes> routingInfo = routingInformationRepository.findById(tenantId);
        if(routingInfo.isPresent()){
            return routingInfo.get();
        }
        return null;
    }

    @Override
    public void removeIngestionRoutingInformation(String tenantId) {
        if(StringUtils.isEmpty(tenantId) || StringUtils.isEmpty(tenantId.trim()))
            throw new IllegalArgumentException("'tenantId' is null, empty or contains all whitespaces.");

        routingInformationRepository.deleteById(tenantId);
    }

    private List<RetentionPolicyEnum> createListOfDefaultRoutes(){
        List<RetentionPolicyEnum> list = new LinkedList();
        list.add(RetentionPolicyEnum.FULL);
        list.add(RetentionPolicyEnum.FIVE_MINUTES);
        list.add(RetentionPolicyEnum.TWENTY_MINUTES);
        list.add(RetentionPolicyEnum.ONE_HOUR);
        list.add(RetentionPolicyEnum.FOUR_HOURS);
        list.add(RetentionPolicyEnum.ONE_DAY);

        return list;
    }
}
