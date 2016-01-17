package main.Interfaces;

import javax.swing.*;

/**
 * Created by phili on 2016-01-15.
 */
public interface Panel {

    void clearMenuBar(JMenuBar jMenuBar);

    void setupMenuBar(JMenuBar jMenuBar);

    JPanel getPageHolder();
}
