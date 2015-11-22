package io.alstonlin.wildhacksproject;

import java.util.ArrayList;

public interface Command {
    public void exec(ArrayList<ImageItem> items);
}
