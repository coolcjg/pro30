package com.myspring.pro30.gallery.vo;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.myspring.pro30.board.vo.BoardAttachVO;

@Component("GalleryVO")
public class GalleryVO {
	
	private int articleNO;
	private String title;
	private String content;
	private Date writeDate;
	private String id;
	private List<BoardAttachVO> attachList;
	
	
	
	public int getArticleNO() {
		return articleNO;
	}
	public void setArticleNO(int articleNO) {
		this.articleNO = articleNO;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getWriteDate() {
		return writeDate;
	}
	public void setWriteDate(Date writeDate) {
		this.writeDate = writeDate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<BoardAttachVO> getAttachList() {
		return attachList;
	}
	public void setAttachList(List<BoardAttachVO> attachList) {
		this.attachList = attachList;
	}
	@Override
	public String toString() {
		return "GalleryVO [articleNO=" + articleNO + ", title=" + title + ", content=" + content + ", writeDate="
				+ writeDate + ", id=" + id + ", attachList=" + attachList + "]";
	}
	
	
	
	
	
}
