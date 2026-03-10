package it.catalog.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



@Component
public class PathPrefixProvider {

    @Value("${path.root}")
    private String prefix;

    public String getPrefix() {
        return prefix;
    }
    
    public String stripPrefix(String value) {
        return value != null && value.startsWith(prefix)
            ? value.substring(prefix.length())
            : value;
    }
}
