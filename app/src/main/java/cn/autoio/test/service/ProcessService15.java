package cn.autoio.test.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;

import cn.autoio.test.bean.Memory;
import cn.autoio.test.receiver.MemoryReceiver;

public class ProcessService15 extends Service {

    public static final String ACTION_ADD = "ADD";
    public static final String ACTION_SUB = "SUB";
    public static final String ACTION_CLEAR = "CLEAR";

    private List<byte[]> byteList = new ArrayList<>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_ADD:
                    // 防止OOM
                    if (Runtime.getRuntime().maxMemory() > Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() + 1024 * 1024 * 2)
                        byteList.add(new byte[1024 * 1024]);
                    break;
                case ACTION_SUB:
                    if (byteList.size() > 0) {
                        byteList.remove(0);
                    }
                    Runtime.getRuntime().gc();
                    break;
                case ACTION_CLEAR:
                    byteList.clear();
                    Runtime.getRuntime().gc();
                    break;
            }
            sendBroadcast();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void sendBroadcast() {
        Intent intent = new Intent();
        intent.setAction(MemoryReceiver.ACTION);
        intent.putExtra(Memory.CODE, 15);
        intent.putExtra(Memory.FREE, Runtime.getRuntime().freeMemory());
        intent.putExtra(Memory.USE, Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        intent.putExtra(Memory.MAX, Runtime.getRuntime().maxMemory());
        sendBroadcast(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Unsupported Operation");
    }
}
