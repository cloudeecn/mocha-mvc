package works.cirno.mocha.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 */
public class DispatcherFilter extends Dispatcher implements Filter {

    private static final Logger log = LoggerFactory.getLogger(DispatcherFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            String className = filterConfig.getInitParameter("configurator");
            if (className == null) {
                throw new ServletException("Can't initialize DispatcherFilter: " + filterConfig.getFilterName() + ", init parameter [configurator] must be specified");
            }
            Class<? extends MVCConfigurator> configuratorClass;
            try {
                configuratorClass = Class.forName(className).asSubclass(MVCConfigurator.class);
            } catch (ClassNotFoundException e) {
                throw new ServletException("Can't initialize DispatcherFilter: " + filterConfig.getFilterName() + ", configurator " + className + " not found", e);
            }
            MVCConfigurator configurator;
            try {
                configurator = configuratorClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ServletException("Can't initialize DispatcherFilter: " + filterConfig.getFilterName() + ", configurator " + className + " can't be instanced", e);
            }
            init(configurator);
        } catch (ServletException e) {
            log.error("Initialization failed", e);
            throw e;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (!invoke(req, resp)) {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
