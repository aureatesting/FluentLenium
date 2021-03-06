package org.fluentlenium.configuration;

/**
 * {@link ConfigurationProperties} based on Environment Variables.
 *
 * @see ConfigurationProperties
 */
public class EnvironmentVariablesConfiguration extends AbstractPropertiesConfiguration {
    @Override
    protected String getPropertyImpl(String propertyName) {
        return System.getenv(propertyName);
    }
}
