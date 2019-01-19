package com.rackspacecloud.metrics.tenantroutingservice;

import com.rackspacecloud.metrics.tenantroutingservice.controllers.RoutingController;
import com.rackspacecloud.metrics.tenantroutingservice.domain.RetentionPolicyEnum;
import com.rackspacecloud.metrics.tenantroutingservice.domain.TenantRoutes;
import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationInput;
import com.rackspacecloud.metrics.tenantroutingservice.services.RoutingServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(value = RoutingController.class)
public class RoutingControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoutingServiceImpl routingServiceImpl;

    @InjectMocks
    RoutingController controller;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_setTenantRoutingInformation_validInput_returnsIngestionRoutingInformationOutput(){
        IngestionRoutingInformationInput input = new IngestionRoutingInformationInput();
        input.setDatabaseName("test_tenantId");
        input.setPath("http://test-path:8086");
        List<RetentionPolicyEnum> list = new LinkedList();
        list.add(RetentionPolicyEnum.FULL);
        list.add(RetentionPolicyEnum.FIVE_MINUTES);
        list.add(RetentionPolicyEnum.TWENTY_MINUTES);
        list.add(RetentionPolicyEnum.ONE_HOUR);
        list.add(RetentionPolicyEnum.FOUR_HOURS);
        list.add(RetentionPolicyEnum.ONE_DAY);

        TenantRoutes tenantRoutingInformation = new TenantRoutes("test_tenantId", input, list);


        when(routingServiceImpl.setIngestionRoutingInformation(anyString(), any(IngestionRoutingInformationInput.class)))
                .thenReturn(tenantRoutingInformation);

        TenantRoutes out = controller.setTenantRoutingInformation("test",
                new IngestionRoutingInformationInput());

        Assert.assertEquals("http://test-path:8086", out.getRoutes().get("full").getPath());
    }

    /*
    This test is commented out because it is expecting the data to exist when it is run but there is no function to add
    the data for this test (unless the tests were to run in order, which is not guaranteed). So this test needs to be fixed
    with setup data or needs to have things setup some other way.

    @Test
    public void test_getTenantRoutingInformation_validInput_returnsIngestionRoutingInformationOutput(){
        TenantRoutes output = new TenantRoutes();
        output.setTenantId("test_tenantId");

        when(routingService.getIngestionRoutingInformation(anyString())).thenReturn(output);

        Assert.assertEquals("http://test-path:8086", output.getRoutes().get("full").getPath());
    }*/
}
