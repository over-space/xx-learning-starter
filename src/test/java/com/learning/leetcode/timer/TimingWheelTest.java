package com.learning.leetcode.timer;

import com.learning.BaseTest;
import org.testng.annotations.Test;

/**
 * @author 李芳
 * @since 2022/7/21
 */
public class TimingWheelTest extends BaseTest {

    @Test
    void test() {

        SystemTimer systemTimer = new SystemTimer();

        systemTimer.add(new TimerTask(350L));

        sleep(5);
    }
}
