package poc;

public class Remote {

    public void service(Function<String,String> str){
        str.apply("hello", "world...");
    }
}
