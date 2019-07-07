package com.myspring.pro30.freeboard.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myspring.pro30.board.vo.BoardAttachVO;
import com.myspring.pro30.board.vo.Criteria;
import com.myspring.pro30.board.vo.PageDTO;
import com.myspring.pro30.freeboard.service.FreeReplyService;
import com.myspring.pro30.freeboard.service.FreeService;
import com.myspring.pro30.gallery.vo.GalleryVO;
import com.myspring.pro30.member.vo.MemberVO;

import lombok.extern.log4j.Log4j;

@Controller("FreeController")
@Log4j
public class FreeControllerImpl implements FreeController{

	@Autowired
	FreeService freeService;
	@Autowired
	GalleryVO galleryVO;
	@Autowired
	FreeReplyService freeReplyService;

	
	
	@Override
	@RequestMapping(value="/free/list.do", method= {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response, Criteria cri) throws Exception{
		String viewName = (String)request.getAttribute("viewName");
		cri.setAmount(9);
		
		List list = freeService.list(cri);
		
		ModelAndView mav = new ModelAndView(viewName);
		
		int total = freeService.serviceGetTotal(cri);
		
		
		mav.addObject("galleryList", list);
		mav.addObject("pageMaker", new PageDTO(cri, total));
		return mav;
	}
	
	@RequestMapping(value="/free/form.do", method=RequestMethod.GET)
	private ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String viewName = (String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}
	
	

	
	//갤러리 게시글 추가
	@Override
	@RequestMapping(value="/free/add.do" ,method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity  add(GalleryVO galleryVO, MultipartHttpServletRequest multipartRequest, HttpServletResponse response) throws Exception {
		multipartRequest.setCharacterEncoding("utf-8");
		Map<String,Object> articleMap = new HashMap<String, Object>();
		
		Enumeration enu=multipartRequest.getParameterNames();
		
		
		while(enu.hasMoreElements()){
			String name=(String)enu.nextElement();
			String value=multipartRequest.getParameter(name);
			log.info(name + " : " + value);
			articleMap.put(name,value);
		}
	
		HttpSession session = multipartRequest.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("member");
		
		String id = memberVO.getId();
		articleMap.put("id",id);
		
		log.info("===============================");
		log.info("register : " + memberVO );
		log.info("register : " + galleryVO );
				
		if(galleryVO.getAttachList()!=null) {
			galleryVO.getAttachList().forEach(
						attach->{
							log.info(attach.getFileName());
						});
		}
		log.info("===============================");
		
		freeService.add(articleMap, galleryVO);
				
		String message;
		ResponseEntity resEnt=null;
		HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.add("Content-Type", "text/html; charset=utf-8");
	    
	    
	    message = "<script>";
		message += " alert('새글 작성 완료');";
		message += " location.href='"+multipartRequest.getContextPath()+"/free/list.do'; ";
		message +=" </script>";
	    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);	    
	 
		return resEnt;
	}
	
	//갤러리 게시글 보기
	@RequestMapping(value="/free/view.do", method=RequestMethod.GET)
	public ModelAndView view(@RequestParam("articleNO") int articleNO, @ModelAttribute("cri") Criteria cri, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String viewName = (String) request.getAttribute("viewName");
		
		
		galleryVO = freeService.view(articleNO);
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		mav.addObject("article", galleryVO);
		return mav;
	}		
	
	//게시글 볼 때 첨부파일 가져오기 기능
	@GetMapping(value="/free/getAttachList.do", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody //json으로 데이터를 받기 위해 ResponseBody 사용
	public ResponseEntity<List<BoardAttachVO>> getAttachList(int articleNO){
		log.info("getAttachList articleNO : " + articleNO );
		return new ResponseEntity<>(freeService.getAttachList(articleNO), HttpStatus.OK);
	}
	
	//수정폼 불러오기
	@GetMapping("/free/mod.do")
	public ModelAndView modArticleForm(@RequestParam("articleNO")int articleNO, @ModelAttribute("cri") Criteria cri,  HttpServletRequest request, HttpServletResponse response ) throws Exception{
		String viewName = (String) request.getAttribute("viewName");
				
		galleryVO = freeService.view(articleNO);
		
		log.info("GalleryController에서 가져온 게시글 정보------------");
		log.info(galleryVO.toString());
		log.info("--------------------------------------------" );
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		mav.addObject("article", galleryVO);
		return mav;		
	}	
	
	
	//mod.jsp에서 수정반영하기 눌렀을 때 설정.
	@RequestMapping(value="/free/modArticle.do", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity modArticle(MultipartHttpServletRequest multipartRequest, GalleryVO galleryVO, @ModelAttribute("Criteria") Criteria cri, HttpServletResponse response) throws Exception{
		log.info("modify : " + galleryVO.toString());
		
		multipartRequest.setCharacterEncoding("utf-8");
		
		Map<String,Object> articleMap = new HashMap<String, Object>();
		Enumeration enu = multipartRequest.getParameterNames();
		
		//파라미터값을 map 객체에 저장.
		log.info("articleMap에 저장되는 값들--------------------");
		while(enu.hasMoreElements()) {
			String name = (String)enu.nextElement();
			String value = multipartRequest.getParameter(name);
			log.info(name + " = "+value);
			articleMap.put(name, value);
		}
		log.info("-----------------------------------------");
		
		 
		HttpSession session = multipartRequest.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("member");
		String id = memberVO.getId();
		log.info("articleMap에 넣어질 id 값 : " + id);
		articleMap.put("id", id);
		
		String articleNO = (String)articleMap.get("articleNO");
		
		int amount = cri.getAmount();
		int pageNum = cri.getPageNum();
		String type = cri.getType();
		String keyword = cri.getKeyword();
		
		log.info("페이지 파라미터-----------------------");
		log.info("amount : " + amount);
		log.info("pageNum : " + pageNum);
		log.info("type : " + type);
		log.info("keyword : " + keyword);
		log.info("----------------------------------");
		
		
		freeService.modify(galleryVO, articleMap);
		
		ResponseEntity resEnt = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		
		String message="";
		message = "<script>";
		message += " alert('수정완료');";
		message += " location.href='"+multipartRequest.getContextPath()+"/free/view.do?articleNO="+articleNO+"&amount="+amount+"&pageNum="+pageNum+"&type="+type+"&keyword="+keyword+"';";
		message += "</script>";
		resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		return resEnt;

	}	
	
	
	//게시글 삭제
	@PostMapping("/free/remove.do")
	public String remove(@RequestParam("articleNO") int articleNO, Criteria cri, RedirectAttributes rttr) throws Exception {
		log.info("remove.... : " + articleNO);
		
		List<BoardAttachVO> attachList = freeService.getAttachList(articleNO);
		
		//게시글에 등록된 댓글 모두 삭제
		freeReplyService.removeAllRepGallery(articleNO);
		 
		if(freeService.remove(articleNO)) {
			deleteFiles(attachList);
			rttr.addFlashAttribute("result", "success");
		}

		return "redirect:/free/list.do"+cri.getListLink();
	}
	
	
	
	//갤러리 글 삭제시 첨부파일 삭제함수 
	private void deleteFiles(List<BoardAttachVO> attachList) {
		if(attachList == null || attachList.size() ==0) {
			return;
		}
		
		log.info("delete attach files..................");
		log.info(attachList);
		
		attachList.forEach(attach->{
			try {
				Path file = Paths.get("C:\\upload\\"+attach.getUploadPath()+"\\" + attach.getUuid()+"_"+attach.getFileName());
				Files.deleteIfExists(file);
				
				if(Files.probeContentType(file).startsWith("image")) {
					Path thumbNail = Paths.get("C:\\upload\\"+attach.getUploadPath()+"\\s_"+attach.getUuid()+"_"+attach.getFileName());
					Files.delete(thumbNail);
				}

				
			}catch(Exception e) {
				log.error("delete file error : " + e.getMessage());
				
			}
			
		});
	}
}
