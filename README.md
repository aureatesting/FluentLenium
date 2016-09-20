# What is FluentLenium ?

[![Travis](https://img.shields.io/travis/FluentLenium/FluentLenium.svg)](https://travis-ci.org/FluentLenium/FluentLenium)
[![Coveralls](https://img.shields.io/coveralls/FluentLenium/FluentLenium.svg)](https://coveralls.io/github/FluentLenium/FluentLenium)
[![Code Climate](https://codeclimate.com/github/FluentLenium/FluentLenium/badges/gpa.svg)](https://codeclimate.com/github/FluentLenium/FluentLenium)
[![HuBoard](https://img.shields.io/badge/Hu-Board-7965cc.svg)](https://huboard.com/FluentLenium/FluentLenium)

FluentLenium is a framework that helps you write [Selenium](http://seleniumhq.org/) tests.
FluentLenium provides a [fluent interface](http://en.wikipedia.org/wiki/Fluent_interface) to the [Selenium Web Driver](http://www.seleniumhq.org/projects/webdriver/).
FluentLenium integrates with JUnit 4.x, TestNG, Cucumber but can also be used standalone.
FluentLenium lets you use the assertion framework you like, either [JUnit assertions](http://junit.sourceforge.net/javadoc/org/junit/Assert.html), [Hamcrest](http://hamcrest.org/JavaHamcrest/) 
or [AssertJ](http://joel-costigliola.github.io/assertj/).

# Quickstart with JUnit and AssertJ

- Add dependencies to your `pom.xml`.

```xml
<dependency>
    <groupId>org.fluentlenium</groupId>
    <artifactId>fluentlenium-junit</artifactId>
    <version>1.0.0-RC2</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.fluentlenium</groupId>
    <artifactId>fluentlenium-assertj</artifactId>
    <version>1.0.0-RC2</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>htmlunit-driver</artifactId>
    <version>2.21</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>xml-apis</groupId>
    <artifactId>xml-apis</artifactId>
    <version>1.4.01</version>
    <scope>test</scope>
</dependency>
```

- Create a Fluent Test.

```java
import org.fluentlenium.adapter.junit.FluentTest;
import org.fluentlenium.core.hook.wait.Wait;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Wait
public class DuckDuckGoTest extends FluentTest {
    @Test
    public void title_of_duck_duck_go_should_contain_search_query_name() {
        goTo("https://duckduckgo.com");
        $("#search_form_input_homepage").fill().with("FluentLenium");
        $("#search_button_homepage").submit();
        assertThat(title()).contains("FluentLenium");
    }
}
```

- Run as a JUnit test.

[More FluentLenium examples are available on github](https://github.com/FluentLenium/FluentLenium/tree/master/fluentlenium-examples).

## Fluent Test

Fluent Test is the entry point of FluentLenium. You only have to extend ```FluentTest``` and implement a test as usual.

```java
public class MyTest extends FluentTest {
    @Test
    public void testGoogle() {
        goTo("http://www.google.com");
    }
}
```

This example is for JUnit, but you may use other framework the exact same way (See [Supported Test Runners](#supported-test-runner) section).

##  Fluent Locators

FluentLenium provides a `$` method to build **Fluent Locators**.

  - **CSS Selector**: Creates a Fluent Locator from CSS Selector.
  - **Selenium By Locator**: Creates a Fluent Locator with native Selenium API.
  - **Filter**: Add an additional filter to the Fluent Locator.
  - **First/Last/Index**: Get a single element from the Fluent Locator.
  - **XPath Axes**: Get another Fluent Locator from its relative position in the DOM.
  
Fluent Locators are implemented by ```FluentWebElement``` and ```FluentList<FluentWebElement>```. Those classes are 
wrappers of Selenium ```WebElement``` and ```List<WebElement>```.

### CSS Selector

You can use CSS Selector with the same restrictions as in Selenium.

```java
$("#title") // Elements with id named "title"
$(".fluent") // Elements with class named "fluent"
$("input") // Elements with tag name "input"

// Most of CSS3 syntax is supported.
$("form > input[data-custom=selenium]")
```

### Selenium By Locator

You can also use native Selenium By Locator.

```java
$(By.cssSelector(".fluent"))
```

### Filters

You can use Filters to make your Fluent Selection easier to read and more powerful.

```java
// Required import to make filters available
import static org.fluentlenium.core.filter.FilterConstructor.*;
```

```java
$(".fluent", withName("foo"))
$(".fluent", withClass("foo"))
$(".fluent", withId("idOne"))
$(".fluent", withText("This field is mandatory."))
$(withId("idOne")) // Filter only
$(By.cssSelector(".header")) // Native Selium `By` locator
$(".fluent", withName("foo"), withId("id1")) // Filter chaining
```

You can do more complex string matching on the above filters using the following methods:

  - `contains`
  - `containsWord`
  - `notContains`
  - `startsWith`
  - `notStartsWith`
  - `endsWith`
  - `notEndsWith`

```java
$(".fluent", withName().notContains("name"))
$(".fluent", withId().notStartsWith("id"))
$(".fluent", withText().endsWith("Female"))
$(".fluent", withName().contains(regex("na?me[0-9]*")))
$(".fluent", withName().notStartsWith(regex("na?me[0-9]*")))
```

`contains`, `startsWith` and `endsWith` with a regexp pattern look for a subsection of the pattern.

### First, Last and Index
If you want the first, last or a particular index element, just use:

```java
$(".fluent").first() // First element
$(".fluent").last() // Last element
$(".fluent").index(2) // Third element
$(".fluent", withName("foo")).index(2) // Third element named "foo"
```

### Fluent Locator chains
You can also chain the Fluent Locators.

```java
// All the "input" tag name elements 
// inside "fluent" class element subtree.
$(".fluent").$("input")

// The first "input" element named "bar" 
// inside the third "fluent" class named "foo" element.
$(".fluent", withName("foo")).index(2).$("input", withName("bar")).first()
```

### XPath Axes

If you need to build another Fluent Locator from the position in the DOM of an existing one, you 
can use [XPath axes](http://www.w3schools.com/xsl/xpath_axes.asp).

```java
$(".fluent"()).axes().parent()
$(".fluent"()).axes().descendants()
$(".fluent"()).axes().ancestors()
$(".fluent"()).axes().followings()
$(".fluent"()).axes().followingSiblings()
$(".fluent"()).axes().precedings()
$(".fluent"()).axes().precedingSiblings()
```

## Located Elements

### Actions
Fluent Locators have methods to interact with located elements:

```java
// click/double-click on all the enabled elements.
$("#create-button").click()
$("#create-button").doubleClick()

// Clear all the enabled fields
$("#firstname").clear()

// Submit all the enabled forms.
$("#account").submit()

// Place the mouse over the first found element
$("#create-button").mouseOver()
```

### Information

You can also retrieve information of located elements:

```java
// Name of the first element
$(".fluent").getName()

// Id of the first element
$(By.cssSelector(".fluent")).getId()

// Value of the first element
$(".fluent").getValue()

// Tag name of the first element
$(".fluent").getTagName()

// Text of the first element
$(".fluent").getText()

// Text content of the first element (includes hidden parts)
$(".fluent").getTextContent()

// Value of attribute "data-custom" of the first element
$(".fluent").getAttribute("data-custom")

// HTML content of the element
$(".fluent").html()

// Size of the element (width/height)
$(".fluent").getSize()
```

You can also access a list of all the names, visible text, and ids of a list of elements:
```java
$(".fluent").getNames()
$(By.id("foo")).getIds()
$(".fluent").getValues()
$(".fluent").getAttributes("myCustomAttribute")
$(".fluent").getTexts()
$(".fluent").getTextContents()
```

You can also check if the element is displayed, enabled or selected:
```java
$(".fluent").isDisplayed()
$(".fluent").isEnabled()
$(".fluent").isSelected()
```

### Lazy Fluent Locators

`$` always returns Lazy Fluent Locators. Building a Locator doesn't perform the search immediately. 
It will be searched later, just before it's really needed to interact with the located elements. 
(ie: clicking, retrieving text value, send user input).

```java
// Creates the Fluent Locator, but doesn't perform the search
FluentList<FluentWebElement> fluentElements = $(".fluent");

// Perform the search and call click() on found elements
fluentElements.click();

// NoSuchElementException will be throw if no element is found.
```

You can control the state of those Lazy Fluent Locators.

```java
// Check if the element is present in DOM (boolean)
$(".fluent").isPresent();

// Force the underlying search if it's not already loaded
// Throws NoSuchElementException if not found
$(".fluent").now();

// Forget the underlying search results.
// You can then reuse the same locator to perform the search again
$(".fluent").reset();

// Check if the underlying element has been loaded (boolean)
$(".fluent").isLoaded();
```

## Window actions

Window actions are available by window() method in FluentTest class.

We introduced few additional methods in comparison to standard ones inherited from Selenium.

These methods are: clickAndOpenNew, openNewAndSwitch, clickAndCloseCurrent, switchToLast, switchToLast(previousWindowName)

### clickAndOpenNew

You should use this method when click action invoked on element should end up with new window opened.
This is just about waiting for new window to open, this method is not opening new browser window.

### openNewAndSwitch

This method opens new window and switches the context to newly oppened one.

###clickAndCloseCurrent

You should use this method when click action invoked on element should end up with current window closed.
This is just about waiting for new window to close, this method is not closing browser window.

### switchToLast, switchToLast(windowHandleToExclude)
First version (without windowHandleToExclude argument) tries to find last window and switches to it.
Second version of this method (with argument) add certainty that switch result won't be the excluded handler of window.

## Keyboard and Mouse actions

Advanced keyboard and mouse actions are available using keyboard() and mouse() in FluentTest class or element.

## Filling forms

A fill() builder method is available to quickly fill forms.

```java
$("input").fill().with("bar")
```

Previous statement will fill all the input elements with bar.

```java
$("input").fill().with("myLogin","myPassword")
```
 
Previous statement will fill the first element of the input selection with `myLogin`, the second with `myPassword`.
If there are more input elements found, the last value `myPassword` will be repeated for each subsequent element.

You can also fill `<select>` elements

```java
// Select "MONDAY" value
$("daySelector").fillSelect().withValue("MONDAY")

// Select second value
$("daySelector").fillSelect().withIndex(1)

// Select value with visible text "Monday"
$("daySelector").fillSelect().withText("Monday")
```

Don't forget that only visible fields will be modified. It simulates a real person using a browser !

## Pages
Selenium tests can easily become a mess. To avoid this, you should use the [Page Object Pattern](http://code.google.com/p/selenium/wiki/PageObjects).
Page Object Pattern will enclose all the plumbing relating to how pages interact with each other and how the user
interacts with the page, which makes tests a lot easier to read and to maintain.

Try to construct your Page thinking that it is better if you offer services from your page rather than just the internals of the page.
A Page Object can model the whole page or just a part of it.

To construct a Page, extend `FluentPage`.

You can define the URL of the page by overriding the `getUrl` method. Then, it's possible tu use the `goTo()` method in 
your test code to set the current URL to the URL of the page.

You can also override isAt method, to run all the assertions necessary in order to ensure that you are on the right
page.

For example, if I choose that the title will be sufficient to know if I'm on the right page:

```java
public class MyPage extends FluentPage {
    @Override
    public void getURL() {
        return "/app/my-page";
    }
    
    @Override
    public void isAt() {
        assertThat(title()).contains("Selenium");
    }
}
```

Instead of manually implementing `isAt` and `getUrl` method, you can use Selenium `@FindBy` annotation on the page 
class, to define an Element Locator that should identify this page, and `@PageUrl` to define the URL 
used by `goTo()` method.

```java
@PageUrl("/app/my-page")
@FindBy(css="#my-page")
public class MyPage extends FluentPage {
    ...
}
```

Create your own methods to easily fill out forms, go to another or whatever else may be needed in your test.

```java
public class LoginPage extends FluentPage {
    public String getUrl() {
        return "myCustomUrl";
    }
    public void isAt() {
        assertThat(title()).isEqualTo("MyTitle");
    }
    public void fillAndSubmitForm(String... paramsOrdered) {
        $("input").fill().with(paramsOrdered);
        $("#create-button").click();
    }
}
```

And the corresponding test:

```java
public void checkLoginFailed() {
    goTo(loginPage);
    loginPage.fillAndSubmitLoginForm("login", "wrongPass");
    loginPage.isAt();
}
```

Or if you have the [AssertJ](https://github.com/joel-costigliola/assertj-core) module (just static import org.fluentlenium.assertj.FluentLeniumAssertions.assertThat)

```java
public void checkLoginFailed() {
    goTo(loginPage);
    loginPage.fillAndSubmitLoginForm("login","wrongPass");
    assertThat($(".error")).hasSize(1);
    assertThat(loginPage).isAt();
}
```

## Injection
You can use the annotation `@Page` to construct your Page Objects easily.

```java
public class AnnotationInitialization extends FluentTest {
    @Page
    public MyPage page;


    @Test
    public void test_no_exception() {
        goTo(page);
        //put your assertions here
    }
}
```

You can also use the factory method `newInstance`:

```java
public class BeforeInitialization extends FluentTest {
    private MyPage page;
    
    @Before
    public void beforeTest() {
        page = newInstance(MyPage.class);
    }
    
    @Test
    public void test_no_exception() {
        page.go();
    }
}
```

Anyway, Page Objects constructors should never be called directly.

All `FluentWebElement` fields are automatically searched for by name or id. For example, if you declare a 
`FluentWebElement` named `createButton`, it will search the page for an element where `id` is `createButton` or 
name is `createButton`.

Keep in mind that all elements are Lazy Proxy Locators, they behave exactly like if they where found with `$` method.

```java
public class LoginPage extends FluentPage {
    private FluentWebElement createButton;
    
    public String getUrl() {
       return "myCustomUrl";
    }
    
    public void isAt() {
       assertThat(title()).isEqualTo("MyTitle");
    }
    
    public void fillAndSubmitForm(String... paramsOrdered) {
        $("input").fill().with(paramsOrdered);
        createButton.click();
    }
}
```

Not only `FluentWebElement` fields are populated. Every type with a constructor taking a `WebElement` is a candidate.
This makes it possible for the page to expose fields with functional methods and not (only) the 'technical' methods
that `FluentWebElement` exposes.

```java
public class LoginPage extends FluentPage {
   private MyButton createButton;
   
   public void fillAndSubmitForm(String... paramsOrdered) {
       $("input").fill().with(paramsOrdered);
       createButton.clickTwice();
   }
   
   public static class MyButton {
       private WebElement webElement;
       
       public MyButton(WebElement webElement) {
           this.webElement = webElement;
       }
       
       public void clickTwice() {
           webElement.click();
           webElement.click();
       }
   }
}
```

If the naming conventions of your HTML ids and names don't match with the naming conventions of your Java fields,
or if you want to select an element with something other than the id or name, you can annotate the field with the
Selenium `@FindBy` (or `@FindBys`) annotation. The following example shows how to find the create button if its CSS class is
`create-button`.

```java
public class LoginPage extends FluentPage {
    @FindBy(css = "button.create-button")
    private FluentWebElement createButton;
    
    public String getUrl() {
       return "myCustomUrl";
    }
    
    public void isAt() {
       assertThat(title()).isEqualTo("MyTitle");
    }
    
    public void fillAndSubmitForm(String... paramsOrdered) {
        $("input").fill().with(paramsOrdered);
        createButton.click();
    }
}
```

You can also refer to the list of FluentWebElements using `FluentList` dedicated interface,
or `java.util.List`.

```java
public class LoginPage extends FluentPage {
    @FindBy(css = "button.create-button")
    private FluentList<FluentWebElement> createButtons;
    
    public String getUrl() {
        return "myCustomUrl";
    }
    
    public void isAt() {
        assertThat(title()).isEqualTo("MyTitle");
        assertThat(buttons).hasSize(2);
    }
    
    public void fillAndSubmitForm(String... paramsOrdered) {
        $("input").fill().with(paramsOrdered);
        createButtons.get(1).click();
    }
}
```

Injection is recursive, and it's possible to retrieve the parent container using ```@Parent``` annotation.

### Components

A ```Component``` is an object wrapping a ```WebElement``` instance.

A ```Component``` supports Injection like a Page Object, but all searchs are performed in the local context of the wrapped element.

Using components improves readability of both Page Objects and Tests.

`FluentWebElement` is the default component class in FluentLenium, so you can implement you own custom component 
by extending `FluentWebElement` to add custom logic.

```java
public class SelectComponent extends FluentWebElement {
    public SelectComponent(WebElement webElement, WebDriver driver, ComponentInstantiator instantiator) {
        super(webElement, driver, instantiator);
    }
    
    public void doSelect(String selection) {
        // Implement selection provided by this component.
    }
    
    public String getSelection() {
        // Return the selected value as text.
    }
}
```

Components are created automatically by injection, 
or programmatically by calling `as(Class<?> componentClass)` method of ```FluentWebElement``` or ```FluentList```.

```java
SelectComponent comp = $("#some-select").first().as(SelectComponent.class);

comp.doSelect("Value to select");
assertThat(comp.getSelection()).isEquals("Value to select");
```

It's not mandatory to extend `FluentWebElement`, but a constructor with at least WebElement parameter is required.

```java
public class SelectComponent {
    private WebElement element;
    
    private FluentWebElement(WebElement element) { // This constructor MUST exist ! But can be private.
        this.element = element;
    }
}
```


## Events

Selenium has a driver wrapper named `EventFiringWebDriver` that is able to generate events and register listeners.

FluentLenium brings Events Annotations and a Listener API to register those listeners easily.

### Annotations

You can use annotations from `org.fluentlenium.core.events.annotations` package to register any method as a event 
listener.

Annotations can be used in a test class.

```java
@AfterClickOn
public void afterClickOn(FluentWebElement element);
    System.out.println("Element Clicked: " + element);
});
```

Annotations related to a WebElement can also be used in a component class.
```java

public class SomeComponent {
    private WebElement element;
    
    public SomeComponent(WebElement element) {
        this.element = element;
    }
    
    public SomeComponent click() {
        this.element.click();
        return this;
    }
    
    @AfterClickOn
    private void afterClickOn() {
        System.out.println("Element Clicked: " + this);
    }
}

public class SomeTest extends FluentTest {
    
    private SomeComponent clickComponent;
    private SomeComponent otherComponent;
    
    @Test
    public void test() {
        clickComponent.click();
        // @AfterClickOn annotated method will be invoked for clickComponent instance only.
    }
}

```

### Listener API

You can also register events through API using `events` method.

```java
events().afterClickOn(new ElementListener() {
    @Override
    public void on(FluentWebElement element, WebDriver driver) {
        System.out.println("Element Clicked: " + element);
    }
});

$("button").first().click(); // This will call the listener.
```

This integrates nicely with Java 8 lambdas

```java
events().afterClickOn((element, driver) -> System.out.println("Element Clicked: " + element));

$("button").first().click(); // This will call the listener.
```

## Wait for an Ajax Element to be available

There are multiple ways to make your driver wait for the result of an asynchronous call.
FluentLenium provides a rich and fluent API in order to help you to handle AJAX calls.
If you want to wait for at most 5 seconds until the number of elements corresponding to the until criteria (here the class fluent) has the requested size:


```java
await().atMost(5, TimeUnit.SECONDS).until(".fluent").hasSize(3);
```
The default wait is 500 ms.

Instead of `hasSize(3)`, you can also use `text("myTextValue")`, `isPresent()`, `isNotPresent()`, `hasId("myId")`, `hasName("myName")`, `containsText("myName")`,`areDisplayed()`, `areEnabled()`.
The `isPresent()` assertion is going to check if there is at most one element on the page corresponding to the filter.

If you need to be more precise, you can also use filters in the search:

```java
await().atMost(5, TimeUnit.SECONDS).until(".fluent").withText("myText").hasSize(3);
```

You can also use after `hasSize()` : `greaterThan(int)`, `lessThan(int)`, `lessThanOrEqualTo(int)`, `greaterThanOrEqualTo(int)` , `equalTo(int)`, `notEqualTo(int)`

You can also use matchers:

```java
await().atMost(5, TimeUnit.SECONDS).until(".fluent").withText().startsWith("start").isPresent();
```
     
Just use `startsWith`, `notStartsWith`, `endsWith`, `notEndsWith`, `contains`, `notContains`, `equalTo`, `containsWord`.

If you need to filter on a custom attribute name, this syntax will help:

```java
await().atMost(5, TimeUnit.SECONDS).until(".fluent").with("myAttribute").startsWith("myValue").isPresent();
```

You can also give instance of elements or list of elements if required.

```java
@FindBy(css = ".button")
private FluentWebElement button;

await().atMost(5, TimeUnit.SECONDS).until(element).isEnabled();
```

When running Java >= 8, you can use lambdas with `until`, `untilPredicate`, `untilElement` or `untilElements`.
```java
await().atMost(5, TimeUnit.SECONDS).untilElement(() -> $(".button").first()).isEnabled();
await().atMost(5, TimeUnit.SECONDS).untilElements(() -> $(".button")).areEnabled();

await().atMost(5, TimeUnit.SECONDS).untilPredicate((f) -> $(".button").first().isEnabled());
await().atMost(5, TimeUnit.SECONDS).until(() -> $(".button").first().isEnabled());
```

You can also check if the page is loaded using
```java
await().atMost(1, NANOSECONDS).untilPage().isLoaded();
```

If you want to wait until the page you want is the page that you are at, you can use:
```java
await().atMost(5, TimeUnit.SECONDS).untilPage(myPage).isAt();
```
This methods actually calls `myPage.isAt()`. If the `isAt()` method of the `myPage` object does not throw any exception during the time specified, then the framework will consider that the page is the one wanted.

You can override `await()` method in your own test class to define global settings on wait objects.
```java
@Override
public FluentWait await() {
    return super.await().atMost(5, TimeUnit.SECONDS).ignoring(NoSuchElementException.class, ElementNotVisibleException.class);
}
```

### Polling Every
You can also define the polling frequency, for example, if you want to poll every 5 seconds:
```java
await().pollingEvery(5, TimeUnit.SECONDS).until(".fluent").with("myAttribute").startsWith("myValue").isPresent();
```
The default value is 500ms.

You can also chain filter in the asynchronous API:

```java
await().atMost(5, TimeUnit.SECONDS).until(".fluent").with("myAttribute").startsWith("myValue").with("a second attribute").equalTo("my@ndValue").isPresent();
```

## Hooks

It's possible to add some behavior for any element without changing its code by using hooks. 

FluentLenium is shipped with the ```Wait``` hook.

```
// This call will wait for ajax-element to be clickable.
find(".fluent").withHook(WaitHook.class).click();

// Hooks can be chained and their effect will be associated.
find(".fluent").withHook(WaitHook.class).withHook(AnotherHook.class).click();

// Options can be given to hooks.
find(".fluent")
    .withHook(WaitHook.class, WaitHookOptions.builder().atMost(20L).build())
    .click()
```

### Hook Annotations

Hooks can also be mapped to annotation, like ```@Wait``` for the ```WaitHook``` class.

You can place a hook annotation on injected Field, Fluent Test class or Page.

The hook will be effective for a field if annotation is present on it. All fields from a Fluent Test class if 
it's present on a Fluent Test class and all fields from a Page if it's present on a Page.

Page build through Injection recursively inherits hooks from parent pages and fluent test.

If you need to disable all inherited hooks in particular Page or Field, you should use ```@NoHook``` annotation,
or call ```noHook()``` function on the element;

It's also possible to use the generic ```@Hook``` annotation to enable a hook class.

```java
@Wait
public class MyTest extends FluentTest {
    FluentWebElement waitElement;
    
    @Hook(AnotherHook.class)
    FluentWebElement waitAndAnotherHookElement;
    
    @NoHook
    FluentWebElement noHookElement;
    
    @Page
    MyPage page;
}

@Hook(PageHook.class)
public class MyPage extends FluentPage {
    
}
```

### @Wait Hook

```Wait``` hook automatically waits for conditions to be applied before interacting with Elements, avoiding the need 
of writing technical waiting and condition code in tests and page objects.

Default timeout for ```@Wait``` hook is 5 seconds.

### Custom hook

It's possible to implement your own hook by extending ```BaseHook``` or ```BaseFluentHook```.

Let's implement Example hook by writing a configurable message before and after click.

- Create the hook option class. It should be a JavaBean containing configuration options for the hook.

```
public class ExampleHookOptions {
    private String message = "ExampleHook";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
```

- Create the hook by extending of following class ```BaseHook<ExampleHookOptions>``` or ```BaseFluentHook<ExampleHookOptions>```. It should at least have a public 
constructor matching parent class, and generic type should be a hook option class.

```
public class ExampleHook extends BaseHook<ExampleHookOptions> {
    public ExampleHook(FluentControl fluentControl, ComponentInstantiator instantiator, Supplier<WebElement> elementSupplier, Supplier<ElementLocator> locatorSupplier, ExampleHookOptions options) {
        super(fluentControl, instantiator, elementSupplier, locatorSupplier, options);
    }
}

```

- Create the hook annotation. Annotation parameters should match options found in hook option class.

```
@Inherited
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Hook(ExampleHook.class)
@HookOptions(ExampleHookOptions.class)
public @interface Example {
    String message() default "";
}
```

- Add a constructor to hook options class that will support loading of hook annotation into hook option class.

```
public class ExampleHookOptions {
    public ExampleHookOptions(Example annotation) {
        message = annotation.message();
    }

    public ExampleHookOptions() {
    }
```

- Override methods from ```WebElement``` or ```ElementLocator``` in the hook implementation class to add desired 
behavior.

```
@Override
public void click() {
    System.out.println(getOptions().getMessage() + ": before click!");
    super.click();
    System.out.println(getOptions().getMessage() + ": after click!");
}
```

[Example sources are available on github](https://github.com/FluentLenium/FluentLenium/tree/master/fluentlenium-examples/src/test/java/org/fluentlenium/examples/hooks).

You may also read [sources for @Wait hook](https://github.com/FluentLenium/FluentLenium/tree/master/fluentlenium-core/src/main/java/org/fluentlenium/core/hook/wait) to read how it's implemented.

## Alternative Syntax

If you don't like the [JQuery](http://jquery.com/) syntax, you can replace `$` with `find` method:

```java
goTo("http://mywebpage/");
find("#firstName").write("toto");
find("#create-button").click();
assertThat(title()).isEqualTo("Hello toto");
```

Both syntax are equivalent. `$` is simply an alias for the `find` method.


## Execute javascript
If you need to execute some javascript, just call `executeScript` with your script as parameter.
For example, if you have a javascript method called change and you want to call it just add this in your test:

```java
executeScript("change();");
```

You can either execute javascript with arguments, with async `executeAsyncScript`, and retrieve the result.

```java
executeScript("change();", 12L).getStringResult();
```

## Taking ScreenShots and HTML Dumps
You can take a ScreenShot and a HTML Dump of the browser.
```java
takeScreenShot();
takeHtmlDump();
```
The file will be named using the current timestamp.
You can of course specify a path and a name using:
```java
takeScreenShot(pathAndfileName);
takeHtmlDump(pathAndfileName);
```
Screenshot and HTML Dump can be automatically performed on test fail.
```java
setScreenShotPath(path);
setHtmlDumpPath(path);
setScreenShotMode(TriggerMode.ON_FAIL);
setHtmlDumpMode(TriggerMode.ON_FAIL);
```

## Isolated Tests
If you want to test concurrency or if you need for any reason to not use the mechanism of extension of FluentLenium, you can also, instead of extending FluentTest, instantiate your fluent test object directly.

```java
IsolatedTest test = new IsolatedTest()
test.goTo(DEFAULT_URL);
test.await().atMost(1, SECONDS).until(".fluent").with("name").equalTo("name").isPresent();
test.$("input").first().isEnabled();
```

## Configure FluentLenium

FluentLenium can be configured in many ways through configuration properties.

### Configuration properties

  - **webDriver**
  
    Sets the WebDriver type to use.

    Default value: ```null```. 
        
    Possible values are ```remote```, ```firefox```, ```marionette```, ```chrome```, ```ie```, ```safari```, 
    ```phantomjs```, ```htmlunit```, or any class name implementing ```WebDriver```.
    
    If not defined, FluentLenium will use the first value for which WebDriver is available in classpath.

  - **remoteUrl**
  
    Sets the remote URL for ```remote``` *webDriver*. This should be the URL to access Selenium-Grid server.

  - **capabilities**

     Sets the [Capabilities](https://github.com/SeleniumHQ/selenium/wiki/DesiredCapabilities) to use with the WebDriver.
     
     Can be:
       - a `Capabilities` JSON Object string.
       - an URL pointing to a `Capabilities` JSON Object string.
       - a static method available in `org.openqa.selenium.remote.DesiredCapabilities`, like `firefox` or `chrome`.
       - a class name implementing `Capabilities`.
       - a reference to a `CapabilitiesFactory`.

     Default value: ```null```.
     
  - **driverLifecycle**
  
    Sets the lifecycle of the WebDriver. WebDriver is fully managed by FluentLenium, so you should never 
    create or quit a WebDriver by yourself.
    
    Possible values are:
    
      - `JVM`: WebDriver is created once, and same instance is used for each test class and method.
      - `CLASS`: WebDriver is created for each test class, and same instance is used for each test method in the class.
      - `METHOD`: WebDriver is created for each test method, and this instance is used only for one test method.
    
    Please keep in mind that this configures when drivers are created and exited at runtime, but it does not deal with
    concurrency of your tests.
    
    Default value: ```METHOD```
    
  - **deleteCookies**

    When using CLASS or JVM *driverLifecycle* configuration property, allow to delete cookies between each 
    test.

    Default value: ```false```.

  - **baseUrl**
  
     Sets the base URL used to build absolute URL when relative URL is given to {@link FluentAdapter#goTo(String)}.
     
     Default value: ```null```.

  - **pageLoadTimeout**
     
     Sets the amount of time to wait for a page load to complete before throwing an error.
     If the timeout is negative, page loads can be indefinite.
     
     Default value: ```null```.

  - **implicitlyWait**
     
     Specifies the amount of time the driver should wait when searching for an element if it is
     not immediately present.
     
     Default value: ```null```.


  - **scriptTimeout**
  
     Sets the amount of time to wait for an asynchronous script to finish execution before
     throwing an error. If the timeout is negative, then the script will be allowed to run
     indefinitely.
     
     Default value: ```null```.
     
  - **eventsEnabled**
  
     Enables ```events()``` by wrapping the ```WebDriver``` in ```EventFiringWebDriver```.
     
     Default value: ```true```.
     
  - **screenshotPath**

     Sets the filesystem path where screenshot will be saved.
     
     Default value: ```null```.


  - **screenshotMode**

     Sets the trigger mode of screenshots. Can be ```AUTOMATIC_ON_FAIL``` to take screenshot when the test fail 
     or ```MANUAL```.
     
     Default value: ```null```.


  - **htmlDumpPath**
     
     Sets the filesystem path where screenshot will be saved.
     
     Default value: ```null```.


  - **htmlDumpMode**
     
     Sets the trigger mode of html dump. Can be ```ON_AUTOMATIC_ON_FAIL``` to take html dump when the test fail 
     or ```MANUAL```.
     
     Default value: ```null```.
     
     
  - **configurationDefaults**
   
    Set this to a class implementing ```ConfigurationProperties``` to provide the default values
    of the configuration properties.

    Default value: ```org.fluentlenium.configuration.ConfigurationDefaults```.
      
      
  - **configurationFactory**
  
     Set this to a class implementing ```ConfigurationFactory``` to customize the ways properties are read.
     This allow to configure properties from sources that are not supported by default FluentLenium.
     
     Default value: ```org.fluentlenium.configuration.DefaultConfigurationFactory```.
     
     
 Keep in mind that when those properties are defined through System Properties or Environment Variables, they need to
 be prefixed with ```fluentlenium.``` (ie. ```fluentlenium.webDriver=chrome```).

### Configuration Ways

It's possible to define those properties using:

  - **Overrides** of JavaBean **property getters** of the test class.
  
        public class SomeFluentTest extends FluentTest {
            @Override
            public String getWebDriver() {
                return "chrome";
            }
        }
  
  - **Calls** of JavaBean **property setters** of the test class.
  
        public class SomeFluentTest extends FluentTest {
            public SomeFluentTest() {
                setWebDriver("chrome");
            }
        }
  
  - **System properties** of the Java Environment, passed using ```-D``` on the command line. Property names must be **prefixed with fluentlenium.**. 
  
        mvn clean test -Dfluentlenium.webDriver=chrome        
  
  - **Environment Variable** of the Operating System. Property names **must be prefixed with fluentlenium.**.
  
        $ EXPORT fluentlenium.webDriver=chrome; mvn clean test;
  
  
  - **@FluentConfiguration Annotation** on test class to configure.

         @FluentConfiguration(webDriver="chrome")
         public class SomeFluentTest extends FluentTest {
             ....
         }


  - **Java Properties file** located at ```/fluentlenium.properties``` in the classpath.

        $ cat fluentlenium.properties
        webDriver=chrome
        ...

  - **ConfigurationProperties** custom implementation specified by ```configurationDefaults``` property.
  
        public class CustomConfigurationDefaults extends ConfigurationDefaults {
            @Override
            public String getWebDriver() {
                return "chrome";
            }
        }
        
        $ cat fluentlenium.properties
        configurationDefaults=org.your.package.CustomConfigurationDefaults
 
This list of way to configure fluentlenium is ordered by priority. If a value is defined for a property in an element, 
lower ways will just be ignored.

You may implement additionnal ways to read configuration property by implementing another
```ConfigurationFactory``` and set the new configuration factory class name in the ```configurationFactory``` property.

### Register a custom WebDriver
You can register a custom WebDriver type by providing your own implementation of ```WebDriverFactory``` in 
```WebDrivers``` registry.

A ```WebDriverFactory``` implementation is responsible for creating new instances for a type of WebDriver.

For instance, to run your tests on [BrowserStack](https://browserstack.com)

```java
@FactoryName("browserstack")
public class BrowserStackWebDriverFactory implements WebDriverFactory {
    @Override
    public WebDriver newWebDriver() {
        String HUB_URL = "http://" + USERNAME + ":" + ACCESS_KEY + "@hub.browserstack.com/wd/hub";
    
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("os", "OS X");
        caps.setCapability("os_version", "El Capitan");
        caps.setCapability("browser", "firefox");
        caps.setCapability("browser_version", "44");
        caps.setCapability("build", "Sample FluentLenium Tests");
        caps.setCapability("browserstack.debug", "true");
    
        URL hubURL = null;
        try {
          hubURL = new URL(HUB_URL);
        } catch(Exception e) {
          System.out.println("Please provide proper credentials. Error " + e);
        }
    
        return new RemoteWebDriver(hubURL, caps);
    }
}
```

This implementation will be discovered with [classindex](https://github.com/atteo/classindex), a 
compile-time alternative to run-time classpath scanning. It requires your IDE to have Annotation Processing 
enabled in the Java Compiler configuration.

Instead of implementing a new ```WebDriverFactory``` class, you may also override ```newWebDriver()``` in the Test 
class, but doing so will ignore any value defined in ```webDriver``` configuration property.

### Run test concurrently

If you need to make your tests run concurrently to speed them up, you should use dedicated libraries/extensions. 
You can use the Surefire maven plugin for example.

**Surefire JUnit example**

```xml
<profile>
    <id>junit-tests</id>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <configuration>
                    <parallel>methods</parallel>
                    <threadCount>4</threadCount>
                    <forkCount>8</forkCount>
                    <reuseForks>true</reuseForks>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

**Surefire TestNG example**

```xml
<profile>
    <id>testng-tests</id>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <configuration>
                    <suiteXmlFiles>
                        <suiteXmlFile>test-suites/basic.xml</suiteXmlFile>
                    </suiteXmlFiles>
                    <goal>test</goal>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

**TestNG test suite file**
```xml
<suite name="Parallel tests" parallel="tests" thread-count="10">
    <test name="Home Page Tests" parallel="methods" thread-count="10">
        <parameter name="test-name" value="Home page"/>
        <classes>
            <class name="com.example.testng.OurLocationTest"/>
            <class name="com.example.testng.OurStoryTest"/>
        </classes>
    </test>
    <test name="Another Home Page Tests" parallel="classes" thread-count="2">
        <parameter name="test-name" value="another home page"/>
        <classes>
            <class name="com.example.testng.HomeTest"/>
            <class name="com.example.testng.JoinUsTest"/>
        </classes>
    </test>
</suite>
```

TestNG gives you more flexibility in order to the concurrency level, test suites and having better control on executed
 scenarios.

Both test frameworks are giving possibility to define the parallelism level of tests. 

It is possible when you have multiple execution/concurrency levels set in your tests to face driver sharing issues,
so please use ```METHOD``` ```driverLifecycle``` configuration property when your execution methods are mixed up.

## Iframe
If you want to switch the Selenium webDriver to an iframe (see this [Selenium FAQ](https://code.google.com/p/selenium/wiki/FrequentlyAskedQuestions#Q:_How_do_I_type_into_a_contentEditable_iframe?)),
you can just call the method switchTo() :

To switch to the default context:
```java
switchTo();
```
or
```java
switchToDefault();
```

To switch to the iframe selected:
```java
switchTo($("iframe#frameid"));
```

## Alert
If you want manage alert (see this [Selenium FAQ](http://code.google.com/p/selenium/wiki/FrequentlyAskedQuestions#Q:_Does_WebDriver_support_Javascript_alerts_and_prompts?)),

When an alert box pops up, click on "OK":
```java
alert().accept();
```

When an alert box pops up, click on "Cancel":
```java
alert().dismiss();
```

Entering an input value in prompt:
```java
alert().prompt("FluentLenium")
```

## Window
Maximize browser window:
```java
maximizeWindow();
```

## SNAPSHOT version (unstable!)

A SNAPSHOT version is automatically packaged after each commit on master branch.

This version can be very unstable and should be used for testing purpose only.

To use it, you need to declare an additional repository in the ```pom.xml``` of your project.

```xml
<repositories>
    <repository>
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy> <!-- Force checking of updates -->
        </snapshots>
        <releases>
            <enabled>false</enabled>
        </releases>
        <id>sonatype-snapshots</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    </repository>
</repositories>
```

Then use ```SNAPSHOT``` version when declaring the dependencies.

```xml
<dependency>
    <groupId>org.fluentlenium</groupId>
    <artifactId>fluentlenium-core</artifactId>
    <version>1.0.0-RC2</version>
    <scope>test</scope>
</dependency>
```

## Supported Test Runners

### JUnit

- Import this maven dependency.

```xml
<dependency>
    <groupId>org.fluentlenium</groupId>
    <artifactId>fluentlenium-junit</artifactId>
    <version>1.0.0-RC2</version>
    <scope>test</scope>
</dependency>
```

- Extends FluentTest.

### TestNG

- Import this maven dependency.

```xml
<dependency>
    <groupId>org.fluentlenium</groupId>
    <artifactId>fluentlenium-testng</artifactId>
    <version>1.0.0-RC2</version>
    <scope>test</scope>
</dependency>
```

- Extends FluentTestNG instead of FluentTest.

### Cucumber

- Import this maven dependency.

```xml
<dependency>
    <groupId>org.fluentlenium</groupId>
    <artifactId>fluentlenium-cucumber</artifactId>
    <version>1.0.0-RC2</version>
    <scope>test</scope>
</dependency>
```

- Extend each Step with `FluentCucumberTest` instead of `FluentTest`.

- Add this piece of code for each Cucumber Step.

```java
@Before
public void before(Scenario scenario) {
    super.before(scenario);
}

@After
public void after(Scenario scenario) {
    super.after(scenario);
}
```

## Supported Assertions Libraries

### JUnit
You can use FluentLenium using [JUnit](http://www.junit.org) assertions, but can of course use others frameworks such as [AssertJ](https://github.com/joel-costigliola/assertj-core) or [Hamcrest](http://hamcrest.org/JavaHamcrest/).

```java
goTo("http://mywebpage/");
$("#firstName").fill().with("toto");
$("#create-button").click();
assertEqual("Hello toto",title());
```

### AssertJ

- Import this additional maven dependency.

```xml
<dependency>
    <groupId>org.fluentlenium</groupId>
    <artifactId>fluentlenium-assertj</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
</dependency>
```

- Add those static imports.

```java
import static org.assertj.core.api.Assertions.*;
import static org.fluentlenium.assertj.FluentLeniumAssertions.*;
```

```java
goTo("http://mywebpage/");
$("#firstName").fill().with("toto");
$("#create-button").click();
assertThat(title()).isEqualTo("Hello toto");
assertThat($(".fluent")).hasText("present text");
assertThat($(".fluent")).hasNotText("not present text");
assertThat($(".another")).hasSize(7);
assertThat($(".another2")).hasSize().lessThan(5);
assertThat($(".another2")).hasSize().lessThanOrEqualTo(5);
assertThat($(".another3")).hasSize().greaterThan(2);
assertThat($(".another3")).hasSize().greaterThanOrEqualTo(2);
```

### Hamcrest

- Import this additional maven dependency.

```xml
<dependency>
    <groupId>org.hamcrest</groupId>
    <artifactId>hamcrest-core</artifactId>
    <version>1.3</version>
    <scope>test</scope>
</dependency>
```

- Add those static imports.

```java
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
```

```java
goTo("http://mywebpage/");
$("#firstName").fill().with("toto");
$("#create-button").click();
assertThat(title(),equalTo("Hello toto"));
```

## Contact Us
If you have any comment, remark or issue, please open an issue on 
[FluentLenium Issue Tracker](https://github.com/FluentLenium/FluentLenium/issues)
