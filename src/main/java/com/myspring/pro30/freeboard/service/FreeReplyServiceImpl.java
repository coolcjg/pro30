package com.myspring.pro30.freeboard.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myspring.pro30.board.vo.Criteria;
import com.myspring.pro30.board.vo.ReplyVO;

import lombok.extern.log4j.Log4j;

@Service("FreeReplyService")
@Log4j
public class FreeReplyServiceImpl implements FreeReplyService {
	
	@Autowired
	private SqlSession sqlSession;

	@Override
	public int register(ReplyVO vo) {
		log.info("register ......... : " + vo);
		return sqlSession.insert("mapper.reply.freeinsert", vo);
	}

	@Override
	public ReplyVO get(int rno) {
		log.info("get ......... : " + rno);
		return sqlSession.selectOne("mapper.reply.freeread", rno);
	}

	@Override
	public int modify(ReplyVO vo) {
		log.info("modify ......... : " + vo);
		
		log.info("articleNO :  " + vo.getArticleNO());
		log.info("id :  " + vo.getId());
		log.info("reply :  " + vo.getReply());
		log.info("rno :  " + vo.getRno());
		log.info("replydate :  " + vo.getReplyDate());
		log.info("updatedate :  " + vo.getUpdateDate());
		
		int result = sqlSession.update("mapper.reply.freeupdate", vo);
		
		log.info("result :  " + result);
		
		return result;
	}

	@Override
	public int remove(int rno) {
		log.info("remove ......... : " + rno);
		return sqlSession.delete("mapper.reply.freedelete", rno);
	}

	@Override
	public List<ReplyVO> getList(Criteria cri, int articleNO) {
		log.info("get Reply List of a Board : " + articleNO);
		return sqlSession.selectList("mapper.reply.freegetListWithPaging", articleNO);
	}
	
	@Override
	public void removeAllRepGallery(int articleNO) {
		log.info("remove all articleNO : " + articleNO);
		sqlSession.delete("mapper.reply.removeAllRepFree", articleNO);
	}
}
