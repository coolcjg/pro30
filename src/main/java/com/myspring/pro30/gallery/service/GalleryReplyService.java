package com.myspring.pro30.gallery.service;

import java.util.List;

import com.myspring.pro30.board.vo.Criteria;
import com.myspring.pro30.board.vo.ReplyVO;


public interface GalleryReplyService {
	
	public int register(ReplyVO vo);
	
	public ReplyVO get(int rno);
	
	public int modify(ReplyVO vo);
	
	public int remove(int rno);
	
	public List<ReplyVO> getList(Criteria cri, int articleNO);
	
	public void removeAllRepGallery(int articleNO);

}
