package com.myspring.pro30.board.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myspring.pro30.board.vo.Criteria;
import com.myspring.pro30.board.vo.ReplyVO;

import lombok.extern.log4j.Log4j;

@Service("ReplyService")
@Log4j
public class ReplyServiceImpl implements ReplyService {
	
	@Autowired
	private SqlSession sqlSession;

	@Override
	public int register(ReplyVO vo) {
		log.info("register ......... : " + vo);
		return sqlSession.insert("mapper.reply.insert", vo);
	}

	@Override
	public ReplyVO get(int rno) {
		log.info("get ......... : " + rno);
		return sqlSession.selectOne("mapper.reply.read", rno);
	}

	@Override
	public int modify(ReplyVO vo) {
		log.info("modify ......... : " + vo);
		
		int result = sqlSession.update("mapper.reply.update", vo);
		
		log.info("result :  " + result);
		
		return result;
	}

	@Override
	public int remove(int rno) {
		log.info("remove ......... : " + rno);
		return sqlSession.delete("mapper.reply.delete", rno);
	}

	@Override
	public List<ReplyVO> getList(Criteria cri, int articleNO) {
		log.info("get Reply List of a Board : " + articleNO);
		return sqlSession.selectList("mapper.reply.getListWithPaging", articleNO);
	}
	
	@Override
	public void removeAll(int articleNO) throws Exception{
		sqlSession.delete("mapper.reply.removeAllRep", articleNO);
	}
}
