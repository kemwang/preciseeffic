package com.ruqimobility.precisiontest.utils;

import com.ruqimobility.precisiontest.service.impl.CallGraphServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 队列工具类
 *
 */
public class QueueUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueUtil.class);

    /**
     * 初始化有界队列队列
     */

    private LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(100);


    public void addTask(Runnable task) throws InterruptedException {
        queue.put(task);
    }

    public void processTasks() {
        while (!queue.isEmpty()) {
            try {
                Runnable task = queue.take();
                task.run();
            } catch (InterruptedException e) {
                LOGGER.error("queue run error：{}",e.getMessage());
            }
        }
    }
}
