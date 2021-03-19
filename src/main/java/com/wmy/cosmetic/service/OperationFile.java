package com.wmy.cosmetic.service;

import org.junit.Test;

import java.io.*;
import java.util.*;

public class OperationFile {
    public static void main(String[] args) throws IOException {
        copyFile("D://1.jpg","D://2.png");
    }

    public static void copyFile(String fromPath,String toPath) throws IOException {
        FileInputStream fis=new FileInputStream(fromPath);
        FileOutputStream fos=new FileOutputStream(toPath);
        int len=0;
        byte[] bytes=new byte[1024*4];
        while ((len=fis.read(bytes))!=-1){
             fos.write(bytes,0,len);
        }
        //释放资源
        fos.close();
        fis.close();

    }

//    public static void main(String[] args) throws IOException {
//        FileInputStream fis=new FileInputStream("D:/idea_workplace/cosmeticResources/avatarImage/1.png");
//        FileOutputStream fos=new FileOutputStream("D:/idea_workplace/cosmeticResources/avatarImage2/1.png");
//        int len=0;
//        while ((len=fis.read())!=-1){
//            fos.write(len);
//        }
//        //释放资源
//        fos.close();
//        fis.close();
//    }
    @Test
    public static void main1(String[] args) throws IOException {
        Properties properties = new Properties();
        Map<String,String> map=new HashMap<>();
        BufferedReader bf = null;
        BufferedWriter bw = null;
        FileReader fr=null;
        FileWriter fw=null;
        try {
            fr=new FileReader("D:/data/file/student.txt");
            fw=new FileWriter("D:/data/file/newstudent.txt");
            bf = new BufferedReader(fr);
            bw = new BufferedWriter(fw);
            properties.load(bf);
            // 返回Properties中包含的key-value的Set视图
            Set<Map.Entry<Object, Object>> set = properties.entrySet();
            for (Map.Entry<Object, Object> entry : set) {
                String key=(String) entry.getKey();
                String value=(String) entry.getValue();
                if (key.equals("刘芳")){
                    value="18";
                }
                map.put(key, value);
            }
            Set<String> keySet = map.keySet(); //把map集合的key转换为set集合，方便遍历
            properties=new Properties();
            for (String in : keySet) {
                properties.setProperty(in,map.get(in));
            }
            properties.store(bw,"test");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("文件不存在或文件读取写入失败");
        } finally {
            bw.close();
            bf.close();
            fw.close();
            fr.close();
        }
    }
}
