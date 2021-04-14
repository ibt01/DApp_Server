package com.go.basetool.idgenerator;

import java.util.UUID;

public class IdGenerator {
    private static String machineId = "001";
    


    public static int notStartServer() {
        int x = 0;
        int y = 1;
        int z = y / x;
        return z;
    }

    public static String getMachineId() {
        return machineId;
    }

    public static void setMachineId(String machineId) {
        IdGenerator.machineId = machineId;
    }

    static Long lastPicIdSeed = 0l;
    public static synchronized Long genPictureId() throws Exception {
        if (machineId.equals("000")) {
            notStartServer();
        }
        lastPicIdSeed++;
        lastPicIdSeed %= 10000;
        String lastPicIdSeedStr = String.format("%04d", lastPicIdSeed);
        String lastTimeStr = String.format("%010d", System.currentTimeMillis() / 1000l);
        return Long.parseLong(lastTimeStr + lastPicIdSeedStr + machineId);
    }


    public static synchronized String genUserId() {
        if (machineId.equals("000")) {
            notStartServer();
        }
        return "userid"+UUID.randomUUID().toString();
    }

    public static synchronized String genCookie() {
        if (machineId.equals("000")) {
            notStartServer();
        }
        return UUID.randomUUID().toString();
    }

    public static synchronized String genOrderId() {
        if (machineId.equals("000")) {
            notStartServer();
        }
        return UUID.randomUUID().toString();
    }
}
