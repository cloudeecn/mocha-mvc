package works.cirno.mocha;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartupPerformanceListener implements ServletContextListener {

	private static final Logger log = LoggerFactory.getLogger(StartupPerformanceListener.class);

	public final static String KEY_PERF_START = "PERF.START.TS";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		log.info("Put startup timestamp to servlet context: {}", KEY_PERF_START);
		ServletContext sc = sce.getServletContext();
		sc.setAttribute(KEY_PERF_START, System.currentTimeMillis());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

}
