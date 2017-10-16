package cn.autoio.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.autoio.test.bean.Memory;
import cn.autoio.test.service.ProcessService01;
import cn.autoio.test.service.ProcessService02;
import cn.autoio.test.service.ProcessService03;
import cn.autoio.test.service.ProcessService04;
import cn.autoio.test.service.ProcessService05;
import cn.autoio.test.service.ProcessService06;
import cn.autoio.test.service.ProcessService07;
import cn.autoio.test.service.ProcessService08;
import cn.autoio.test.service.ProcessService09;
import cn.autoio.test.service.ProcessService10;
import cn.autoio.test.service.ProcessService11;
import cn.autoio.test.service.ProcessService12;
import cn.autoio.test.service.ProcessService13;
import cn.autoio.test.service.ProcessService14;
import cn.autoio.test.service.ProcessService15;
import cn.autoio.test.service.ProcessService16;
import cn.autoio.test.service.ProcessService17;
import cn.autoio.test.service.ProcessService18;
import cn.autoio.test.service.ProcessService19;
import cn.autoio.test.service.ProcessService20;
import cn.autoio.test.service.ProcessService21;
import cn.autoio.test.service.ProcessService22;
import cn.autoio.test.service.ProcessService23;
import cn.autoio.test.service.ProcessService24;
import cn.autoio.test.service.ProcessService25;
import cn.autoio.test.service.ProcessService26;
import cn.autoio.test.service.ProcessService27;
import cn.autoio.test.service.ProcessService28;
import cn.autoio.test.service.ProcessService29;
import cn.autoio.test.service.ProcessService30;
import cn.autoio.test.service.ProcessService31;
import cn.autoio.test.service.ProcessService32;
import cn.autoio.test.tool.RxBus;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    public static final String ACTION_MEMORY_ADD = "ADD";
    public static final String ACTION_MEMORY_SUB = "SUB";
    public static final String ACTION_MEMORY_CLEAR = "CLEAR";

    private TextView threadView;
    private Button threadAddView;
    private Button threadSubView;
    private Button threadClearView;

    private TextView memoryView;
    private Button memoryAddView;
    private Button memorySubView;
    private Button memoryClearView;

    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;

    private List<Disposable> disposables = new ArrayList<>();

    private List<Subject<Boolean>> threadList = new ArrayList<>();
    private Map<Integer, Memory> memoryMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        RxBus.get()
                .observe(MainActivity.class, Memory.class)
                .map(new Function<Memory, Map<Integer, Memory>>() {
                    @Override
                    public Map<Integer, Memory> apply(Memory memory) throws Exception {
                        memoryMap.put(memory.code, memory);
                        return memoryMap;
                    }
                })
                .throttleLast(1, TimeUnit.SECONDS)
                .map(new Function<Map<Integer, Memory>, Long>() {
                    @Override
                    public Long apply(Map<Integer, Memory> integerMemoryMap) throws Exception {
                        long totalMemory = 0;
                        for (Memory memory : integerMemoryMap.values()) {
                            totalMemory += memory.use;
                        }
                        totalMemory += Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                        return totalMemory;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(Long value) {
                        memoryView.setText(getString(R.string.memory, 1.0 * value / 1024 / 1024));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        threadView.setText(getString(R.string.thread, threadList.size()));
        memoryView.setText(getString(R.string.memory, 1.0 * (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024));

        Glide.with(this).load(R.drawable.check).asGif().into(imageView1);
        Glide.with(this).load(R.drawable.check).asGif().into(imageView2);
        Glide.with(this).load(R.drawable.check).asGif().into(imageView3);
        Glide.with(this).load(R.drawable.check).asGif().into(imageView4);
    }

    private void initView() {

        threadView = (TextView) findViewById(R.id.thread);
        threadAddView = (Button) findViewById(R.id.threadAdd);
        threadSubView = (Button) findViewById(R.id.threadSub);
        threadClearView = (Button) findViewById(R.id.threadClear);

        memoryView = (TextView) findViewById(R.id.memory);
        memoryAddView = (Button) findViewById(R.id.memoryAdd);
        memorySubView = (Button) findViewById(R.id.memorySub);
        memoryClearView = (Button) findViewById(R.id.memoryClear);

        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView4 = (ImageView) findViewById(R.id.imageView4);

        threadAddView.setOnClickListener(this);
        threadSubView.setOnClickListener(this);
        threadClearView.setOnClickListener(this);

        memoryAddView.setOnClickListener(this);
        memorySubView.setOnClickListener(this);
        memoryClearView.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Subject<Boolean> subject : threadList) {
            if (!subject.hasComplete()) {
                subject.onComplete();
            }
        }
        for (Disposable disposable : disposables) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.threadAdd:
                addThread();
                threadView.setText(getString(R.string.thread, threadList.size()));
                break;
            case R.id.threadSub:
                subThread();
                threadView.setText(getString(R.string.thread, threadList.size()));
                break;
            case R.id.threadClear:
                clearThread();
                threadView.setText(getString(R.string.thread, threadList.size()));
                break;
            case R.id.memoryAdd:
                addMemory();
                break;
            case R.id.memorySub:
                subMemory();
                break;
            case R.id.memoryClear:
                clearMemory();
                break;
        }
    }

    private void addThread() {
        Subject<Boolean> subject = PublishSubject.create();
        subject
                .subscribe(new Observer<Boolean>() {

                    volatile boolean loop;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(Boolean value) {
                        loop = value;
                        if (value) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while (loop) ;
                                }
                            }).start();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        loop = false;
                    }
                });
        subject.onNext(true);
        threadList.add(subject);
    }

    private void subThread() {
        if (threadList.size() > 0) {
            threadList.get(0).onComplete();
            threadList.remove(0);
        }
    }

    private void clearThread() {
        Iterator<Subject<Boolean>> iterator = threadList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onComplete();
            iterator.remove();
        }
    }

    private void addMemory() {
        startService(newIntent(ProcessService01.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService02.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService03.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService04.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService05.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService06.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService07.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService08.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService09.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService10.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService11.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService12.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService13.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService14.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService15.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService16.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService17.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService18.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService19.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService20.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService21.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService22.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService23.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService24.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService25.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService26.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService27.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService28.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService29.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService30.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService31.class, ACTION_MEMORY_ADD));
        startService(newIntent(ProcessService32.class, ACTION_MEMORY_ADD));
    }

    private void subMemory() {
        startService(newIntent(ProcessService01.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService02.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService03.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService04.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService05.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService06.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService07.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService08.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService09.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService10.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService11.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService12.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService13.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService14.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService15.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService16.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService17.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService18.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService19.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService20.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService21.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService22.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService23.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService24.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService25.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService26.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService27.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService28.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService29.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService30.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService31.class, ACTION_MEMORY_SUB));
        startService(newIntent(ProcessService32.class, ACTION_MEMORY_SUB));
    }

    private void clearMemory() {
        startService(newIntent(ProcessService01.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService02.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService03.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService04.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService05.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService06.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService07.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService08.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService09.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService10.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService11.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService12.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService13.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService14.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService15.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService16.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService17.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService18.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService19.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService20.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService21.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService22.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService23.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService24.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService25.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService26.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService27.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService28.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService29.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService30.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService31.class, ACTION_MEMORY_CLEAR));
        startService(newIntent(ProcessService32.class, ACTION_MEMORY_CLEAR));
    }

    private Intent newIntent(Class<?> cls, String action) {
        return new Intent(this, cls).setAction(action);
    }
}
