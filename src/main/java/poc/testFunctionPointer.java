package poc;

import java.util.*;

class testFunctionPointer {

    interface Command {
        void runCommand(int p1, int p2);
    }

    public static void main(String[] args) throws Exception {
        Map<Character, Command> methodMap = new HashMap<>();

        methodMap.put('p', new Command(){
           public void runCommand(int p1, int p2) {
               System.out.println(p1 + p2);
           }
        });
        methodMap.put('m', new Command(){
            public void runCommand(int p1, int p2) {
                System.out.println(p1 - p2);
            }
        });

        methodMap.get('p').runCommand(3,5);
        methodMap.get('m').runCommand(5, 2);
    }
}
