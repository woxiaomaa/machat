package com.ma.netty.bean;

import java.io.Serializable;

/**
 * Created by mh on 2019/2/14.
 */
public class ChatMsg implements Serializable{

	private static final long serialVersionUID = 5248981444735692154L;
	private String senderId;
	private String receiverId;
	private String msg;
	// 用于消息的签收
	private String msgId;
	
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	
}
