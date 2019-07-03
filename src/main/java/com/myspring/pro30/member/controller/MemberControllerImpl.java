package com.myspring.pro30.member.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myspring.pro30.board.vo.Criteria;
import com.myspring.pro30.member.service.MemberService;
import com.myspring.pro30.member.vo.MemberVO;

import lombok.extern.log4j.Log4j;

@Log4j
@Controller("memberController")
public class MemberControllerImpl implements MemberController{
	private static final Logger logger = LoggerFactory.getLogger(MemberControllerImpl.class);
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	MemberVO memberVO;
	
	@RequestMapping(value= {"/", "/main.do"}, method=RequestMethod.GET)
	private ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String viewName = (String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}

	@Override
	@RequestMapping(value="/member/listMembers.do", method=RequestMethod.GET)
	public ModelAndView listMembers(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String viewName = (String)request.getAttribute("viewName");
		List membersList = memberService.listMembers();
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("membersList", membersList);
		return mav;
	}

	@Override
	@RequestMapping(value="/member/addMember.do", method=RequestMethod.POST)
	public ModelAndView addMember(MultipartHttpServletRequest multipartRequest, 
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		multipartRequest.setCharacterEncoding("utf-8");
		Map<String, Object> articleMap = new HashMap<String, Object>();
		Enumeration enu = multipartRequest.getParameterNames();
		
		while(enu.hasMoreElements()) {
			String name = (String)enu.nextElement();
			String value = multipartRequest.getParameter(name);
			System.out.println(name+ " : " + value);
			articleMap.put(name,value);
		}
		
		MemberVO member = new MemberVO();
		member.setId((String)articleMap.get("id"));
		member.setPwd((String)articleMap.get("pwd"));
		member.setName((String)articleMap.get("name"));
		member.setEmail((String)articleMap.get("email"));
		
		int result=0;
		result = memberService.addMember(member);
		ModelAndView mav = new ModelAndView("redirect:/member/listMembers.do");
		return mav;
	}
	
	

	@Override
	@RequestMapping(value="/member/removeMember.do", method=RequestMethod.GET)
	public ModelAndView removeMember(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response) throws Exception{
		request.setCharacterEncoding("utf-8");
		memberService.removeMember(id);
		ModelAndView mav = new ModelAndView("redirect:/member/listMembers.do");
		return mav;
	}
	
	
	@Override
	@RequestMapping(value="/member/login.do", method = RequestMethod.POST) 
	public ModelAndView login(@ModelAttribute("member") MemberVO member, @ModelAttribute("cri") Criteria cri, RedirectAttributes rAttr,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		ModelAndView mav = new ModelAndView();
		memberVO = memberService.login(member);
		
		if(memberVO != null) {
			HttpSession session = request.getSession();
			session.setAttribute("member", memberVO);
			session.setAttribute("isLogOn", true);
			String action = (String)session.getAttribute("action");
			session.removeAttribute("action");
			if(action!=null) {
				mav.setViewName("redirect:"+action);
			}else {
				mav.setViewName("redirect:/main.do");
			}
		}else {
			rAttr.addAttribute("result", "loginFailed");
			mav.setViewName("redirect:/member/loginForm.do");
		}
			return mav;
	}
	
	@Override
	@RequestMapping(value="/member/logout.do", method=RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HttpSession session = request.getSession();
		session.removeAttribute("member");
		session.removeAttribute("isLogOn");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/board/listArticlesWithPaging.do");
		return mav;
	}
	
	@RequestMapping(value="/member/*Form.do", method=RequestMethod.GET)
	public ModelAndView form(
			@RequestParam(value="result", required=false) String result,
			@RequestParam(value="action", required=false) String action,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String viewName = (String)request.getAttribute("viewName");
		HttpSession session = request.getSession();
		session.setAttribute("action", action);
		ModelAndView mav = new ModelAndView();
		mav.addObject("result", result);
		mav.setViewName(viewName);
		return mav;
	}
	
	@Override
	@RequestMapping(value="/member/info.do", method=RequestMethod.GET)
	public ModelAndView info(@RequestParam(value="id") String id, HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.info(id);
		
		memberVO = memberService.view(id);
		
		String viewName = (String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		mav.addObject("member", memberVO);
		return mav;
	}
	
	
	@Override
	@RequestMapping(value="/member/modMember.do", method=RequestMethod.POST)
	public ResponseEntity mod(MultipartHttpServletRequest multipartRequest, MemberVO memberVO, HttpServletResponse response) throws Exception{
		
		HttpSession session = multipartRequest.getSession();
		MemberVO sessionMemberVO = (MemberVO)session.getAttribute("member");
		
		String id = sessionMemberVO.getId();
		
		memberVO.setId(id);

		log.info(memberVO.toString());
		
		memberService.mod(memberVO);
		
		session.setAttribute("member", memberVO);
		
		ResponseEntity resEnt;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type","text/html; charset=utf-8" );
		
		
		String message="";
		message = "<script>";
		message += " alert('수정완료');";
		message += " location.href='"+multipartRequest.getContextPath()+"/main.do'";
		message += "</script>";
		resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
		return resEnt;		
		
	}
	
	
	


	private String getViewName(HttpServletRequest request) throws Exception {
		String contextPath = request.getContextPath();
		System.out.println("contextPath = " + contextPath);
		
		String uri = (String) request.getAttribute("javax.servlet.include.request_uri");
		System.out.println("uri = " + uri);
		
		if (uri == null || uri.trim().equals("")) {
			uri = request.getRequestURI();
			System.out.println("새로받은 uri = " + uri);
		}

		int begin = 0;
		if (!((contextPath == null) || ("".equals(contextPath)))) {
			begin = contextPath.length();
			System.out.println("begin = " + begin);
		}

		int end;
		if (uri.indexOf(";") != -1) {
			end = uri.indexOf(";");
		} else if (uri.indexOf("?") != -1) {
			end = uri.indexOf("?");
		} else {
			end = uri.length();
			System.out.println("end = " + end);
		}

		String viewName = uri.substring(begin, end);
		System.out.println("viewName = " + viewName);
		if (viewName.indexOf(".") != -1) {
			viewName = viewName.substring(0, viewName.lastIndexOf("."));
			System.out.println("새로받은 viewName = " + viewName);
		}
		if (viewName.lastIndexOf("/") != -1) {
			viewName = viewName.substring(viewName.lastIndexOf("/", 1), viewName.length());
			System.out.println("새로받은 viewName = " + viewName);
		}
		return viewName;
	}
	
	
	
	
	
	
	

}
