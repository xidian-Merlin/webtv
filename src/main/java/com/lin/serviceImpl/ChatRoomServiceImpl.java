package com.lin.serviceImpl;

import com.lin.dao.ChatroomMapper;
import com.lin.domain.Chatroom;
import com.lin.domain.ChatroomExample;
import com.lin.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import com.lin.domain.ChatroomExample.Criteria;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by tongho on 2017/5/2.
 */
@Service("ChatRoomService")
public class ChatRoomServiceImpl implements ChatRoomService {

    @Autowired
    private ChatroomMapper chatroomMapper;


    @Override
    public List<Chatroom> getMeeseageByName(String AnchorName) {

          //按当前时间戳从数据库中读取数据
        //获取当前时间



        //条件查询
        ChatroomExample example = new ChatroomExample();

        Criteria criteria = example.createCriteria();
        criteria.andAnchornameEqualTo(AnchorName);


        Calendar calendar=Calendar.getInstance();
        Date nowTime=calendar.getTime();
        System.out.println(nowTime);
        calendar.add(Calendar.SECOND, -2); //减填负数,当前时间上2秒的时间戳
        nowTime=calendar.getTime();
        criteria.andSendtimeGreaterThanOrEqualTo(nowTime);
        //按条件查询所有的消息

        return chatroomMapper.selectByExample(example);





    }

    @Override
    public boolean saveMessage(Chatroom chatroom) {

       try{

           chatroomMapper.insert(chatroom);
           return  true;

       }   catch (Exception e){

           return  false;
       }

    }
}
