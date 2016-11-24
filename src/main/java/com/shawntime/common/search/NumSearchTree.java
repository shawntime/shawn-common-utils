package com.shawntime.common.search;

import java.util.List;
import java.util.Set;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class NumSearchTree {

	private Node root = new Node(-1, 0);
	
	/**
	 * 插入数据
	 * @param data
	 */
	public void insert(long data) {
		Node parentNode = root;
		String anchorId = String.valueOf(data);
		while(anchorId.length() > 0) {
			int length = anchorId.length();
			loop :
			for(int level=1; level<=length; ++level) {
				
				int index = (int)(anchorId.charAt(level - 1) - '0');
				
				List<Node> childNode = parentNode.getNextNode();
				if(childNode == null) {
					parentNode.nextNode = Lists.newArrayList();
				} else {
					for(Node tmpNode : childNode) {
						if(tmpNode.getLevel() == level && tmpNode.getIndex() == index) {
							tmpNode.addData(data);
							parentNode = tmpNode;
							continue loop;
						}
					}
				}
				
				Node node = new Node(index, level, data);
				parentNode.nextNode.add(node);
				parentNode = node;
			}
			
			anchorId = anchorId.substring(1, anchorId.length());
			parentNode = root;
		}
	}
	
	/**
	 * 查找数据
	 * @param key
	 * @return
	 */
	public Optional<Set<Long>> getNodes(String key) {
		
		int length = key.length();
		Node parentNode = root;
		loop:
		for(int level=1; level<=length; ++level) {
			int index = (int)(key.charAt(level - 1)-'0');
			List<Node> childNode = parentNode.getNextNode();
			while(childNode != null) {
				for(Node node : childNode) {
					if(node.getLevel() == level && node.getIndex() == index) {
						parentNode = node;
						continue loop;
					}
				}
				parentNode = null;
				break loop;
			}
			parentNode = null;
			break;
		}
		
		if(parentNode != null) {
			return Optional.fromNullable(parentNode.dataList);
		}
		
		return Optional.fromNullable(null);
	}
	
	
	class Node {
		
		private Node prevNode;
		private List<Node> nextNode = null;
		
		private int index;
		private int level;
		private Set<Long> dataList = Sets.newTreeSet();

		public Node() {
			super();
		}

		public Node(int index, int level) {
			super();
			this.index = index;
			this.level = level;
		}

		public Node(int index, int level, Long data) {
			super();
			this.index = index;
			this.level = level;
			dataList.add(data);
		}
		
		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public void addData(Long data) {
			dataList.add(data);
		}

		public Node getPrevNode() {
			return prevNode;
		}

		public void setPrevNode(Node prevNode) {
			this.prevNode = prevNode;
		}

		public List<Node> getNextNode() {
			return nextNode;
		}

		public void setNextNode(List<Node> nextNode) {
			this.nextNode = nextNode;
		}

		public Set<Long> getDataList() {
			return dataList;
		}

		public void setDataList(Set<Long> dataList) {
			this.dataList = dataList;
		}

		public int getLevel() {
			return level;
		}

		public void setLevel(int level) {
			this.level = level;
		}
	}
}
