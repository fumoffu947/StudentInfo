package main;

import main.Interfaces.Panel;

/**
 * Created by phili on 2016-01-15.
 */
public class PanelHistoryStore {

    // the maximum number of elements in the list
    private int maxSize = 10;
    // one more than max panels
    private Panel[] panelHistory = new Panel[maxSize];
    //the position of the front
    int frontPos = 0;
    //the position of the last element
    int backPos = 0;
    // the position of the current panel
    int currentPos = 0;


    public PanelHistoryStore() {
        panelHistory[currentPos] = null;
        assert modulo(maxSize) == 0 : "modulo of maxsize failed";
        assert  modulo(maxSize+1) == 1 : "modulo of maxsize+1 failed";
        assert modulo(-1) == maxSize-1 : "modulo of -1 failed";
        assert modulo(-22) == maxSize-2 : "modulo of -22 failed";
    }

    public void addPanel(Panel panel) {
        if (panelHistory[frontPos] == null) {
            panelHistory[frontPos] = panel;
            return;
        }
        if (modulo(currentPos) != modulo(frontPos)) {
            frontPos = modulo(currentPos);
        }
        if (modulo(frontPos + 1) == modulo(backPos)) {
            backPos = modulo(backPos+1);
        }
        frontPos = modulo(frontPos+1);
        currentPos = modulo(currentPos+1);
        panelHistory[frontPos] = panel;
    }

    /**
     * this will be calls then the previous panel wants to be acquired to back into the history
     * @return
     * returns a Panel if there are any left behind the currentPos else null
     */
    public Panel getPreviousPanel() {
        if (modulo(currentPos) != modulo(backPos)) {
            currentPos = modulo(currentPos-1);
            return panelHistory[currentPos];
        }
        return null;
    }

    /**
     * this will be calls then the subsequent panel wants to be acquired to back into the history
     * @return
     * returns a Panel if there are any in front of the currentPos else null
     */
    public Panel getSubsequentPanel() {
        if (modulo(currentPos) != modulo(frontPos)) {
            currentPos = modulo(currentPos+1);
            return panelHistory[currentPos];
        }
        return null;
    }

    public Panel getCurrentPanel() {
        return panelHistory[currentPos];
    }

    private int modulo(int num) {
        if (num < 0) {
            int tmp = num +  num *(-2);
            while (tmp >  maxSize) {
                tmp = tmp - maxSize;
            }
            return maxSize-tmp;
        }else {
            int tmp = num;
            while (tmp >= maxSize) {
                tmp = tmp - maxSize;
            }
            return tmp;
        }
    }
}