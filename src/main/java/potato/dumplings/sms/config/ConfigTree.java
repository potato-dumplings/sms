package potato.dumplings.sms.config;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author potato-dumplings
 */
public class ConfigTree {

    private final ConfigurableEnvironment environment;
    private final String prefix;

    public ConfigTree(ConfigurableEnvironment environment, String prefix) {
        Assert.notNull(environment, "environment is required");
        Assert.notNull(prefix, "prefix is required");
        this.environment = environment;
        this.prefix = prefix;
    }

    private String fullPrefixOrKey(String prefixOrKey) {
        return this.prefix + prefixOrKey;
    }

    public Map<String, Object> getProperties() {
        Map<String, Object> m = new HashMap<>();
        for (PropertySource<?> source : environment.getPropertySources()) {
            if (!(source instanceof EnumerablePropertySource)) {
                continue;
            }
            for (String name : ((EnumerablePropertySource<?>) source).getPropertyNames()) {
                if (name != null && name.startsWith(prefix)) {
                    String subKey = name.substring(prefix.length());
                    m.put(subKey, environment.getProperty(name));
                }
            }
        }
        return m;
    }

    public boolean containsProperty(String key) {
        key = fullPrefixOrKey(key);
        return environment.containsProperty(key);
    }

}
