package com.yinni.tlk.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {

    public static Gson instance = null;

    public static synchronized Gson providesGson() {
        if (null == instance) {
            instance = new GsonBuilder()
                    .registerTypeAdapterFactory(new GsonEosTypeAdapterFactory())
                    .excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        }
        return instance;
    }
}
