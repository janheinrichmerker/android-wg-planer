package com.heinrichreimersoftware.wg_planer.structure;

import com.heinrichreimersoftware.wg_planer.utils.Utils;

import java.text.Collator;
import java.util.Comparator;

public class SubjectComparator implements Comparator<String> {
    @Override
    public int compare(String string1, String string2) {
        if (string1.equals(string2)) return 0;

        for (int i = 0; i < Math.min(string1.length(), string2.length()); i++) {
            if (Utils.compareCase(string1.charAt(i), string2.charAt(i)) != 0)
                return Utils.compareCase(string1.charAt(i), string2.charAt(i));
        }
        return Collator.getInstance().compare(string1, string2);
    }

    @Override
    public boolean equals(Object object) {
        return this == object || !(object == null || getClass() != object.getClass());
    }
}
