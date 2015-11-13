package main.Comparators;

import main.ClassInfo;

import java.util.Comparator;

/**
 * Created by fumoffu on 2015-10-24.
 */
public class ClassInfoComparator implements Comparator<ClassInfo>
{
    @Override public int compare(final ClassInfo o1, final ClassInfo o2) {
	return o1.getClassName().compareTo(o2.getClassName());
    }
}
