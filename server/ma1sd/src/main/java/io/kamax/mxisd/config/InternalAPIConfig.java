package io.kamax.mxisd.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InternalAPIConfig {

    private final static Logger log = LoggerFactory.getLogger(InternalAPIConfig.class);

    private boolean enabled = false;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void build() {
        log.info("--- Internal API config ---");
        log.info("Internal API enabled: {}", isEnabled());
    }
}
