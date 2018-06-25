package ewhine.service;


import ewhine.models.FileArrayProcessor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

public class Main {

	public static String BASE_DIR = "/Users/liuyihao/WorkSpace/xiaoqiwang/my_master/btc/t/";

	public static void main(String[] args) {
		// 启动顺序
		TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
		TimeZone.setDefault(timeZone);

		// 1.打开配置服务
		ConfigService config_service = new ConfigService();
		config_service.start();

//		for (int i=0;i<8;i++) {
//			String dir = BASE_DIR + i;
//			new FileArrayProcessor(dir).start();
//			System.out.println("start processing " + i);
//		}
		List<List<String>> fList = getFileList(20);
		for (int i = 0; i < fList.size(); i++) {
			new FileArrayProcessor(fList.get(i)).start();
		}

		try {
			Thread.sleep(5000);
			System.out.println("OK...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static List<List<String>> getFileList(int targ) {
		List<List<String>>mEndList=new ArrayList<>();

		File dirFile = new File(BASE_DIR);
		if (!dirFile.exists()) {
			System.out.println(BASE_DIR + " not exist");
			return mEndList ;
		}
		if (!dirFile.isDirectory()) {
			System.out.println(BASE_DIR + " is not a dirctory");
			return mEndList;
		}

		List<String> flist = Arrays.asList(dirFile.list());

		if( flist.size()%targ!=0) {
			for (int j = 0; j < flist.size() / targ + 1; j++) {
				if ((j * targ + targ) < flist.size()) {
					mEndList.add(flist.subList(j * targ, j * targ + targ));//0-3,4-7,8-11    j=0,j+3=3   j=j*3+1
				} else if ((j * targ + targ) > flist.size()) {
					mEndList.add(flist.subList(j * targ, flist.size()));
				} else if (flist.size() < targ) {
					mEndList.add(flist.subList(0, flist.size()));
				}
			}
		}else if(flist.size()%targ==0){
			for (int j = 0; j < flist.size() / targ; j++) {
				if ((j * targ + targ) <= flist.size()) {
					mEndList.add(flist.subList(j * targ, j * targ + targ));//0-3,4-7,8-11    j=0,j+3=3   j=j*3+1
				} else if ((j * targ+ targ) > flist.size()) {
					mEndList.add(flist.subList(j * targ, flist.size()));
				} else if (flist.size() < targ) {
					mEndList.add(flist.subList(0, flist.size()));
				}
			}
		}
		for (int i = 0; i < mEndList.size(); i++) {
			System.out.println(mEndList.get(i).size());
		}
		return mEndList;
	}

}
