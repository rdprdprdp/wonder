//
// ERD2WDebugFlags.java: Class file for WO Component 'ERD2WDebugFlags'
// Project ERDirectToWeb
//
// Created by patrice on Wed Jul 24 2002
//


package er.directtoweb.components;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.woextensions.WOStatsPage;

import er.directtoweb.ERD2WModel;
import er.directtoweb.ERDirectToWeb;
import er.extensions.ERXExtensions;
import er.extensions.appserver.ERXApplication;
import er.extensions.components.ERXComponentUtilities;
import er.extensions.foundation.ERXProperties;

///////////////////////////////////////////////////////////////////////////////////////////////////////
// This component can be used in the wrapper of a D2W app to provide convenient development time 
//  (as flagged by WOCachingEnabled) access to
//	the log4j configuration
//	ERD2WDebuggingEnabled
///////////////////////////////////////////////////////////////////////////////////////////////////////


public class ERD2WDebugFlags extends WOComponent {
	/**
	 * Do I need to update serialVersionUID?
	 * See section 5.6 <cite>Type Changes Affecting Serialization</cite> on page 51 of the 
	 * <a href="http://java.sun.com/j2se/1.4/pdf/serial-spec.pdf">Java Object Serialization Spec</a>
	 */
	private static final long serialVersionUID = 1L;

    public ERD2WDebugFlags(WOContext context) {
        super(context);
    }

    public boolean isStateless() {
        return true;
    }

    public WOComponent statisticsPage() {
        WOStatsPage nextPage = (WOStatsPage) pageWithName("ERXStatisticsPage");
        nextPage.password = ERXProperties.stringForKey("WOStatisticsPassword");
        return nextPage.submit();
    }
    
    public WOComponent toggleD2WInfo() {
        boolean currentState=ERDirectToWeb.d2wDebuggingEnabled(session());
        ERDirectToWeb.setD2wDebuggingEnabled(session(), !currentState);
        return null;
    }

    public WOComponent toggleAdaptorLogging() {
        boolean currentState = adaptorLoggingEnabled();
        ERXExtensions.setAdaptorLogging(!currentState);
        return null;
    }

    public boolean adaptorLoggingEnabled() {
        return ERXExtensions.adaptorLogging();
    }

    public WOComponent clearD2WRuleCache() {
        ERD2WModel.erDefaultModel().clearD2WRuleCache();
        return null;
    }

    /**
     * Toggles the display of page metrics.
     * @return the current page
     */
    public WOComponent togglePageMetrics() {
        ERDirectToWeb.setPageMetricsEnabled(!metricsEnabled());
        return null;
    }

    /**
     * Determines if detailed page metrics should be displayed.
     * @return true if they should be displayed
     */
    public boolean metricsEnabled() {
        return ERDirectToWeb.pageMetricsEnabled();
    }

    /**
     * Toggles the display of detailed page metrics.
     * @return the current page
     */
    public WOComponent toggleDetailedPageMetrics() {
        ERDirectToWeb.setDetailedPageMetricsEnabled(!detailedMetricsEnabled());
        return null;
    }

    /**
     * Determines if detailed page metrics should be displayed.
     * @return true if they should be displayed
     */
    public boolean detailedMetricsEnabled() {
        return ERDirectToWeb.detailedPageMetricsEnabled();
    }

    /**
     * Allow users to override when the debug flags show.  Defaults to showing when the application is running in
     * {@link ERXApplication#isDevelopmentMode development mode}, i.e. is not deployed to production.
     * @return true when the debug flags should be displayed
     */
    public boolean shouldShow() {
        return ERXComponentUtilities.booleanValueForBinding(this, "shouldShow", ERXApplication.erxApplication().isDevelopmentMode());
    }

}
