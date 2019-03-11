package com.rackspacecloud.metrics.tenantroutingservice;

import com.rackspacecloud.metrics.tenantroutingservice.domain.RetentionPolicyEnum;
import com.rackspacecloud.metrics.tenantroutingservice.domain.TenantRoutes;
import com.rackspacecloud.metrics.tenantroutingservice.exceptions.RouteConflictException;
import com.rackspacecloud.metrics.tenantroutingservice.exceptions.RouteDeleteException;
import com.rackspacecloud.metrics.tenantroutingservice.exceptions.RouteNotFoundException;
import com.rackspacecloud.metrics.tenantroutingservice.exceptions.RouteWriteException;
import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationInput;
import com.rackspacecloud.metrics.tenantroutingservice.repositories.ITenantRoutingInformationRepository;
import com.rackspacecloud.metrics.tenantroutingservice.services.RoutingServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RoutingServiceImplUnitTest {

    @Mock
    private ITenantRoutingInformationRepository routingInformationRepository;

    @InjectMocks
    private RoutingServiceImpl routingServiceImpl;

    private List<RetentionPolicyEnum> list;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        list = new ArrayList<>();
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
        TenantRoutes tenantRoutes = new TenantRoutes("test_tenantId", input, list);
        when(routingInformationRepository.save(any(TenantRoutes.class)))
                .thenReturn(tenantRoutes);

        TenantRoutes routingInfo = routingServiceImpl.setIngestionRoutingInformation(
                "test_tenantId", input);

        Assert.assertEquals("http://test-path:8086", routingInfo.getRoutes().get("full").getPath());
        Assert.assertEquals("test_tenantId", routingInfo.getTenantIdAndMeasurement());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_setIngestionRoutingInformation_emptyTenantId_throwsIllegalArgumentException(){
        Assert.assertNull(routingServiceImpl.setIngestionRoutingInformation(
                "", new IngestionRoutingInformationInput()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_setIngestionRoutingInformation_nullTenantId_throwsIllegalArgumentException(){
        Assert.assertNull(routingServiceImpl.setIngestionRoutingInformation(
                null, new IngestionRoutingInformationInput()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_setIngestionRoutingInformation_whitespacedTenantId_throwsIllegalArgumentException(){
        Assert.assertNull(routingServiceImpl.setIngestionRoutingInformation(
                "  ", new IngestionRoutingInformationInput()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_setIngestionRoutingInformation_nullTenantInfo_throwsIllegalArgumentException(){
        IngestionRoutingInformationInput input  = null;
        Assert.assertNull(routingServiceImpl.setIngestionRoutingInformation("test", input));
    }

    @Test
    public void test_getIngestionRoutingInformation_validInput_returnsIngestionRoutingInformationOutput(){
        IngestionRoutingInformationInput input = new IngestionRoutingInformationInput();
        input.setDatabaseName("test_tenantId");
        input.setPath("http://test-path:8086");

        TenantRoutes routes = new TenantRoutes("test_tenantId", input, list);


        when(routingInformationRepository.findById("test_tenantId"))
                .thenReturn(Optional.ofNullable(routes));

        TenantRoutes routingInfo =
                routingServiceImpl.getIngestionRoutingInformation("test_tenantId", "test");

            Assert.assertEquals("http://test-path:8086", routingInfo.getRoutes().get("full").getPath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getIngestionRoutingInformation_emptyTenantId_throwsIllegalArgumentException(){
        Assert.assertNull(routingServiceImpl.getIngestionRoutingInformation("", ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getIngestionRoutingInformation_nullTenantId_throwsIllegalArgumentException(){
        Assert.assertNull(routingServiceImpl.getIngestionRoutingInformation(null, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getIngestionRoutingInformation_whitespacedTenantId_throwsIllegalArgumentException(){
        Assert.assertNull(routingServiceImpl.getIngestionRoutingInformation("  ", "  "));
    }

    @Test(expected = RouteNotFoundException.class)
    public void test_getIngestionRoutingInformation_wrongTenantId_throwsRouteNotFoundException(){
        TenantRoutes tenantRoutingInformation = null;

        when(routingInformationRepository.findById("test_tenantId"))
                .thenReturn(Optional.ofNullable(tenantRoutingInformation));

        TenantRoutes routingInfo =
                routingServiceImpl.getIngestionRoutingInformation("test_tenantId", "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_removeIngestionRoutingInformation_emptyTenantId_throwsIllegalArgumentException(){
        routingServiceImpl.removeIngestionRoutingInformation("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_removeIngestionRoutingInformation_nullTenantId_throwsIllegalArgumentException(){
        routingServiceImpl.getIngestionRoutingInformation(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_removeIngestionRoutingInformation_whitespacedTenantId_throwsIllegalArgumentException(){
        routingServiceImpl.getIngestionRoutingInformation("  ", "  ");
    }

    @Test
    public void test_removeIngestionRoutingInformation_validTenantId_successfullyDeletesRoutes(){
        doNothing().when(routingInformationRepository).deleteById(anyString());

        routingServiceImpl.removeIngestionRoutingInformation("dummy");

        // Verify that mocked method is called once and only once
        verify(routingInformationRepository, times(1)).deleteById("dummy");
    }

    @Test(expected = RouteDeleteException.class)
    public void test_removeIngestionRoutingInformation_exceptionFromRoutingInformationRepository_throwsRouteDeleteException(){
        doThrow(IllegalArgumentException.class).when(routingInformationRepository).deleteById(anyString());

        routingServiceImpl.removeIngestionRoutingInformation("dummy");
    }

    @Test(expected = RouteWriteException.class)
    public void test_setIngestionRoutingInformation_exceptionFromRoutingInformationRepository_throwsRouteWriteException(){
        doThrow(IllegalArgumentException.class).when(routingInformationRepository).save(any());

        routingServiceImpl.setIngestionRoutingInformation("dummy", new IngestionRoutingInformationInput());
    }

    @Test(expected = RouteConflictException.class)
    public void test_setIngestionRoutingInformation_existingRoutingInformation_throwsRouteConflictException(){
        when(routingInformationRepository.findById(anyString())).thenReturn(Optional.of(new TenantRoutes()));
        TenantRoutes routingInfo = routingServiceImpl.setIngestionRoutingInformation(
                "test_tenantId", new IngestionRoutingInformationInput());
    }
}
