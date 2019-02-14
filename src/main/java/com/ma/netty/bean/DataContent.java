package com.ma.netty.bean;

import java.io.Serializable;

/**
 * Created by mh on 2019/2/14.
 */
public class DataContent implements Serializable {

	private static final long serialVersionUID = 2589631446852392136L;

	private Integer action;
	private ChatMsg chatMsg;
	private String extend;
	
	public Integer getAction() {
		return action;
	}
	public void setAction(Integer action) {
		this.action = action;
	}
	public ChatMsg getChatMsg() {
		return chatMsg;
	}
	public void setChatMsg(ChatMsg chatMsg) {
		this.chatMsg = chatMsg;
	}
	public String getExtend() {
		return extend;
	}
	public void setExtend(String extend) {
		this.extend = extend;
	}
}
