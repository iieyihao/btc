package ewhine.service;


import ewhine.models.DirFilesProcessor;

import java.util.TimeZone;

public class Main {

	private static String BASE_DIR = "/Users/liuyihao/WorkSpace/xiaoqiwang/my_master/btc_files/";

	public static void main(String[] args) {
		// 启动顺序
		TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
		TimeZone.setDefault(timeZone);

		// 1.打开配置服务
		ConfigService config_service = new ConfigService();
		config_service.start();

		for (int i=0;i<8;i++) {
			String dir = BASE_DIR + i;
			new DirFilesProcessor(dir).start();
			System.out.println("start processing " + i);
		}

		try {
			Thread.sleep(5000);
			System.out.println("OK...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


}
