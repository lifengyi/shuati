package poc;


/**
 * 函数接口
 * @author chuer
 *
 * @param <T>
 * @param <E>
 */
@FunctionalInterface
public interface Function<T,E> {
    public void apply(T t, E e);
}