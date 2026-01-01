package com.metaverse.workflow.logging;

import com.metaverse.workflow.configuration.SplunkSdkLogger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.*;
import org.apache.logging.log4j.core.layout.PatternLayout;

@Plugin(
        name = "SplunkSdkAppender",
        category = Core.CATEGORY_NAME,
        elementType = Appender.ELEMENT_TYPE,
        printObject = true
)
public class SplunkSdkAppender extends AbstractAppender {

    protected SplunkSdkAppender(String name, Filter filter) {
        super(name, filter, PatternLayout.createDefaultLayout(), false);
    }

    @Override
    public void append(LogEvent event) {
        try {
            String message = event.getMessage().getFormattedMessage();
            SplunkSdkLogger.send(message);
        } catch (Exception ignored) {
            // NEVER break application flow
        }
    }

    @PluginFactory
    public static SplunkSdkAppender createAppender(
            @PluginAttribute("name") String name
    ) {
        return new SplunkSdkAppender(name, null);
    }
}
