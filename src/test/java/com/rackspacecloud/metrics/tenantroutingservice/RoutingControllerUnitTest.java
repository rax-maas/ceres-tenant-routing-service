package com.rackspacecloud.metrics.tenantroutingservice;

import com.rackspacecloud.metrics.tenantroutingservice.controllers.RoutingController;
import com.rackspacecloud.metrics.tenantroutingservice.domain.TenantRoutingInformation;
import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationInput;
import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationOutput;
import com.rackspacecloud.metrics.tenantroutingservice.services.RoutingService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(value = RoutingController.class)
public class RoutingControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoutingService routingService;

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
        TenantRoutingInformation tenantRoutingInformation = new TenantRoutingInformation(input);
        tenantRoutingInformation.setTenantId("test_tenantId");


        when(routingService.setIngestionRoutingInformation(anyString(), any(IngestionRoutingInformationInput.class)))
                .thenReturn(tenantRoutingInformation);

        IngestionRoutingInformationOutput out = controller.setTenantRoutingInformation("test",
                new IngestionRoutingInformationInput());

        Assert.assertEquals("http://test-path:8086", out.getPath());
    }

    @Test
    public void test_getTenantRoutingInformation_validInput_returnsIngestionRoutingInformationOutput(){
        IngestionRoutingInformationOutput output = new IngestionRoutingInformationOutput();
        output.setPath("http://test-path:8086");

        when(routingService.getIngestionRoutingInformation(anyString())).thenReturn(output);

        Assert.assertEquals("http://test-path:8086", output.getPath());
    }
}
