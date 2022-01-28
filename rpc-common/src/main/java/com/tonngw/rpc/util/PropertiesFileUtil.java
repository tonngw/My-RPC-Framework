package com.tonngw.rpc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Properties 文件工具类
 * 
 * @author tonngw
 * @date 2022-01-28 18:53
 */
public class PropertiesFileUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesFileUtil.class);
    
    private PropertiesFileUtil() {
    }

    public static Properties readPropertiesFile(String fileName) {
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        System.out.println(url);
        String rpcConfigPath = "";
        if (url != null) {
            rpcConfigPath = url.getPath() + fileName;
            System.out.println(rpcConfigPath);
        }
        new Properties();
        Properties properties = null;
        try (InputStreamReader inputStreamReader = new InputStreamReader(
                new FileInputStream(rpcConfigPath), StandardCharsets.UTF_8)) {
            properties = new Properties();
            properties.load(inputStreamReader);
        } catch (IOException e) {
            logger.error("occur exception when read properties file [{}]", fileName);
        }
        return properties;
    }
}
