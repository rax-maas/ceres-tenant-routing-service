package com.rackspacecloud.metrics.tenantroutingservice;

import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationInput;
import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationOutput;
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

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    /*@Test
    public void test_setIngestionRoutingInformation_validInput_returnsTenantRoutingInformation(){
        IngestionRoutingInformationInput input = new IngestionRoutingInformationInput();
        input.setDatabaseName("test_tenantId");
        input.setPath("http://test-path:8086");
        TenantRoutingInformation tenantRoutingInformation = new TenantRoutingInformation(input);
        tenantRoutingInformation.setTenantId("test_tenantId");
        when(routingInformationRepository.save(any(TenantRoutingInformation.class)))
                .thenReturn(tenantRoutingInformation);

        TenantRoutingInformation routingInfo = routingService.setIngestionRoutingInformation(
                "test_tenantId", new IngestionRoutingInformationInput());

        Assert.assertEquals("http://test-path:8086", routingInfo.getIngestionPath());
        Assert.assertEquals("test_tenantId", routingInfo.getTenantId());
        //Assert.assertEquals(10000, routingInfo.getMaxSeriesCount());
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
        Assert.assertNull(routingService.setIngestionRoutingInformation("test", null));
    }

    @Test
    public void test_getIngestionRoutingInformation_validInput_returnsIngestionRoutingInformationOutput(){
        IngestionRoutingInformationInput input = new IngestionRoutingInformationInput();
        input.setDatabaseName("test_tenantId");
        input.setPath("http://test-path:8086");

        TenantRoutingInformation tenantRoutingInformation = new TenantRoutingInformation(input);
        tenantRoutingInformation.setTenantId("test_tenantId");


        when(routingInformationRepository.findById("test_tenantId"))
                .thenReturn(Optional.ofNullable(tenantRoutingInformation));

        IngestionRoutingInformationOutput routingInfo =
                routingService.getIngestionRoutingInformation("test_tenantId");

        Assert.assertEquals("http://test-path:8086", routingInfo.getPath());
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
        TenantRoutingInformation tenantRoutingInformation = null;

        when(routingInformationRepository.findById("test_tenantId"))
                .thenReturn(Optional.ofNullable(tenantRoutingInformation));

        IngestionRoutingInformationOutput routingInfo =
                routingService.getIngestionRoutingInformation("test_tenantId");

        Assert.assertNull(routingInfo);
    }*/
}
