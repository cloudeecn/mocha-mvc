package works.cirno.mocha.mvc.resolver.prd;

import java.util.*;

/**
 *
 */
public class TreePrefixDict<T> implements PrefixDict<T> {

    protected HashMap<String, TreePrefixDict<T>> nodes = new HashMap<>();
    protected int size = 0;
    protected final ArrayList<T> values = new ArrayList<>(4);

    protected TreePrefixDict<T> createNode() {
        return new TreePrefixDict<T>();
    }

    private void changeSize(int newSize) {
        assert newSize > 0;
        assert newSize < size;
        int diffSize = size - newSize;
        HashMap<String, TreePrefixDict<T>> newNodes = new HashMap<>();
        for (Map.Entry<String, TreePrefixDict<T>> entry : nodes.entrySet()) {
            String key = entry.getKey();
            String newKey1 = key.substring(0, newSize);
            String newKey2 = key.substring(newSize);
            TreePrefixDict<T> node = newNodes.get(newKey1);
            if (node == null) {
                node = createNode();
                newNodes.put(newKey1, node);
            }
            node.size = diffSize;
            node.nodes.put(newKey2, entry.getValue());
        }
        this.nodes = newNodes;
        this.size = newSize;
    }

    protected int getNewSize(String prefix) {
        if (this.size == 0) {
            return prefix.length();
        } else {
            Set<String> others = nodes.keySet();
            int cps = Math.min(prefix.length(), size);
            all:
            for (String other : others) {
                for (int i = 0; i < cps; i++) {
                    if (prefix.charAt(i) != other.charAt(i)) {
                        cps = i;
                        if (cps == 0) {
                            break all;
                        } else {
                            break;
                        }
                    }
                }
            }
            return Math.min(cps + 1, Math.min(prefix.length(), size));
        }
    }

    public void add(String prefix, T value) {
        if (prefix.length() == 0) {
            values.add(value);
        } else if (this.size == 0) {
            this.size = prefix.length();
            TreePrefixDict<T> node = createNode();
            nodes.put(prefix, node);
            node.add("", value);
        } else {
            int newSize = getNewSize(prefix);
            if (newSize < size) {
                changeSize(newSize);
            }
            String key1 = prefix.substring(0, this.size);
            String key2 = prefix.substring(this.size);
            TreePrefixDict<T> node = nodes.get(key1);
            if (node == null) {
                node = createNode();
                nodes.put(key1, node);
            }
            node.add(key2, value);
        }
    }

    public List<T> select(String string) {
        return select(string, null);
    }

    public List<T> select(String string, List<T> values) {
        if (values == null) {
            values = new ArrayList<>();
        }
        values.addAll(this.values);
        int len = string.length();
        if (len >= size) {
            String prefix = string.substring(0, size);
            TreePrefixDict<T> node = nodes.get(prefix);
            if (node != null) {
                string = string.substring(size);
                node.select(string, values);
            }
        }
        return values;
    }

    public void printNodes(int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print("\t");
        }
        System.out.println("\"\": " + values);
        for (Map.Entry<String, TreePrefixDict<T>> entry : nodes.entrySet()) {
            for (int i = 0; i < indent; i++) {
                System.out.print("\t");
            }
            System.out.println("\"" + entry.getKey() + "\":");
            entry.getValue().printNodes(indent + 1);
        }
    }
}
