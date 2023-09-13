package com.synthetic.watermelon;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Log 工具类
 */
public class LogUtil{
    // Log路径
    public static String DebugLogFile;
	
	public static boolean DEBUG = true;

	public static void init(Context context){
        File f = context.getExternalFilesDir("debug");
        if(f==null)DebugLogFile = new File(context.getExternalFilesDir("log"),"debug.log").toString();
        else{
            if(!f.exists())f.mkdirs();
            DebugLogFile = f.toString();
        }
    }

    // 调试模式下才会进行输出
    public static void Log(String s){
        if(DEBUG)debugLog(s);
    }

    // 输出log
    public static void Log(String s,Throwable e){
        debugLog(s);
        Log(e);
    }

    // 输出到debug文件
    public static void debugLog(String string){
        File file = new File(DebugLogFile,"debug.log");
        RandomAccessFile raf = null;
        try{
            if(!file.exists()) file.createNewFile();
            raf = new RandomAccessFile(file,"rw");
            raf.seek(raf.length());
            raf.write((string+"\n").getBytes());
        }catch(IOException ignored){
        
        }finally{
            try{
                if(raf!=null) raf.close();
            }catch(IOException ignored){ }
        }
    }

    // log输出异常
    public static void Log(Throwable e){
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String str = stringWriter.toString();
        File f = new File(DebugLogFile,Integer.toString(str.hashCode())+".log");
        if(f.exists())debugLog(SimpleDateFormat.getInstance().format(new Date(System.currentTimeMillis()))+" 发生异常:"+str.hashCode());
        else{
            try{
                FileWriter w = new FileWriter(f);
                w.write(str);
                w.flush();
                w.close();
            }catch(IOException ignored){ }
            debugLog(SimpleDateFormat.getInstance().format(new Date(System.currentTimeMillis()))+" 发生异常:"+str.hashCode()+"\n"+e.getClass().getName()+" : "+e.getMessage());
        }
    }

}
