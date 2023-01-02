package com.learning.io.nio.sample;

import java.nio.channels.Channel;

public interface ChooseSelectorStrategy{

    SelectorThread chooseSelector(Channel channel);
}
