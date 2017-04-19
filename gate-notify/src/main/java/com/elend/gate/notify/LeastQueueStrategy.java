package com.elend.gate.notify;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elend.gate.notify.mapper.NQueueMapper;

/**
 * 选择队列数据最少
 * 
 * @author liyongquan 2015年6月4日
 */
@Component
public class LeastQueueStrategy implements QueueStrategy {
    @Autowired
    private NQueueMapper queueMapper;

    @Override
    public int getQueueIndex(String orderId) {
        List<QueueIndex> lists = new ArrayList<QueueIndex>();
        for (int i = 0; i < QueueSetting.THREAD_COUNT; i++) {
            int queueIndex = i + 1;
            int count = queueMapper.countAll(queueIndex);
            QueueIndex vo = new QueueIndex();
            vo.setIndex(queueIndex);
            vo.setCount(count);
            lists.add(vo);
        }
        // 排序
        TreeSet<QueueIndex> treeSet = new TreeSet<QueueIndex>(
                                                              new Comparator<QueueIndex>() {
                                                                  @Override
                                                                  public int compare(
                                                                          QueueIndex o1,
                                                                          QueueIndex o2) {
                                                                      return o1.getCount() > o2.getCount() ? 1
                                                                          : -1;
                                                                  }
                                                              });
        treeSet.addAll(lists);
        return treeSet.first().getIndex();
    }

    public class QueueIndex {

        private int index;

        private int count;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
