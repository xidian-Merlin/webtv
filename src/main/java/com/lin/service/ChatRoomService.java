package com.lin.service;

import com.lin.domain.Chatroom;

import java.util.List;

/**
 * Created by tongho on 2017/5/2.
 */
public interface ChatRoomService {



    public List<Chatroom> getMeeseageByName(String AnchorName);

    public boolean saveMessage(Chatroom chatroom);


}
