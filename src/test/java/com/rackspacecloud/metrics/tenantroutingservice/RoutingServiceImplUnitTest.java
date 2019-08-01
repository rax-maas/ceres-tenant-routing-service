package com.rackspacecloud.metrics.tenantroutingservice;

import com.rackspacecloud.metrics.tenantroutingservice.domain.RetentionPolicyEnum;
import com.rackspacecloud.metrics.tenantroutingservice.domain.TenantMeasurements;
import com.rackspacecloud.metrics.tenantroutingservice.domain.TenantRoutes;
import com.rackspacecloud.metrics.tenantroutingservice.exceptions.MeasurementNotFoundException;
import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationInput;
import com.rackspacecloud.metrics.tenantroutingservice.repositories.ITenantMeasurementRepository;
import com.rackspacecloud.metrics.tenantroutingservice.repositories.ITenantRoutingInformationRepository;
import com.rackspacecloud.metrics.tenantroutingservice.services.RoutingServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RoutingServiceImplUnitTest {

    @Mock
    private ITenantRoutingInformationRepository routingInformationRepository;

    @Mock
    private ITenantMeasurementRepository tenantMeasurementRepository;

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
    public void test_getIngestionRoutingInformation_forExistingRoute_validInput_returnsIngestionRoutingInformationOutput() {
        IngestionRoutingInformationInput input = new IngestionRoutingInformationInput();
        input.setDatabaseName("test_tenantId");
        input.setPath("http://test-path:8086");

        TenantRoutes routes = new TenantRoutes("test_tenantId", input, list);

        RoutingServiceImpl routingServiceImplLocal = new RoutingServiceImpl(null,
                routingInformationRepository, null, null);

        when(routingInformationRepository.findById("test_tenantId:test")).thenReturn(Optional.ofNullable(routes));

        TenantRoutes routingInfo =
                routingServiceImplLocal.getIngestionRoutingInformation("test_tenantId", "test", false);

            Assert.assertEquals("http://test-path:8086", routingInfo.getRoutes().get("full").getPath());
    }

    /**
     * This test case is for scenario:
     * 1. No routing information is present for the tenantId+Measurement
     * 2. TenantId is present in tenant-measurement table, but we are getting a new measurement for existing tenantId
     */
    @Test
    public void test_getIngestionRoutingInformation_forNonExistingRouteAndExistingTenant_validInput_returnsRoutingInformationOutput() {
        IngestionRoutingInformationInput input = new IngestionRoutingInformationInput();
        input.setDatabaseName("test_tenantId");
        input.setPath("http://test-path:8086");

        TenantMeasurements tenantMeasurements = new TenantMeasurements();
        tenantMeasurements.setTenantId("test_tenantId");
        tenantMeasurements.setMeasurements(new HashSet<>());

        RoutingServiceImpl routingServiceImplLocal = new RoutingServiceImpl(null,
                routingInformationRepository,
                "", tenantMeasurementRepository);

        when(routingInformationRepository.findById("test_tenantId:test")).thenReturn(Optional.ofNullable(null));

        when(tenantMeasurementRepository.findById("test_tenantId")).thenReturn(Optional.ofNullable(tenantMeasurements));

        TenantRoutes routingInfo =
                routingServiceImplLocal.getIngestionRoutingInformation("test_tenantId", "test", false);

        verify(routingInformationRepository, Mockito.times(1))
                .save(any(TenantRoutes.class));
        verify(tenantMeasurementRepository, Mockito.times(1))
                .save(any(TenantMeasurements.class));
    }

    /**
     * This test case is for scenario:
     * 1. No routing information is present for the tenantId+Measurement
     * 2. Also TenantId is new to the system, so there is no record for this tenantId in tenant-measurement table
     */
    @Test
    public void test_getIngestionRoutingInformation_forNonExistingRouteAndNewTenant_validInput_returnsRoutingInformationOutput() {
        IngestionRoutingInformationInput input = new IngestionRoutingInformationInput();
        input.setDatabaseName("test_tenantId");
        input.setPath("http://test-path:8086");

        RoutingServiceImpl routingServiceImplLocal = new RoutingServiceImpl(null,
                routingInformationRepository,
                null, tenantMeasurementRepository);

        when(routingInformationRepository.findById("test_tenantId:test")).thenReturn(Optional.ofNullable(null));

        when(tenantMeasurementRepository.findById("test_tenantId")).thenReturn(Optional.ofNullable(null));

        TenantRoutes routingInfo =
                routingServiceImplLocal.getIngestionRoutingInformation("test_tenantId", "test", false);

        verify(routingInformationRepository, Mockito.times(1))
                .save(any(TenantRoutes.class));
        verify(tenantMeasurementRepository, Mockito.times(1))
                .save(any(TenantMeasurements.class));
    }

    @Test
    public void test_getMeasurements_existingTenantId_successfullyListAllMeasurements(){
        TenantMeasurements tenantMeasurements = new TenantMeasurements();
        tenantMeasurements.setTenantId("test_tenantId");

        Set<String> measurements = new HashSet<>();
        measurements.add("measurement1");
        measurements.add("measurement2");
        tenantMeasurements.setMeasurements(measurements);

        when(tenantMeasurementRepository.findById("test_tenantId")).thenReturn(Optional.ofNullable(tenantMeasurements));


        Collection<String> measurementCollection = routingServiceImpl.getMeasurements("test_tenantId");

        Assert.assertEquals(measurements.size(), measurementCollection.size());
        Assert.assertTrue(measurementCollection.contains("measurement1"));
        Assert.assertTrue(measurementCollection.contains("measurement2"));
    }

    @Test(expected = MeasurementNotFoundException.class)
    public void test_getMeasurements_RepositoryThrowsException_throwsMeasurementNotFoundException(){
        when(tenantMeasurementRepository.findById("test_tenantId"))
                .thenThrow(new MeasurementNotFoundException("test_tenantId", null));
        routingServiceImpl.getMeasurements("test_tenantId");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getIngestionRoutingInformation_emptyTenantId_throwsIllegalArgumentException() throws Exception {
        Assert.assertNull(routingServiceImpl.getIngestionRoutingInformation("", "", false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getIngestionRoutingInformation_nullTenantId_throwsIllegalArgumentException() throws Exception {
        Assert.assertNull(routingServiceImpl.getIngestionRoutingInformation(null, null,false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getIngestionRoutingInformation_whitespacedTenantId_throwsIllegalArgumentException() throws Exception {
        Assert.assertNull(routingServiceImpl.getIngestionRoutingInformation("  ", "  ", false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getMeasurements_emptyTenantId_throwsIllegalArgumentException(){
        routingServiceImpl.getMeasurements("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getMeasurements_allWhiteSpacedTenantId_throwsIllegalArgumentException(){
        routingServiceImpl.getMeasurements("     ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_removeIngestionRoutingInformation_nullTenantId_throwsIllegalArgumentException() throws Exception {
        routingServiceImpl.getIngestionRoutingInformation(null, null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_removeIngestionRoutingInformation_whitespacedTenantId_throwsIllegalArgumentException() throws Exception {
        routingServiceImpl.getIngestionRoutingInformation("  ", "  ", false);
    }
}
