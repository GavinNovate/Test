package cn.autoio.test.tool;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by gavin on 2017/5/15.
 */

/**
 * 事件总线
 */
public class RxBus {

    private static final String TAG = "RxBus";

    private final Subject<Object> bus;

    private RxBus() {
        bus = PublishSubject.create().toSerialized();
    }

    /**
     * 获取单例
     *
     * @return 事件总线
     */
    public static RxBus get() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 发送数据
     *
     * @param target 目标类名
     * @param object 数据
     */
    public void post(@NonNull Class<?> target, @NonNull Object object) {
        bus.onNext(new Event(target, object));
    }

    /**
     * 监听数据
     *
     * @param current 当前类名
     * @param type    数据类型
     * @param <T>     类型
     * @return 监听对象
     */
    public <T> Observable<T> observe(@NonNull final Class<?> current, @NonNull Class<T> type) {
        return bus
                .ofType(Event.class)
                .filter(new Predicate<Event>() {
                    @Override
                    public boolean test(Event event) throws Exception {
                        return event.target.equals(current);
                    }
                })
                .map(new Function<Event, Object>() {
                    @Override
                    public Object apply(Event event) throws Exception {
                        return event.object;
                    }
                })
                .ofType(type);
    }

    /**
     * 取消绑定数据源，防止内存泄漏
     *
     * @param disposable 数据源取绑接口
     */
    public void dispose(Disposable disposable) {
        if (disposable == null) {
            return;
        }
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    /**
     * 批量取消绑定数据源，防止内存泄漏
     *
     * @param disposables 数据源取绑接口列表
     */
    public void dispose(List<Disposable> disposables) {
        if (disposables == null) {
            return;
        }
        for (Disposable disposable : disposables) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }

    /**
     * 事件
     */
    private static class Event {

        private Class<?> target;
        private Object object;

        Event(Class<?> target, Object object) {
            this.target = target;
            this.object = object;
        }
    }

    private static class SingletonHolder {
        private static final RxBus INSTANCE = new RxBus();
    }
}