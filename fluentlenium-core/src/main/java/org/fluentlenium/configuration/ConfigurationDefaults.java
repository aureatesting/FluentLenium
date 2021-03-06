package org.fluentlenium.configuration;

import org.openqa.selenium.Capabilities;

/**
 * Default configuration implementation.
 *
 * @see ConfigurationProperties
 */
public class ConfigurationDefaults implements ConfigurationProperties {
    @Override
    public Class<? extends ConfigurationFactory> getConfigurationFactory() {
        return null;
    }

    @Override
    public Class<? extends ConfigurationProperties> getConfigurationDefaults() {
        return ConfigurationDefaults.class;
    }

    @Override
    public String getWebDriver() {
        return null;
    }

    @Override
    public String getRemoteUrl() {
        return null;
    }

    @Override
    public Capabilities getCapabilities() {
        return null;
    }

    @Override
    public DriverLifecycle getDriverLifecycle() {
        return DriverLifecycle.METHOD;
    }

    @Override
    public Boolean getDeleteCookies() {
        return false;
    }

    @Override
    public String getBaseUrl() {
        return null;
    }

    @Override
    public Long getPageLoadTimeout() {
        return null;
    }

    @Override
    public Long getImplicitlyWait() {
        return null;
    }

    @Override
    public Long getScriptTimeout() {
        return null;
    }

    @Override
    public Boolean getEventsEnabled() {
        return true;
    }

    @Override
    public String getScreenshotPath() {
        return null;
    }

    @Override
    public String getHtmlDumpPath() {
        return null;
    }

    @Override
    public TriggerMode getScreenshotMode() {
        return null;
    }

    @Override
    public TriggerMode getHtmlDumpMode() {
        return null;
    }
}
