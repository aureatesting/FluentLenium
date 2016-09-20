package org.fluentlenium.core.conditions;

import com.google.common.base.Predicates;
import org.fluentlenium.adapter.FluentAdapter;
import org.fluentlenium.core.components.DefaultComponentInstantiator;
import org.fluentlenium.core.domain.FluentWebElement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WebElementConditionsTest {

    @Mock
    private WebElement webElement;

    @Mock
    private WebDriver webDriver;

    private FluentWebElement fluentWebElement;
    private WebElementConditions conditions;

    private FluentAdapter fluentAdapter;

    @Before
    public void before() {
        fluentAdapter = new FluentAdapter(webDriver);

        fluentWebElement = new FluentWebElement(webElement, fluentAdapter, new DefaultComponentInstantiator(fluentAdapter));
        conditions = new WebElementConditions(fluentWebElement);
    }

    @After
    public void after() {
        reset(webElement);
    }

    @Test
    public void isVerified() {
        assertThat(conditions.isVerified(Predicates.<FluentWebElement>alwaysTrue())).isTrue();
        assertThat(conditions.isVerified(Predicates.<FluentWebElement>alwaysFalse())).isFalse();

        assertThat(conditions.not().isVerified(Predicates.<FluentWebElement>alwaysTrue())).isFalse();
        assertThat(conditions.not().isVerified(Predicates.<FluentWebElement>alwaysFalse())).isTrue();
    }

    @Test
    public void isClickable() {
        assertThat(conditions.isClickable()).isFalse();

        when(webElement.isEnabled()).thenReturn(true);
        when(webElement.isDisplayed()).thenReturn(true);

        assertThat(conditions.isClickable()).isTrue();
    }

    @Test
    public void isStale() {
        assertThat(conditions.isStale()).isFalse();

        // Selenium invokes isEnabled to check staleness.
        when(webElement.isEnabled()).thenThrow(StaleElementReferenceException.class);

        assertThat(conditions.isStale()).isTrue();
    }

    @Test
    public void isEnabled() {
        assertThat(conditions.isEnabled()).isFalse();

        when(webElement.isEnabled()).thenReturn(true);

        assertThat(conditions.isEnabled()).isTrue();
    }

    @Test
    public void isDisplayed() {
        assertThat(conditions.isDisplayed()).isFalse();

        when(webElement.isDisplayed()).thenReturn(true);

        assertThat(conditions.isDisplayed()).isTrue();
    }

    @Test
    public void isSelected() {
        assertThat(conditions.isSelected()).isFalse();

        when(webElement.isSelected()).thenReturn(true);

        assertThat(conditions.isSelected()).isTrue();
    }

    @Test
    public void hasText() {
        when(webElement.getText()).thenReturn("Some Text");

        assertThat(conditions.text().equals("Some Text")).isTrue();
        assertThat(conditions.text().equals("Other Text")).isFalse();
    }

    @Test
    public void containsText() {
        when(webElement.getText()).thenReturn("Some Text");

        assertThat(conditions.text().contains("Te")).isTrue();
        assertThat(conditions.text().contains("Other")).isFalse();
    }

    @Test
    public void hasAttribute() {
        assertThat(conditions.hasAttribute("attr", "value")).isFalse();

        when(webElement.getAttribute("attr")).thenReturn("value");

        assertThat(conditions.hasAttribute("attr", "value")).isTrue();
    }

    @Test
    public void hasId() {
        assertThat(conditions.hasId("value")).isFalse();

        when(webElement.getAttribute("id")).thenReturn("value");

        assertThat(conditions.hasId("value")).isTrue();
    }

    @Test
    public void hasName() {
        assertThat(conditions.hasName("value")).isFalse();

        when(webElement.getAttribute("name")).thenReturn("value");

        assertThat(conditions.hasName("value")).isTrue();
    }

}
