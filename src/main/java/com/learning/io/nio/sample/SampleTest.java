package com.learning.io.nio.sample;

import java.io.IOException;

public class SampleTest {


    public static void main(String[] args) throws IOException {

        // 1. 创建IO线程，指定线程数量
        // SelectorThreadGroup selectorThreadGroup = new SelectorThreadGroup(1);
        SelectorThreadGroup selectorThreadGroup = new SelectorThreadGroup(3);

        // 2. 指定selector绑定策略
        ChooseSelectorStrategy chooseSelectorStrategy = new AcceptHandlerChooseSelectorStrategy(selectorThreadGroup.selectorThreads);
        selectorThreadGroup.setChooseSelectorStrategy(chooseSelectorStrategy);

        // 3. 绑定端口
        selectorThreadGroup.bind(9090);
    }

}
