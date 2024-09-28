package net.sliceclient.ac.processor;

import net.sliceclient.ac.check.data.ACPlayer;

public interface Processor {
    void handle(ProcessedData data);
    ACPlayer player();
    boolean registered();
}