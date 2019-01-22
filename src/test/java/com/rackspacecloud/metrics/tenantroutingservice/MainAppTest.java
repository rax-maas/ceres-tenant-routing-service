package com.rackspacecloud.metrics.tenantroutingservice;

import org.junit.Test;

public class MainAppTest {
    @Test
    public void applicationContextTest() {
        // There is no assert here. It's mainly to test applicationContext loading.
        TenantRoutingServiceApplication.main(new String[] {});
    }
}
