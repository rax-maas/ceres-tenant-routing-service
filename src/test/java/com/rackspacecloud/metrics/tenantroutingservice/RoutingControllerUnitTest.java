package com.rackspacecloud.metrics.tenantroutingservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rackspacecloud.metrics.tenantroutingservice.controllers.GlobalExceptionHandler;
import com.rackspacecloud.metrics.tenantroutingservice.controllers.RoutingController;
import com.rackspacecloud.metrics.tenantroutingservice.domain.RetentionPolicyEnum;
import com.rackspacecloud.metrics.tenantroutingservice.domain.TenantRoutes;
import com.rackspacecloud.metrics.tenantroutingservice.exceptions.RouteWriteException;
import com.rackspacecloud.metrics.tenantroutingservice.model.IngestionRoutingInformationInput;
import com.rackspacecloud.metrics.tenantroutingservice.services.RoutingServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebAppConfiguration
@WebMvcTest(value = RoutingController.class)
public class RoutingControllerUnitTest {
    private MockMvc mockMvc;

    @MockBean
    private RoutingServiceImpl routingServiceImpl;

    @InjectMocks
    RoutingController controller;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    private TenantRoutes getTenantRoutes() {
        IngestionRoutingInformationInput input = new IngestionRoutingInformationInput();
        input.setDatabaseName("test_tenantId");
        input.setPath("http://test-path:8086");
        List<RetentionPolicyEnum> list = new ArrayList<>();
        list.add(RetentionPolicyEnum.FULL);
        list.add(RetentionPolicyEnum.FIVE_MINUTES);
        list.add(RetentionPolicyEnum.TWENTY_MINUTES);
        list.add(RetentionPolicyEnum.ONE_HOUR);
        list.add(RetentionPolicyEnum.FOUR_HOURS);
        list.add(RetentionPolicyEnum.ONE_DAY);

        return new TenantRoutes("test_tenantId", input, list);
    }

    @Test
    public void test_getTenantRoutingInformation_validInput_returnsTenantRoutes() throws Exception {
        TenantRoutes output = getTenantRoutes();

        when(routingServiceImpl.getIngestionRoutingInformation(anyString(), anyString(), anyBoolean())).thenReturn(output);

        TenantRoutes out = controller.getTenantRoutingInformation(anyString(), anyString(), anyBoolean());

        Assert.assertEquals("http://test-path:8086", out.getRoutes().get("full").getPath());
    }

    @Test
    public void test_GlobalExceptionHandler_postMethod_newTenant_throwsRouteWriteException() throws Exception {
        doThrow(RouteWriteException.class).when(routingServiceImpl)
                .getIngestionRoutingInformation(anyString(), anyString(), anyBoolean());

        IngestionRoutingInformationInput input = new IngestionRoutingInformationInput();
        input.setDatabaseName("test_database");
        input.setPath("http://test-path:8086");

        ObjectMapper mapper = new ObjectMapper();

        this.mockMvc.perform(get("/dummy_tenantId/measurement?readOnly=true")
                .content(mapper.writeValueAsString(input)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("{\"message\":null,\"rootCause\":null}"));
    }

    @Test
    public void test_postMethod_newTenant_defaultValue() throws Exception {
        TenantRoutes output = getTenantRoutes();

        when(routingServiceImpl.getIngestionRoutingInformation(anyString(), anyString(), eq(false))).thenReturn(output);

        TenantRoutes out = controller.getTenantRoutingInformation(anyString(), anyString(), eq(false));

        Assert.assertEquals("http://test-path:8086", out.getRoutes().get("full").getPath());
    }
}
