package org.fluentlenium.integration;

import org.fluentlenium.integration.localtest.IntegrationFluentTest;
import org.junit.Test;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.assertj.core.api.Assertions.assertThat;

public class FluentabilityTest extends IntegrationFluentTest {

    @Test
    public void checkIsEnabled() {
        goTo(DEFAULT_URL);
        await().atMost(1, NANOSECONDS).until(".small").with("name").equalTo("name").present();

        assertThat(find("input").first().enabled()).isTrue();
    }

}
