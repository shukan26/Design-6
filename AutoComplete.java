/**
 * Builds a Trie where each node stores top 3 most frequent sentences for that prefix.
 * During insertion, each node maintains a sorted list (by frequency desc, lex asc) of top results.
 * Query is O(L) by traversing prefix; insertion is O(L * log 3) ≈ O(L).
 *
 * Time Complexity: O(L) per input/query, L = length of sentence/prefix
 * Space Complexity: O(N * L) for Trie storage
 */
class AutocompleteSystem {
    HashMap<String, Integer> map;
    String search;
    TrieNode root;

    class TrieNode {
        TrieNode[] children;
        List<String> top3Results;

        public TrieNode() {
            this.children = new TrieNode[256]; // supports extended ASCII
            this.top3Results = new ArrayList<>();
        }
    }

    private void insert(String word) {
        TrieNode curr = root;
        for (char c : word.toCharArray()) {
            if (curr.children[c - ' '] == null) {
                curr.children[c - ' '] = new TrieNode();
            }
            curr = curr.children[c - ' '];

            List<String> list = curr.top3Results;

            // Only add if not already present to avoid duplicates per node
            if (!list.contains(word)) {
                list.add(word);
            }

            // Maintain invariant: list always sorted by (freq desc, lex asc)
            Collections.sort(list, (a, b) -> {
                if (map.get(a).equals(map.get(b))) return a.compareTo(b);
                return map.get(b) - map.get(a);
            });

            // Keep only top 3 to ensure O(1) query time later
            if (list.size() > 3) list.remove(list.size() - 1);
        }
    }

    private List<String> searchPrefix(String prefix) {
        TrieNode curr = root;
        for (char c : prefix.toCharArray()) {
            // Early exit: prefix path does not exist
            if (curr.children[c - ' '] == null) return new ArrayList<>();
            curr = curr.children[c - ' '];
        }
        // Directly return cached top 3 results → no recomputation needed
        return curr.top3Results;
    }

    public AutocompleteSystem(String[] sentences, int[] times) {
        this.search = "";
        this.map = new HashMap<>();
        this.root = new TrieNode();

        for (int i = 0; i < sentences.length; i++) {
            map.put(sentences[i], map.getOrDefault(sentences[i], 0) + times[i]);
            insert(sentences[i]); // build Trie with ranking info
        }
    }

    public List<String> input(char c) {
        if (c == '#') {
            // Commit current search string and update frequency
            map.put(search, map.getOrDefault(search, 0) + 1);
            insert(search); // reinsert to update rankings along the Trie
            search = "";
            return new ArrayList<>();
        }

        search += c;
        return searchPrefix(search); // O(L) lookup
    }
}