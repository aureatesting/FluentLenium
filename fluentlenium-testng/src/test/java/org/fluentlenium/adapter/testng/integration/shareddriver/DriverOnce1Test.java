package org.fluentlenium.adapter.testng.integration.shareddriver;

import org.fluentlenium.adapter.testng.integration.localtest.IntegrationFluentTestNg;
import org.fluentlenium.configuration.ConfigurationProperties.DriverLifecycle;
import org.fluentlenium.configuration.FluentConfiguration;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.withName;

@FluentConfiguration(driverLifecycle = DriverLifecycle.JVM)
@Test(groups = "DriverOnce1", suiteName = "Once")
public class DriverOnce1Test extends IntegrationFluentTestNg {

    @Test
    public void firstMethod() {
        goTo(IntegrationFluentTestNg.DEFAULT_URL);
        assertThat($(".small", withName("name"))).hasSize(1);
    }


    @Test
    public void secondMethod() {
        assertThat($(".small", withName("name"))).hasSize(1);
    }


}
