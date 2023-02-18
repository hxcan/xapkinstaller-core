package com.stupidbeauty.xapkinstaller.core;

import android.os.Environment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.io.File;

/**
 * 一些基本信息的定义。
 * @author root
 *
 */
class XAPKManifest
{
	private ArrayList<XAPKPart> splitApks; //!< split apks list.

	public static final String LOG_BASE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"com.stupidbeauty.hxlauncher"+File.separator+"Log"; //!<日志目录的路径。
	
	public ArrayList<XAPKPart> getSplitApks()
	{
    return splitApks;
	} // public ArrayList<XAPKPart> getSplitApks()
}
