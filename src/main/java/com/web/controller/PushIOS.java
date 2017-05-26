package com.web.controller;

import java.util.ArrayList;
import java.util.List;

import javapns.Push;
import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;

import org.apache.commons.lang.StringUtils;


public class PushIOS {


	private static List<String> tokens = new ArrayList<String>();
	private static String relativelyPath = System.getProperty("user.dir");
	private static String certificatePath = relativelyPath + "/src/main/webapp/apns_dev.p12";
	//此处注意导出的证书密码不能为空因为空密码会报错
	private static String certificatePassword = "abc123";

	static {
		tokens.add("baef1ca72caa7e2fc850c4b01668ea95462650280557c60319ac8f4cb2a68f12");// 5s
		//tokens.add("205a16a274d39eb9c375e45f4e98d32fb9e48862d16d6dfd8d3cd435c0313f5f");// plus
	}

	public static void main(String[] args) throws Exception {
		pushAlert();
		// pushSilent();
	}

	static void pushAlert() {
		String alert = "给你发信息了";// push的内容
		int badge = 1;// 图标小红圈的数值
		String sound = "default";// 铃音

		boolean sendCount = (tokens.size() == 1);

		try {
			PushNotificationPayload payLoad = new PushNotificationPayload(alert, badge, sound);
			payLoad.addAlert(alert); // 消息内容
			payLoad.addBadge(badge); // iphone应用图标上小红圈上的数值
			if (!StringUtils.isBlank(sound)) {
				payLoad.addSound(sound);// 铃音
			}
			PushNotificationManager pushManager = new PushNotificationManager();
			// true：表示的是产品发布推送服务 false：表示的是产品测试推送服务
			pushManager
					.initializeConnection(new AppleNotificationServerBasicImpl(
							certificatePath, certificatePassword, false));
			List<PushedNotification> notifications = new ArrayList<PushedNotification>();
			// 发送push消息
			if (sendCount) {
				Device device = new BasicDevice();
				device.setToken(tokens.get(0));
				PushedNotification notification = pushManager.sendNotification(
						device, payLoad, true);
				notifications.add(notification);
			} else {
				List<Device> device = new ArrayList<Device>();
				for (String token : tokens) {
					device.add(new BasicDevice(token));
				}
				notifications = pushManager.sendNotifications(payLoad, device);
			}
			pushManager.stopConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void pushSilent() {
		try {
			Push.contentAvailable(certificatePath, certificatePassword, false,
					tokens);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	}
}
	
}
