package com.myspring.pro30.mapper;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.log4j.Log4j;

@RunWith(SpringRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context2.xml")
@Log4j
public class ReplyMapperTests {
	
	@Autowired
	private SqlSession sqlSession;
	
	@Test
	public void testMapper() {
		log.info(sqlSession);
	}

}
