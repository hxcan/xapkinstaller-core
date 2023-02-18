package com.stupidbeauty.hxlauncher.bean;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

// import com.stupidbeauty.comgooglewidevinesoftwaredrmremover.InstalledPackageLoadTask;
// import com.stupidbeauty.hxlauncher.InstalledPackageLoadTaskInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.stupidbeauty.hxlauncher.interfaces.LocalServerListLoadListener;

public class ApplicationListData implements Serializable
{
	private HashSet<String> urlSet=new HashSet<>(); //!<网址集合。

	public HashMap<String, Drawable> getLaunchIconMap() {
		return launchIconMap;
	}

	private final HashMap<String,Drawable> launchIconMap=new HashMap<>(); //!<启动图标缓存。

	/**
	 * 删除被卸载的软件包。
	 */
	public void removePackage(String packageName)
	{
		for (PackageInfo packageInfo: packageInfoList) //一个个地检查。
		{
			String currentPackageName=packageInfo.packageName; //获取软件包名字。

			if (currentPackageName.equals(packageName)) //正是这个软件包被卸载了。
			{
				packageInfoList.remove(packageInfo); //从列表中删除。

				break; //跳出。
			} //if (currentPackageName.equals(packageName)) //正是这个软件包被卸载了。
		} //for (PackageInfo packageInfo:packageInfoList) //一个个地检查。

		notifyApplicationList();
	} //public void removePackage(int uid)

	/**
	 * 将对象加入到本地服务器载入完毕的回调列表中。
	 * @param localServerListLoadListener 要加入的回调对象。
	 */
	public void addLocalServerListLoadListener(LocalServerListLoadListener localServerListLoadListener)
	{
    if (localServerListLoadListenerList.contains(localServerListLoadListener)) // Already in it
    {
    } // if (localServerListLoadListenerList.contains(localServerListLoadListener)) // Already in it
    else // not in it
    {
      localServerListLoadListenerList.add(localServerListLoadListener); //加入列表。
    } // else // not in it
	} //public void addLocalServerListLoadListener(LocalServerListLoadListener localServerListLoadListener)

	private final ArrayList<LocalServerListLoadListener> localServerListLoadListenerList=new ArrayList<>(); //!<本地服务器列表载入完毕监听器列表。

	/**
	 * 设置显示数据。
	 */
	public void notifyApplicationList()
	{
    for(LocalServerListLoadListener localServerListLoadListener:localServerListLoadListenerList) //一个个地通知。
    {
      localServerListLoadListener.onLoadPackageInfoList(); // 载入完毕。
    } //for(LocalServerListLoadListener localServerListLoadListener:localServerListLoadListenerList) //一个个地通知。
	} //private void notifyApplicationList()

	private final Context mContext; //!<上下文。

	public ApplicationListData(Context context) 
	{
		mContext=context;
	}

	private final List<PackageInfo> packageInfoList = new ArrayList<>(); //!<软件包列表。

	public List<PackageInfo> getPackageInfoList() {
		return packageInfoList;
	}

	/**
	 * 是否已经包含这个网址。
	 * @param fullUrl 要检查的网址。
	 * @return 是否已经包含。
	 */
	public boolean containsUrl(String fullUrl)
	{
		return urlSet.contains(fullUrl);
	} //public boolean containsUrl(String fullUrl)

	/**
	 * 记录，已经请求下载这个网址。
	 * @param fullUrl 完整的网址。
	 */
	public void addUrl(String fullUrl)
	{
		urlSet.add(fullUrl);
	} //public void addUrl(String fullUrl)


	/**
	 * 删除网址
	 * @param fullUrl 要删除的网址
	 */
	public void removeUrl(String fullUrl)
	{
		urlSet.remove(fullUrl); //删除
	} //public void removeUrl(String fullUrl)

}
