package main.Interfaces.InterfaceDataTransfer;

import main.DataStore.ClassInfo;

/**
 * Created by phili on 2016-02-16.
 */
public interface GroupDataTransfer {
    ClassInfo getClassInfoByName(String className);
}
