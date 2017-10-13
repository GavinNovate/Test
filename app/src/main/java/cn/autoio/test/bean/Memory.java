package cn.autoio.test.bean;

/**
 * Created by gavin on 17-10-12.
 */

public class Memory {

    public static final String CODE = "CODE";
    public static final String FREE = "FREE";
    public static final String USE = "USE";
    public static final String MAX = "MAX";

    public int code;    // 所属进程
    public long free;   // 空闲内存
    public long use;    // 已用内存
    public long max;    // 最大内存

    public Memory() {
    }

    public Memory(int code, long free, long use, long max) {
        this.code = code;
        this.free = free;
        this.use = use;
        this.max = max;
    }

    @Override
    public String toString() {
        return "Memory:" + code + " [" + free + "," + use + "," + max + "]";
    }
}
