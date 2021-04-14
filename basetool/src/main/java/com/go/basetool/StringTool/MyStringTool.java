package com.go.basetool.StringTool;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MyStringTool {
    public static boolean splitcontains(String parent, String isson) {
        if (StringUtils.isEmpty(parent) || StringUtils.isEmpty(isson)) {
            return false;
        }

        String[] parents = parent.split(",");
        Set<String> setParents = new HashSet<>();
        setParents.addAll(Arrays.asList(parents));
        return setParents.contains(isson.trim());
    }
}
