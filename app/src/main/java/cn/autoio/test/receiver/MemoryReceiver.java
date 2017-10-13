package cn.autoio.test.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.autoio.test.MainActivity;
import cn.autoio.test.bean.Memory;
import cn.autoio.test.tool.RxBus;

public class MemoryReceiver extends BroadcastReceiver {

    public static final String ACTION = "MemoryReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null && intent.getAction().equals(ACTION)) {
            Memory memory = new Memory();
            memory.code = intent.getIntExtra(Memory.CODE, 0);
            memory.free = intent.getLongExtra(Memory.FREE, 0L);
            memory.use = intent.getLongExtra(Memory.USE, 0L);
            memory.max = intent.getLongExtra(Memory.MAX, 0L);
            RxBus.get().post(MainActivity.class, memory);
        }
    }

}
