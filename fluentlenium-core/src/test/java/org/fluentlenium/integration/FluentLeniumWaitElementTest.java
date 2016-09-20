package org.fluentlenium.integration;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import org.fluentlenium.core.FluentControl;
import org.fluentlenium.core.FluentPage;
import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.domain.FluentWebElement;
import org.fluentlenium.integration.localtest.IntegrationFluentTest;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.FindBy;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class FluentLeniumWaitElementTest extends IntegrationFluentTest {
    @Before
    public void before() {
        goTo(DEFAULT_URL);
    }

    @FindBy(className = "small")
    private FluentList<FluentWebElement> smallElements;

    @FindBy(className = "not-found")
    private FluentList<FluentWebElement> notFoundElements;

    @FindBy(tagName = "input")
    private FluentList<FluentWebElement> inputElement;

    @FindBy(tagName = "input")
    private FluentList<FluentWebElement> inputElements;

    @FindBy(css = "input[disabled]")
    private FluentWebElement inputDisabledElement;

    @FindBy(css = "input[disabled]")
    private FluentList<FluentWebElement> inputDisabledElements;

    @FindBy(className = "not-found")
    private FluentWebElement notFoundElement;

    @FindBy(id = "default")
    private FluentWebElement defaultElement;

    @FindBy(id = "default")
    private FluentList<FluentWebElement> defaultElements;

    @FindBy(id = "unvisible")
    private FluentList<FluentWebElement> unvisibleElements;

    @FindBy(id = "nonexistent")
    private FluentWebElement nonexistentElement;

    @FindBy(id = "nonexistent")
    private FluentList<FluentWebElement> nonexistentElements;

    @FindBy(id = "disabled")
    private FluentList<FluentWebElement> disabledElements;

    @FindBy(id = "disabled")
    private FluentWebElement disabledElement;

    @Test
    public void checkAwaitIsPresent() {
        await().atMost(1, NANOSECONDS).until(smallElements).isPresent();
    }

    @Test
    public void checkAwaitIsClickable() {
        await().atMost(1, NANOSECONDS).until(smallElements).isClickable();
    }

    @Test
    public void checkAwaitIsClickableForSingleElement() {
        await().atMost(1, NANOSECONDS).until(inputElement).isClickable();
    }

    @Test(expected = TimeoutException.class)
    public void checkAwaitDisabledIsClickableThrowTimeoutException() {
        await().atMost(1, NANOSECONDS).until(inputDisabledElements).isClickable();
    }

    @Test(expected = TimeoutException.class)
    public void checkAwaitDisabledIsClickableThrowTimeoutExceptionForSingleElement() {
        await().atMost(1, NANOSECONDS).until(inputDisabledElement).isClickable();
    }

    @Test
    public void awaitForElementXPosition() {
        await().until(inputDisabledElements).hasRectangle().withX(5);
    }

    @Test(expected = TimeoutException.class)
    public void awaitForElementXPositionElementNotFound() {
        await().until(notFoundElements).hasRectangle().withX(0);
    }

    @Test
    public void checkAwaitHasSize() {
        await().atMost(1, NANOSECONDS).until(smallElements).each().hasSize(3);
    }

    @Test
    public void checkUseCustomMessage() {
        try {
            await().withMessage("toto").atMost(1, NANOSECONDS).until(notFoundElement).isPresent();
            fail();
        } catch (TimeoutException e) {
            assertThat(e.getMessage()).contains("toto");
        }
    }

    @Test
    public void checkAwaitContainsText() {
        await().atMost(1, NANOSECONDS).until(smallElements).text().contains("Small 1");
    }

    @Test
    public void checkAwaitHasText() {
        await().atMost(1, NANOSECONDS).until(smallElements).text().equals("Small 1");
    }

    @Test
    public void checkAwaitContainsName() {
        await().atMost(1, NANOSECONDS).until(smallElements).hasName("name");
    }

    @Test
    public void checkAwaitContainsId() {
        await().atMost(1, NANOSECONDS).until(smallElements).hasId("id2");
    }

    @Test
    public void checkHasAttribute() {
        await().atMost(1, NANOSECONDS).until(inputElements).hasAttribute("value", "John");
    }

    @Test
    public void when_element_is_present_then_areDisplayed_return_true() {
        goTo(JAVASCRIPT_URL);
        await().atMost(1, NANOSECONDS).until(defaultElements).each().isDisplayed();
    }

    @Test
    public void when_element_is_present_then_isDisplayed_return_true() {
        goTo(JAVASCRIPT_URL);
        await().atMost(1, NANOSECONDS).until(defaultElements).isDisplayed();
    }

    @Test(expected = TimeoutException.class)
    public void when_element_is_not_displayed_then_areDisplayed_throws_exception() {
        goTo(JAVASCRIPT_URL);
        await().atMost(1, NANOSECONDS).until(unvisibleElements).each().isDisplayed();
    }

    @Test(expected = TimeoutException.class)
    public void when_element_is_not_displayed_then_isDisplayed_throws_exception() {
        goTo(JAVASCRIPT_URL);
        await().atMost(1, NANOSECONDS).until(unvisibleElements).isDisplayed();
    }

    @Test
    public void when_element_is_not_present_then_areNotDisplayed_return_true() {
        goTo(JAVASCRIPT_URL);
        await().atMost(1, NANOSECONDS).until(nonexistentElements).each().not().isDisplayed();
    }

    @Test
    public void when_element_is_not_present_then_isNotDisplayed_return_true() {
        goTo(JAVASCRIPT_URL);
        await().atMost(1, NANOSECONDS).until(nonexistentElements).not().isDisplayed();
    }

    @Test
    public void when_element_is_not_displayed_then_areNotDisplayed_return_true() {
        goTo(JAVASCRIPT_URL);
        await().atMost(1, NANOSECONDS).until(unvisibleElements).each().not().isDisplayed();
    }

    @Test
    public void when_element_is_not_displayed_then_isNotDisplayed_return_true() {
        goTo(JAVASCRIPT_URL);
        await().atMost(1, NANOSECONDS).until(unvisibleElements).not().isDisplayed();
    }

    @Test(expected = TimeoutException.class)
    public void when_element_is_displayed_then_areNotDisplayed_throws_exception() {
        goTo(JAVASCRIPT_URL);
        await().atMost(1, NANOSECONDS).until(defaultElements).each().not().isDisplayed();
    }

    @Test(expected = TimeoutException.class)
    public void when_element_is_displayed_then_isNotDisplayed_throws_exception() {
        goTo(JAVASCRIPT_URL);
        await().atMost(1, NANOSECONDS).until(defaultElements).not().isDisplayed();
    }

    @Test
    public void when_element_is_enabled_then_areEnabled_return_true() {
        goTo(JAVASCRIPT_URL);
        await().atMost(1, NANOSECONDS).until(defaultElements).each().isEnabled();
    }

    @Test
    public void when_element_is_enabled_then_isEnabled_return_true() {
        goTo(JAVASCRIPT_URL);
        await().atMost(1, NANOSECONDS).until(defaultElements).isEnabled();
    }

    @Test
    public void when_single_element_is_enabled_then_isEnabled_return_true() {
        goTo(JAVASCRIPT_URL);
        await().atMost(1, NANOSECONDS).until(defaultElement).isEnabled();
    }

    @Test(expected = TimeoutException.class)
    public void when_single_nonexisting_element_then_isEnabled_throws_exception() {
        goTo(JAVASCRIPT_URL);
        await().atMost(1, NANOSECONDS).until(nonexistentElement).isEnabled();
    }

    @Test(expected = TimeoutException.class)
    public void when_element_is_not_enabled_then_areEnabled_throws_exception() {
        goTo(JAVASCRIPT_URL);
        await().atMost(1, NANOSECONDS).until(disabledElements).each().isEnabled();
    }

    @Test(expected = TimeoutException.class)
    public void when_element_is_not_enabled_then_isEnabled_throws_exception() {
        goTo(JAVASCRIPT_URL);
        await().atMost(1, NANOSECONDS).until(disabledElements).isEnabled();
    }

    @Test
    public void when_element_is_not_enabled_then_isNotEnabled_return_true() {
        goTo(JAVASCRIPT_URL);
        await().atMost(1, NANOSECONDS).until(disabledElements).not().isEnabled();
    }

    @Test(expected = TimeoutException.class)
    public void when_single_element_is_not_enabled_then_isEnabled_throws_exception() {
        goTo(JAVASCRIPT_URL);
        await().atMost(1, NANOSECONDS).until(disabledElement).isEnabled();
    }

    @Test
    public void when_single_element_is_not_enabled_then_isNotEnabled_return_true() {
        goTo(JAVASCRIPT_URL);
        await().atMost(1, NANOSECONDS).until(disabledElement).not().isEnabled();
    }

    @Test
    public void when_element_is_not_displayed_then_isPresent_return_true() {
        goTo(JAVASCRIPT_URL);
        await().atMost(1, NANOSECONDS).until(unvisibleElements).isPresent();
    }

    @Test(expected = TimeoutException.class)
    public void checkPolling() {
        goTo(JAVASCRIPT_URL);
        await().pollingEvery(1500, TimeUnit.MILLISECONDS).until(defaultElements).text().equals("wait");
    }

    @Test
    public void checkIsAt() {
        goTo(JAVASCRIPT_URL);
        await().pollingEvery(1000, TimeUnit.MILLISECONDS).untilPage(new FluentPage() {
            @Override
            public void isAt() {
            }
        }).isAt();
    }

    @Test
    public void checkLoaded() {
        goTo(JAVASCRIPT_URL);
        await().pollingEvery(1000, TimeUnit.MILLISECONDS).untilPage().isLoaded();
    }

    @Test
    public void checkPredicate() {
        goTo(JAVASCRIPT_URL);
        await().pollingEvery(800, TimeUnit.MILLISECONDS).untilPredicate(new Predicate<FluentControl>() {
            @Override
            public boolean apply(FluentControl o) {
                return true;
            }
        });
    }

    @Test(expected = TimeoutException.class)
    public void checkPredicateFail() {
        goTo(JAVASCRIPT_URL);
        await().atMost(1000).untilPredicate(new Predicate<FluentControl>() {
            @Override
            public boolean apply(FluentControl o) {
                return false;
            }
        });
    }

    @Test
    public void checkFunction() {
        goTo(JAVASCRIPT_URL);
        await().pollingEvery(1000, TimeUnit.MILLISECONDS).until(new Function<FluentControl, Boolean>() {
            @Override
            public Boolean apply(FluentControl fluent) {
                return true;
            }
        });
    }

    @Test(expected = TimeoutException.class)
    public void checkFunctionFail() {
        goTo(JAVASCRIPT_URL);
        await().atMost(1000).until(new Function<FluentControl, Boolean>() {
            @Override
            public Boolean apply(FluentControl fluent) {
                return false;
            }
        });
    }

    @Test
    public void checkSupplier() {
        goTo(JAVASCRIPT_URL);
        await().pollingEvery(1000, TimeUnit.MILLISECONDS).until(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return true;
            }
        });
    }

    @Test(expected = TimeoutException.class)
    public void checkSupplierFail() {
        goTo(JAVASCRIPT_URL);
        await().atMost(1000).until(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return false;
            }
        });
    }

    private static class MyFluentPage extends FluentPage {
        @Override
        public void isAt() {
            assertThat(find("#newField").getTexts()).contains("new");
        }

        @Override
        public String getUrl() {
            return IntegrationFluentTest.JAVASCRIPT_URL;
        }
    }

}


