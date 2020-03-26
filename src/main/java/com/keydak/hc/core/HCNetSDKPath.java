package com.keydak.hc.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class HCNetSDKPath {

    @Value("${sdk.path}")
    public static String dllPath;//HCNetSDK.dll 文件路径

    public static void main(String[] args) {

        String path = (HCNetSDKPath.class.getResource("/HCNetSDK.dll").getPath())
                .replaceAll("%20", " ").substring(1).replace("/", "\\");
        System.out.println(path);
        try {
            dllPath = java.net.URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(dllPath);
    }
}
