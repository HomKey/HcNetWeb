package com.keydak.hc.core;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class HCNetSDKPath {
    public static String DLL_PATH;

    static {
//        String path = HCNetSDKPath.class.getResource("/")
//                .getPath()
//                .replaceAll("%20"," ")
//                .substring(1)
//                .replace("/","\\")
//                + File.separator;
        String path = HCNetSDKPath.class.getResource("/")
                .getPath()
                .replaceAll("%20"," ")
                .substring(6)
                .replace("/","\\");
        path = "D:\\lib\\";
        System.out.println("path:" + path);
        try {
            DLL_PATH = java.net.URLDecoder.decode(path, "utf-8");
            System.out.println("DDL_PATH:" + DLL_PATH);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
