package com.rackspacecloud.metrics.tenantroutingservice;

import com.rackspacecloud.metrics.tenantroutingservice.domain.RetentionPolicyEnum;
import com.rackspacecloud.metrics.tenantroutingservice.domain.TenantRoutes;
import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationInput;
import com.rackspacecloud.metrics.tenantroutingservice.repositories.ITenantRoutingInformationRepository;
import com.rackspacecloud.metrics.tenantroutingservice.services.RoutingService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RoutingServiceUnitTest {

    @Mock
    private ITenantRoutingInformationRepository routingInformationRepository;

    @InjectMocks
    private RoutingService routingService;

    private List<RetentionPolicyEnum> list;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        list = new LinkedList();
        list.add(RetentionPolicyEnum.FULL);
        list.add(RetentionPolicyEnum.FIVE_MINUTES);
        list.add(RetentionPolicyEnum.TWENTY_MINUTES);
        list.add(RetentionPolicyEnum.ONE_HOUR);
        list.add(RetentionPolicyEnum.FOUR_HOURS);
        list.add(RetentionPolicyEnum.ONE_DAY);
    }

    @Test
    public void test_setIngestionRoutingInformation_validInput_returnsTenantRoutingInformation(){
        IngestionRoutingInformationInput input = new IngestionRoutingInformationInput();
        input.setDatabaseName("test_tenantId");
        input.setPath("http://test-path:8086");
        TenantRoutes tenantRoutes = new TenantRoutes(input, list);
        tenantRoutes.setTenantId("test_tenantId");
        when(routingInformationRepository.save(any(TenantRoutes.class)))
                .thenReturn(tenantRoutes);

        TenantRoutes routingInfo = routingService.setIngestionRoutingInformation(
                "test_tenantId", new IngestionRoutingInformationInput());

        Assert.assertEquals("http://test-path:8086", routingInfo.getRoutes().get("FULL").getPath());
        Assert.assertEquals("test_tenantId", routingInfo.getTenantId());
        Assert.assertEquals(10000, routingInfo.getRoutes().get("FULL").getMaxSeriesCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_setIngestionRoutingInformation_emptyTenantId_throwsIllegalArgumentException(){
        Assert.assertNull(routingService.setIngestionRoutingInformation(
                "", new IngestionRoutingInformationInput()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_setIngestionRoutingInformation_nullTenantId_throwsIllegalArgumentException(){
        Assert.assertNull(routingService.setIngestionRoutingInformation(
                null, new IngestionRoutingInformationInput()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_setIngestionRoutingInformation_whitespacedTenantId_throwsIllegalArgumentException(){
        Assert.assertNull(routingService.setIngestionRoutingInformation(
                "  ", new IngestionRoutingInformationInput()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_setIngestionRoutingInformation_nullTenantInfo_throwsIllegalArgumentException(){
        //Assert.assertNull(routingService.setIngestionRoutingInformation("test", null));
    }

    @Test
    public void test_getIngestionRoutingInformation_validInput_returnsIngestionRoutingInformationOutput(){
        IngestionRoutingInformationInput input = new IngestionRoutingInformationInput();
        input.setDatabaseName("test_tenantId");
        input.setPath("http://test-path:8086");

        TenantRoutes routes = new TenantRoutes(input, list);
        routes.setTenantId("test_tenantId");


        when(routingInformationRepository.findById("test_tenantId"))
                .thenReturn(Optional.ofNullable(routes));

        TenantRoutes routingInfo =
                routingService.getIngestionRoutingInformation("test_tenantId");

        Assert.assertEquals("http://test-path:8086", routingInfo.getRoutes().get("FULL").getPath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getIngestionRoutingInformation_emptyTenantId_throwsIllegalArgumentException(){
        Assert.assertNull(routingService.getIngestionRoutingInformation(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getIngestionRoutingInformation_nullTenantId_throwsIllegalArgumentException(){
        Assert.assertNull(routingService.getIngestionRoutingInformation(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getIngestionRoutingInformation_whitespacedTenantId_throwsIllegalArgumentException(){
        Assert.assertNull(routingService.getIngestionRoutingInformation("  "));
    }

    @Test
    public void test_getIngestionRoutingInformation_wrongTenantId_returnsNullIngestionRoutingInformationOutput(){
        TenantRoutes tenantRoutingInformation = null;

        when(routingInformationRepository.findById("test_tenantId"))
                .thenReturn(Optional.ofNullable(tenantRoutingInformation));

        TenantRoutes routingInfo =
                routingService.getIngestionRoutingInformation("test_tenantId");

        Assert.assertNull(routingInfo);
    }
}
