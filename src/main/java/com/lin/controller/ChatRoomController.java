package com.lin.controller;

import com.lin.domain.AnchorModel;
import com.lin.domain.Chatroom;
import com.lin.service.ChatRoomService;
import org.aspectj.apache.bcel.util.ClassLoaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tongho on 2017/5/2.
 * 聊天室数据操作
 */


@Controller
public class ChatRoomController {

    @Autowired
    private ChatRoomService chatRoomService;

    /**
     * 将聊天室发送的消息进行存储
     * @param request
     * @return
     */
    @RequestMapping("/sendMessage.do")
   public  @ResponseBody Map<String,Object>  sendMessage(HttpServletRequest request) {

       Map<String, Object> map1 = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();

       Chatroom chatroom = new Chatroom();

       chatroom.setAnchorname(request.getParameter("anchorname"));
       chatroom.setMessage(request.getParameter("message"));
       chatroom.setSender(request.getParameter("username"));

        try{

           chatRoomService.saveMessage(chatroom);

           map.put("success", true);
           map.put("message", "Successfully resisting");
           map1.put("response",map);

       }catch (Exception e){


           map.put("success", false);
           map.put("message", "the user has exist");
           map1.put("response",map);

       }
       return  map1;
   }

    /**
     * 按照当前系统时间来获取聊天室消息
     * @param request
     * @return
     */
    @RequestMapping("/getMessage.do")
    public  @ResponseBody  Map<String,Object>  getMessage(HttpServletRequest request) {
        List<Chatroom> chatrooms = new ArrayList<Chatroom>();

        List<Chatroom> list = chatRoomService.getMeeseageByName(request.getParameter("anchorName"));

        if(list==null){
            return null;
        }

        for(Chatroom chatroom: list){

            chatrooms.add(chatroom);
        }
        Map<String, Object> map1 = new HashMap<String, Object>();

        map1.put("messages",chatrooms);
        return  map1;


    }


}
