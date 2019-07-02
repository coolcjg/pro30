package com.myspring.pro30.gallery.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.myspring.pro30.board.vo.BoardAttachVO;
import com.myspring.pro30.board.vo.Criteria;
import com.myspring.pro30.board.vo.PageDTO;
import com.myspring.pro30.gallery.service.GalleryService;
import com.myspring.pro30.gallery.vo.GalleryVO;
import com.myspring.pro30.member.vo.MemberVO;

import lombok.extern.log4j.Log4j;

@Controller("GalleryController")
@Log4j
public class GalleryControllerImpl implements GalleryController{
	private static final String ARTICLE_IMAGE_REPO = "C:\\board\\article_image";
	
	@Autowired
	GalleryService galleryService;
	@Autowired
	GalleryVO galleryVO;
	
	
	@Override
	@RequestMapping(value="/gallery/list.do", method= {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response, Criteria cri) throws Exception{
		String viewName = (String)request.getAttribute("viewName");
		List list = galleryService.list(cri);
		
		ModelAndView mav = new ModelAndView(viewName);
		
		int total = galleryService.serviceGetTotal(cri);
		
		
		mav.addObject("galleryList", list);
		mav.addObject("pageMaker", new PageDTO(cri, total));
		return mav;
	}
	
	
	@RequestMapping(value="/gallery/galleryForm.do", method=RequestMethod.GET)
	private ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String viewName = (String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}
	
	//갤러리 게시글 추가
	@Override
	@RequestMapping(value="/gallery/add.do" ,method = RequestMethod.POST)
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
		
		galleryService.add(articleMap, galleryVO);
				
		String message;
		ResponseEntity resEnt=null;
		HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.add("Content-Type", "text/html; charset=utf-8");
	    
	    
	    message = "<script>";
		message += " alert('새글 작성 완료');";
		message += " location.href='"+multipartRequest.getContextPath()+"/gallery/list.do'; ";
		message +=" </script>";
	    resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);	    
	 
		return resEnt;
	}
	
	//갤러리 메인페이지 썸네일 보여주기
	@GetMapping("/gallery/displaythumb.do")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String articleNO) throws Exception{
		
		int no = Integer.parseInt(articleNO);
							
		BoardAttachVO boardAttachVO = galleryService.thumbnail(no);
		String fileName = boardAttachVO.getUploadPath()+"/"+boardAttachVO.getUuid()+"_"+boardAttachVO.getFileName();
				
		File file = new File("c:\\upload\\"+fileName);
		
		log.info("file: " + file);
		
		ResponseEntity<byte[]> result = null;
		
		try {
			HttpHeaders header = new HttpHeaders();
			
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return result;
		
	}
	
	
	
	
	
	/*
	@RequestMapping(value="/board/viewArticle.do", method=RequestMethod.GET)
	public ModelAndView viewArticle(@RequestParam("articleNO") int articleNO, @ModelAttribute("cri") Criteria cri, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String viewName = (String) request.getAttribute("viewName");
		
		
		articleVO = boardService.viewArticle(articleNO);
		
		System.out.println("BoardController에서 가져온 게시글 정보------------" );
		System.out.println("ArticleNO = "+articleVO.getArticleNO() );
		System.out.println("title = "+articleVO.getTitle() );
		System.out.println("content = "+articleVO.getContent() );
		System.out.println("imageFileName = "+articleVO.getImageFileName() );
		System.out.println("id = "+articleVO.getId() );
		System.out.println("writeDate = "+articleVO.getWriteDate() );
		System.out.println("--------------------------------------------" );
		
		
		

		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		mav.addObject("article", articleVO);
		return mav;
	}	
	
	@GetMapping("/board/modArticleForm.do")
	public ModelAndView modArticleForm(@RequestParam("articleNO")int articleNO, @ModelAttribute("cri") Criteria cri,  HttpServletRequest request, HttpServletResponse response ) throws Exception{
		String viewName = (String) request.getAttribute("viewName");
				
		articleVO = boardService.viewArticle(articleNO);
		
		System.out.println("BoardController에서 가져온 게시글 정보------------" );
		System.out.println("ArticleNO = "+articleVO.getArticleNO() );
		System.out.println("title = "+articleVO.getTitle() );
		System.out.println("content = "+articleVO.getContent() );
		System.out.println("imageFileName = "+articleVO.getImageFileName() );
		System.out.println("id = "+articleVO.getId() );
		System.out.println("writeDate = "+articleVO.getWriteDate() );
		System.out.println("--------------------------------------------" );
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		mav.addObject("article", articleVO);
		return mav;		
	}
	
	
	@PostMapping("/board/remove.do")
	public String remove(@RequestParam("articleNO") int articleNO, Criteria cri, RedirectAttributes rttr) throws Exception {
		log.info("remove.... : " + articleNO);
		
		List<BoardAttachVO> attachList = boardService.getAttachList(articleNO);
		 
		if(boardService.remove(articleNO)) {
			deleteFiles(attachList);
			rttr.addFlashAttribute("result", "success");
		}
		
		return "redirect:/board/listArticlesWithPaging.do"+cri.getListLink();
		
	}
		
	
	//게시글 볼 때 첨부파일 가져오기 기능
	@GetMapping(value="/board/getAttachList.do", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody //json으로 데이터를 받기 위해 ResponseBody 사용
	public ResponseEntity<List<BoardAttachVO>> getAttachList(int articleNO){
		log.info("getAttachList articleNO : " + articleNO );
		return new ResponseEntity<>(boardService.getAttachList(articleNO), HttpStatus.OK);
	}
	



	
	  




	
	
	
	@Override
	@RequestMapping(value="/board/removeArticle.do", method=RequestMethod.POST)
	@ResponseBody 
	public ResponseEntity removeArticle(@RequestParam("articleNO") int articleNO, @ModelAttribute("cri") Criteria cri, HttpServletRequest request, HttpServletResponse response) throws Exception{
		response.setContentType("text/html; charset=UTF-8");
		String message;
		ResponseEntity resEnt = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		
		
		
		try {
			boardService.removeArticle(articleNO);
			File destDir = new File(ARTICLE_IMAGE_REPO+"\\"+articleNO);
			FileUtils.deleteDirectory(destDir);
			
			message = "<script>";
			message +="alert('삭제 완료');";
			message +="location.href='"+request.getContextPath()+"/board/listArticlesWithPaging.do?amount="+cri.getAmount()+"&pageNum="+cri.getPageNum()+"&type="+cri.getType()+"&keyword="+cri.getKeyword()+"';";
			message +="</script>";
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			
		}catch(Exception e) {
			message = "<script>";
			message +="alert('삭제하지 못했습니다.');";
			message +="location.href='"+request.getContextPath()+"/board/listArticlesWithPaging.do';";
			message +="</script>";
			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			e.printStackTrace();
			
		}
		return resEnt;
	}
	
	
	@RequestMapping(value="/board/modArticle.do", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity modArticle(MultipartHttpServletRequest multipartRequest, ArticleVO articleVO, @ModelAttribute("Criteria") Criteria cri, HttpServletResponse response) throws Exception{
		log.info("modify : " + articleVO);
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
		
		
		boardService.modify(articleVO, articleMap);
		
		ResponseEntity resEnt = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");
		
		String message="";
		message = "<script>";
		message += " alert('수정완료');";
		message += " location.href='"+multipartRequest.getContextPath()+"/board/viewArticle.do?articleNO="+articleNO+"&amount="+amount+"&pageNum="+pageNum+"&type="+type+"&keyword="+keyword+"';";
		message += "</script>";
		resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		return resEnt;
		
		
	}	
	
	
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
	

	
	
	
	private String upload(MultipartHttpServletRequest multipartRequest) throws Exception{
		
		String imageFileName = null;
		Map<String, String> articleMap = new HashMap<String, String>();
		Iterator<String> fileNames = multipartRequest.getFileNames();
		
		while(fileNames.hasNext()) {
			String fileName = fileNames.next();
			MultipartFile mFile = multipartRequest.getFile(fileName);
			imageFileName = mFile.getOriginalFilename();
			File file = new File(ARTICLE_IMAGE_REPO+"\\"+fileName);
			if(mFile.getSize()!=0) {
				if(! file.exists()) {
					if(file.getParentFile().mkdirs()) {
						file.createNewFile();
					}
				}
				mFile.transferTo(new File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName));	
			}			
		}
		return imageFileName;
	}
	
	*/
}
