package poc;

public class FunctionManager {

    /**
     *  远程调用方法
     */
    public void remote(){
        Remote proxy = new Remote();
        proxy.service(this::callback);
    }

    /**
     * 回调方法
     * @param a
     * @param b
     */
    public void callback(String a,String b){
        System.out.println(a+"   "+ b);
    }

    public static void main(String[] args) {
        FunctionManager manager = new FunctionManager();
        manager.remote();
    }
}

