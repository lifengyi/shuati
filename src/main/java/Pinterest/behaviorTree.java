package Pinterest;

public class behaviorTree {
    public static void main(String[] args) {
        String path1 = "abc", path2 = "bca", path3 = "abd";

        TrieTree tree = new TrieTree();
        tree.add(path1);
        tree.add(path2);
        tree.add(path3);
        tree.dump();
    }
}

class TrieTree {

    class TrieNode {
        public char ch = '$';
        public int count = 0;
        public TrieNode[] child = null;
        public int childCount = 0;

        public TrieNode(char ch)  {
            this.ch = ch;
            this.child = new TrieNode[26];
        }
    }

    TrieNode root = null;

    public TrieTree() {
        root = new TrieNode('$');
    }

    public void add(String path) {
        TrieNode node = root;
        for(int i = 0; i < path.length(); ++i) {
            char ch = path.charAt(i);
            int index = ch - 'a';
            if(node.child[index] == null) {
                node.child[index] = new TrieNode(ch);
                node.childCount += 1;
            }
            node.child[index].count += 1;
            node = node.child[index];
        }
    }

    public void dump() {
        StringBuilder sb = new StringBuilder();
        dfs(root, sb);
    }

    private void dfs(TrieNode node, StringBuilder sb) {
        if(node.childCount == 0) {
            System.out.println(sb.toString());
            return;
        }

        for(int i = 0; i < 26; ++i) {
            if(node.child[i] != null) {
                int curIndex = sb.length();
                sb.append(node.child[i].ch).append(node.child[i].count);
                dfs(node.child[i], sb);
                sb.delete(curIndex, sb.length());
            }
        }
    }
}
