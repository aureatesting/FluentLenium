package org.fluentlenium.core.proxy;

import com.google.common.base.Suppliers;
import org.fluentlenium.core.domain.WrapsElements;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * {@link org.openqa.selenium.support.pagefactory.ElementLocator} for an already found list of {@link WebElement} instance.
 */
public class ElementListInstanceLocator extends ElementListSupplierLocator implements WrapsElements {
    public ElementListInstanceLocator(List<WebElement> elements) {
        super(Suppliers.ofInstance(elements));
    }

    @Override
    public List<WebElement> getWrappedElements() {
        return findElements();
    }
}
