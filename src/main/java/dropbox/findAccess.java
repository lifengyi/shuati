package dropbox;


import java.util.*;

/*
// /A
// |___ /B
//     |___ /C <-- access
//     |___ /D
// |___ /E <-- access
//     |___ /F
//         |___ /G


// folders = [
//   ('A', None),-google 1point3acres
//   ('B', 'A'),
//   ('C', 'B'),
//   ('D', 'B'),
//   ('E', 'A'),
//   ('F', 'E'),
// ]. From 1point 3acres bbs

// access = set(['C', 'E'])

// has_access(String folder_name) -> boolean. 1point3acres.com/bbs

// has_access("B") -> false
// has_access("C") -> true
// has_access("F") -> true
// has_access("G") -> true
*/

public class findAccess {

    Map<String, String> map = new HashMap<>();

    public static void main(String[] args) {
        // System.out.println("test");
        String[][] folders = {{"A", null}, {"B", "A"}, {"C", "B"}, {"D", "B"},{"E", "A"},{"F", "E"}, {"G", "F"}};

        Map<String, String> graph = buildMap(folders);
        Set<String> access = new HashSet<>(Arrays.asList("C", "E"));

        System.out.println(hasAccess("B", graph, access));
        System.out.println(hasAccess("C", graph, access));
        System.out.println(hasAccess("F", graph, access));
        System.out.println(hasAccess("G", graph, access));
    }

    public static Map<String, String> buildMap(String[][] folders) {
        Map<String, String> graph = new HashMap<>();
        for (String[] folder : folders) {
            graph.put(folder[0], folder[1]);
        }
        return graph;
    }

    public static boolean hasAccess(String folder, Map<String, String> graph, Set<String> access) {
        if (access.contains(folder)) return true;
        if (folder == null) return false;
        boolean res = hasAccess(graph.get(folder), graph, access);
        if (res) access.add(folder);
        return res;
    }
}
