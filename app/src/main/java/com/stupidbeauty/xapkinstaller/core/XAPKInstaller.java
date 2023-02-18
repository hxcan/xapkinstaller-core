package com.stupidbeauty.xapkinstaller.core;

import java.util.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipFile;
import java.util.Enumeration;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Random;
import static com.stupidbeauty.comgooglewidevinesoftwaredrmremover.Constants.Networks.RabbitMQPassword;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import java.io.OutputStream;
import android.content.IntentSender;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.view.View;
import android.content.IntentSender;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Looper;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.app.Application;
import android.os.Looper;
import com.stupidbeauty.voiceui.VoiceUi;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.Looper;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.os.Build;
import androidx.core.content.FileProvider;
import android.database.ContentObserver;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import androidx.core.content.FileProvider;
import android.database.ContentObserver;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import java.io.DataInputStream;
import com.stupidbeauty.hxlauncher.R;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import com.stupidbeauty.hxlauncher.R;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import org.apache.commons.io.FileUtils;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.stupidbeauty.hxlauncher.bean.ApplicationListData;
import org.apache.commons.io.FilenameUtils;
import java.io.File;

public class XAPKInstaller
{
  private static final String PACKAGE_INSTALLED_ACTION = "com.example.android.apis.content.SESSION_API_PACKAGE_INSTALLED";
  private String fullUrl = null; //!< Downloading full url.
  private Notification continiusNotification=null; //!<记录的通知
  private VoiceUi voiceUi=null; //!< 语音交互对象。
  private int NOTIFICATION = 84951; //!< 通知编号。陈欣
  private long MinimalApkSize = 250; //!< minimal accepted apk file size.

  public Future<File> fileDownloadFuture; //!< The file download future.
  private NotificationManager mNM;
  private Context baseApplication = null; //!< 上下文对象。
  private static final String TAG="XAPKInstaller"; //!<输出调试信息了时使用的标记
  private long downloadId; //!<当前的下载编号
  
  /**
  * Extract xapk file parts.
  */
  private ArrayList<XAPKPart> extractXapk(String downloadFilePath) throws ZipException
  {
    ArrayList<XAPKPart> result=new ArrayList();
    
    try
    {
//     Chen xin.

    ZipFile zipFile=new ZipFile(downloadFilePath);
    
    Enumeration<ZipArchiveEntry> entries=zipFile.getEntries();
    
    while(entries.hasMoreElements())
    {
      ZipArchiveEntry entry=(ZipArchiveEntry)(entries.nextElement());
      
//       XAPKPart xapkPart=new XAPKPart();
      
//       xapkPart.set

      String entryFioleName=entry.getName(); // Get file name.
      
      InputStream entryInputStream=zipFile.getInputStream(entry);
      
//       Chen xin. open the inputstream to file.

      File entryFile=new File(entryFioleName);
      
      FileUtils.copyInputStreamToFile(entryInputStream, entryFile);
    } // while(entries.hasMoreElements())
    
//     Chen xin, parse manifest.json 
    Gson gson=new Gson(); //创建gson对象。

    File manifestFile=new File("manifest.json");
    
    String text=FileUtils.readFileToString(manifestFile);
    XAPKManifest voiceRecognizeResult=gson.fromJson(text, XAPKManifest.class); // 解析成结果对象。
//     String saidText=voiceRecognizeResult.getSaidText(); //获取完整的说出内容。

//     voiceRecognizeResultString=voiceRecognizeResultString+saidText; //追加结果。

//     Log.i(TAG,"onResult, result: "+voiceRecognizeResultString+", is empty?: " + voiceRecognizeResultString.isEmpty()+ ", length: " + voiceRecognizeResultString.length()); //Debug.

//     boolean isLast=voiceRecognizeResult.isLs(); //获取属性，是否是最终结果。

    result=voiceRecognizeResult.getSplitApks();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    return result;
  } // private ArrayList<XAPKPart> extractXapk(String downloadFilePath)
  
  /**
    * 要求安装应用
    * @param downloadFilePath 应用安装包路径
    */
  public boolean installXapk(String downloadFilePath, IntentSender statusReceiver)
  {
    boolean result=false;
    
    try
    {
    ArrayList<XAPKPart> xapkParts=extractXapk(downloadFilePath);
    
    int xapkPartCounter=0;
    
    for(xapkPartCounter=0; xapkPartCounter< xapkParts.size(); xapkPartCounter++)
    {
      XAPKPart xapkPart=xapkParts.get(xapkPartCounter);
      
      String partFiePath=xapkPart.getFile();
      String partId=xapkPart.getId();
      
      requestInstallApi(partFiePath, statusReceiver, partId); // Request install by view.
    } // for(int xapkPartCounter=0; xapkPartCounter< xapkParts.length(); xapkPartCounter++)
    
    result=true;
    
    }
    catch(ZipException e)
    {
      e.printStackTrace();
    }
    
    return result;
  } //private void requestInstall(String downloadFilePath)

  /**
  * 要求安装应用
  * @param downloadFilePath 应用安装包路径
  */
  private void requestInstallApi(String downloadFilePath, IntentSender statusReceiver, String partId)
  {
    //   https://github.com/aosp-mirror/platform_development/blob/master/samples/ApiDemos/src/com/example/android/apis/content/InstallApkSessionApi.java

    PackageInstaller.Session session = null;
    try
    {
      PackageInstaller packageInstaller = baseApplication.getPackageManager().getPackageInstaller();
      PackageInstaller.SessionParams params = new PackageInstaller.SessionParams( PackageInstaller.SessionParams.MODE_FULL_INSTALL);
      int sessionId = packageInstaller.createSession(params);
      session = packageInstaller.openSession(sessionId);

      //                     addApkToInstallSession("HelloActivity.apk", session);
      addApkToInstallSession(downloadFilePath, session, partId);

      // Create an install status receiver.
      //                     Context context = InstallApkSessionApi.this;
//       Intent intent = new Intent(baseApplication, ApkInstallActivity.class);
//       intent.setAction(PACKAGE_INSTALLED_ACTION);
//       PendingIntent pendingIntent = PendingIntent.getActivity(baseApplication, 0, intent, 0);
//       IntentSender statusReceiver = pendingIntent.getIntentSender();

      // Commit the session (this will start the installation workflow).
      session.commit(statusReceiver);
    }
    catch (IOException e) 
    {
      throw new RuntimeException("Couldn't install package", e);
    }
    catch (RuntimeException e) 
    {
      if (session != null) 
      {
        session.abandon();
      }
      throw e;
    }

    //             Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
    //             intent.setData(getApkUri("HelloActivity.apk"));
    //             intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    //             startActivity(intent);
    //    
        
  } //private void requestInstall(String downloadFilePath)

  private void addApkToInstallSession(String assetName, PackageInstaller.Session session, String partId) throws IOException 
  {
    // It's recommended to pass the file size to openWrite(). Otherwise installation may fail
    // if the disk is almost full.
//     OutputStream packageInSession = session.openWrite("package", 0, -1);
    OutputStream packageInSession = session.openWrite(partId, 0, -1);
        
    File apkFileObject=new File(assetName);
            
    byte[] buffer=FileUtils.readFileToByteArray(apkFileObject);
            
    int n=buffer.length;
            
    packageInSession.write(buffer, 0, n);
                
    packageInSession.close();
  }

  public XAPKInstaller(Context context) 
  {
//     BlindBoxApplication baseApplication = BlindBoxApplication.getInstance(); //获取应用程序对象。
    baseApplication = context; // 获取应用程序对象。
  
    voiceUi=new VoiceUi(baseApplication); // 创建语音交互对象。
  } // public DownloadRequestor() 
}
