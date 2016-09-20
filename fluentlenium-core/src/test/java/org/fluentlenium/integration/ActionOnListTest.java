package org.fluentlenium.integration;

import org.fluentlenium.integration.localtest.IntegrationFluentTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ActionOnListTest extends IntegrationFluentTest {

    @Test
    public void checkFillAction() {
        goTo(DEFAULT_URL);
        $("input[type=text]").write("zzz");

        assertThat($("input[type=text]").values()).contains("zzz");
    }

    @Test
    public void checkClearAction() {
        goTo(DEFAULT_URL);
        assertThat($("#name").values()).contains("John");
        $("#name").clear();
        assertThat($("#name").values()).contains("");
    }

    @Test
    public void checkClickAction() {
        goTo(DEFAULT_URL);
        assertThat(title()).contains("Selenium");
        $("#linkToPage2").click();
        assertThat(title()).isEqualTo("Page 2");
    }


    @Test
    public void checkTextAction() {
        goTo(DEFAULT_URL);
        assertThat($("#name").values()).contains("John");
        assertThat($(".small").texts()).contains("Small 1", "Small 2", "Small 3");
    }

    @Test
    public void checkFillFileInput() {
        goTo(DEFAULT_URL);
        $("#fileUpload").fill().with("/data/fileName");
        assertThat($("#fileUpload").value()).endsWith("fileName");
    }

    @Test
    public void checkFillFileInputUpperCase() {
        goTo(DEFAULT_URL);
        $("#fileUpload2").fill().with("/data/fileName");
        assertThat($("#fileUpload2").value()).endsWith("fileName");
    }
}
