package application.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Global exception handler for the F1 application.
 * Handles database exceptions and general exceptions with custom error pages.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Log log = LogFactory.getLog(this.getClass());

    /**
     * Handles all database-related exceptions (DataAccessException).
     * Redirects to database-error page.
     */
    @ExceptionHandler(DataAccessException.class)
    public ModelAndView handleDatabaseException(DataAccessException ex) {
        log.error("Database exception occurred: " + ex.getMessage(), ex);

        ModelAndView mav = new ModelAndView("error/database-error");
        mav.addObject("errorMessage", "A database error occurred. Please try again later.");
        mav.addObject("errorDetails", ex.getMessage());
        return mav;
    }

    /**
     * Handles missing static resource exceptions (NoResourceFoundException).
     * Suppresses logging for common browser-generated requests (favicon, Chrome DevTools, etc.).
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ModelAndView handleNoResourceFoundException(NoResourceFoundException ex) {
        String resourcePath = ex.getResourcePath();

        // List of known harmless browser-generated requests
        boolean isBrowserRequest = resourcePath != null && (
            resourcePath.equals(".") ||
            resourcePath.equals("..") ||
            resourcePath.startsWith(".well-known/") ||
            resourcePath.equals("favicon.ico") ||
            resourcePath.equals("apple-touch-icon") ||
            resourcePath.startsWith("apple-touch-icon-") ||
            resourcePath.equals("robots.txt") ||
            resourcePath.equals("sitemap.xml")
        );

        // Only log at DEBUG level for browser requests, ERROR for actual missing resources
        if (isBrowserRequest) {
            log.debug("Browser-generated request for missing resource: " + resourcePath);
        } else {
            log.error("Static resource not found: " + resourcePath, ex);
        }

        ModelAndView mav = new ModelAndView("error/error");
        mav.addObject("errorMessage", "The requested resource was not found.");
        mav.addObject("errorDetails", "Resource: " + resourcePath);
        return mav;
    }

    /**
     * Handles all other general exceptions.
     * Redirects to general error page.
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneralException(Exception ex) {
        log.error("General exception occurred: " + ex.getMessage(), ex);

        ModelAndView mav = new ModelAndView("error/error");
        mav.addObject("errorMessage", "An unexpected error occurred. Please try again.");
        mav.addObject("errorDetails", ex.getMessage());
        return mav;
    }
}
