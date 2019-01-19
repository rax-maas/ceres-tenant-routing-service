package com.rackspacecloud.metrics.tenantroutingservice.services;

import com.rackspacecloud.metrics.tenantroutingservice.domain.RetentionPolicyEnum;
import com.rackspacecloud.metrics.tenantroutingservice.domain.TenantRoutes;
import com.rackspacecloud.metrics.tenantroutingservice.exceptions.RouteConflictException;
import com.rackspacecloud.metrics.tenantroutingservice.exceptions.RouteDeleteException;
import com.rackspacecloud.metrics.tenantroutingservice.exceptions.RouteNotFoundException;
import com.rackspacecloud.metrics.tenantroutingservice.exceptions.RouteWriteException;
import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationInput;
import com.rackspacecloud.metrics.tenantroutingservice.repositories.ITenantRoutingInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoutingServiceImpl implements RoutingService {
    @Autowired
    ITenantRoutingInformationRepository routingInformationRepository;

    @Override
    public TenantRoutes setIngestionRoutingInformation(String tenantId, IngestionRoutingInformationInput tenantInfo) {
        if(StringUtils.isEmpty(tenantId) || StringUtils.isEmpty(tenantId.trim()))
            throw new IllegalArgumentException("'tenantId' is null, empty or contains all whitespaces.");

        if(tenantInfo == null) throw new IllegalArgumentException("'tenantInfo' is null.");

        Optional<TenantRoutes> oRoutingInformation = routingInformationRepository.findById(tenantId);

        if(oRoutingInformation.isPresent()) throw new RouteConflictException(tenantId);

        TenantRoutes routingInformation = new TenantRoutes(tenantId, tenantInfo, createListOfDefaultRoutes());

        try {
            return routingInformationRepository.save(routingInformation);
        }
        catch (Exception e) {
            throw new RouteWriteException(routingInformation.toString(), e);
        }
    }

    @Override
    public TenantRoutes getIngestionRoutingInformation(String tenantId) {

        if(StringUtils.isEmpty(tenantId) || StringUtils.isEmpty(tenantId.trim()))
            throw new IllegalArgumentException("'tenantId' is null, empty or contains all whitespaces.");

        Optional<TenantRoutes> routingInfo = routingInformationRepository.findById(tenantId);

        if(!routingInfo.isPresent()) throw new RouteNotFoundException(tenantId);

        return routingInfo.get();
    }

    @Override
    public void removeIngestionRoutingInformation(String tenantId) {
        if(StringUtils.isEmpty(tenantId) || StringUtils.isEmpty(tenantId.trim()))
            throw new IllegalArgumentException("'tenantId' is null, empty or contains all whitespaces.");

        try {
            routingInformationRepository.deleteById(tenantId);
        }
        catch (Exception e) {
            throw new RouteDeleteException(tenantId, e);
        }
    }

    private List<RetentionPolicyEnum> createListOfDefaultRoutes(){
        List<RetentionPolicyEnum> list = new ArrayList<>();
        list.add(RetentionPolicyEnum.FULL);
        list.add(RetentionPolicyEnum.FIVE_MINUTES);
        list.add(RetentionPolicyEnum.TWENTY_MINUTES);
        list.add(RetentionPolicyEnum.ONE_HOUR);
        list.add(RetentionPolicyEnum.FOUR_HOURS);
        list.add(RetentionPolicyEnum.ONE_DAY);

        return list;
    }
}
