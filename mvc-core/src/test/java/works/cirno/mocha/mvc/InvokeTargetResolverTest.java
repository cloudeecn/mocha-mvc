package works.cirno.mocha.mvc;

import org.junit.Assert;
import org.junit.Test;
import works.cirno.mocha.mvc.resolver.InvokeTargetResolver;
import works.cirno.mocha.mvc.resolver.PrefixDictInvokeTargetResolver;
import works.cirno.mocha.mvc.resolver.RegexInvokeTargetResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 */
public class InvokeTargetResolverTest {

    private long doBenchmark(InvokeTargetResolver resolver, Collection<String> keys) {
        long ret = 0;
        long begin = System.nanoTime();
        for (String key : keys) {
            int hash = resolver.resolve(key, "GET").hashCode();
            ret += hash;
        }
        long dur = System.nanoTime() - begin;
        if (ThreadLocalRandom.current().nextInt(100000) == 1) {
            System.out.println("Please ignore: " + ret);
        }
        return dur;
    }

    public void doTest(InvokeTargetResolver resolver) {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/uris.list"), Charset.forName("utf-8")))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    resolver.addServe(line, "GET", new MockInvokeTarget(line));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Properties p = new Properties();
        try (InputStream testIs = getClass().getResourceAsStream("/uritest.properties")) {
            p.load(testIs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (String key : p.stringPropertyNames()) {
            InvokeContext ctx = resolver.resolve(key, "GET");
            MockInvokeTarget target = (MockInvokeTarget) ctx.getTarget();
            String[] expected = p.getProperty(key).split(",");
            Assert.assertEquals(target.getTestData(), expected[0]);
            if (expected.length == 1) {
                // Assert.assertNull(ctx.getUriMatcher());
            } else {
                for (int i = 1, max = expected.length; i < max; i++) {
                    int idx = expected[i].indexOf('=');
                    if (idx < 0) {
                        throw new IllegalArgumentException("Illegal test descriptor line: " + key + "=" + p.getProperty(key));
                    }
                    String ek = expected[i].substring(0, idx);
                    String ev = expected[i].substring(idx + 1);
                    Assert.assertEquals(ev, ctx.getUriMatcher().group(ek));
                }
            }
        }

        long total = 0;
        for (int i = 0; i < 50; i++) {
            long dur = doBenchmark(resolver, p.stringPropertyNames());
            if (i > 39) {
                total += dur;
            }
        }
        System.out.println(resolver.getClass().getName() + ": " + (p.stringPropertyNames().size() * 20) + " / " + p.stringPropertyNames().size() + " uris resolved in " + (total / 1000000.0) + "ms");
    }

    @Test
    public void testRegexInvokeTargetResolver() {
        doTest(new RegexInvokeTargetResolver());
    }

    @Test
    public void testPrefixedInvokeTargetResolver() {
        doTest(new PrefixDictInvokeTargetResolver());
    }
}
