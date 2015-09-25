package works.cirno.mocha.resolver.prd;

import org.junit.Assert;
import org.junit.Test;

import works.cirno.mocha.resolver.prd.LongTreePrefixDict;
import works.cirno.mocha.resolver.prd.TreePrefixDict;

/**
 *
 */

public class TreePrefixDictTest {

    private void add(TreePrefixDict<String> prd, String prefix) {
        prd.add(prefix, prefix);
    }

    private void testDict(TreePrefixDict<String> prd) {
        System.out.println("Testing " + prd.getClass().getName());
        add(prd, "/json/user/manage");
        add(prd, "/json/user/manage/");
        add(prd, "/json/user/manage/add");
        add(prd, "/json/user/manage/add/1");
        add(prd, "/json/user/manage/add/2");
        add(prd, "/json/user/manage/add/3");
        add(prd, "/json/user/manage/delete");
        add(prd, "/json/user/manage/modify");
        add(prd, "/json/user");
        add(prd, "/json/unit");
        add(prd, "/json/unit/manage");
        add(prd, "/");
        add(prd, "/login");
        add(prd, "/login.do");
        add(prd, "aaaaaab");
        add(prd, "aaaaaac");
        add(prd, "aaaaaad");
        add(prd, "abaaaab");
        add(prd, "abaaaac");
        add(prd, "abaaaad");
        prd.printNodes(0);
        add(prd, "aaaa");
        add(prd, "aa");
        prd.printNodes(0);

        java.util.List<String> var = prd.select("/login");
        Assert.assertArrayEquals(var.toArray(new String[var.size()]), new String[]{"/", "/login"});
        var = prd.select("/login.do");
        Assert.assertArrayEquals(var.toArray(new String[var.size()]), new String[]{"/", "/login", "/login.do"});

        var = prd.select("/json/user/manage/add");
        Assert.assertArrayEquals(var.toArray(new String[var.size()]), new String[]{"/", "/json/user", "/json/user/manage", "/json/user/manage/", "/json/user/manage/add"});
        var = prd.select("/json/user/manage/45");
        Assert.assertArrayEquals(var.toArray(new String[var.size()]), new String[]{"/", "/json/user", "/json/user/manage", "/json/user/manage/"});
        var = prd.select("/json/user/manage/add/45");
        Assert.assertArrayEquals(var.toArray(new String[var.size()]), new String[]{"/", "/json/user", "/json/user/manage", "/json/user/manage/", "/json/user/manage/add"});
        var = prd.select("/json/user/manage/delete/45");
        Assert.assertArrayEquals(var.toArray(new String[var.size()]), new String[]{"/", "/json/user", "/json/user/manage", "/json/user/manage/", "/json/user/manage/delete"});
        var = prd.select("aaaaaac");
        Assert.assertArrayEquals(var.toArray(new String[var.size()]), new String[]{"aa", "aaaa", "aaaaaac"});
        var = prd.select("abaaaac");
        Assert.assertArrayEquals(var.toArray(new String[var.size()]), new String[]{"abaaaac"});
    }

    @Test
    public void testPrefixReverseDict() {
        TreePrefixDict<String> prd = new TreePrefixDict<>();
        testDict(prd);
    }

    @Test
    public void testLongPrefixDict() {
        LongTreePrefixDict<String> prd = new LongTreePrefixDict<>();
        testDict(prd);
    }
}
