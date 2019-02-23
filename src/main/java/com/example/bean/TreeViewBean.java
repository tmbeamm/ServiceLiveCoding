package com.example.bean;

import java.util.List;

public class TreeViewBean {
	String name;
	List<TreeViewBean> children;
	boolean toggled;

	public boolean isToggled() {
		return toggled;
	}

	public void setToggled(boolean toggled) {
		this.toggled = toggled;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TreeViewBean> getChildren() {
		return children;
	}

	public void setChildren(List<TreeViewBean> children) {
		this.children = children;
	}
}
