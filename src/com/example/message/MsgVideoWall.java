package com.example.message;

import java.io.UnsupportedEncodingException;

import com.example.utils.*;

public class MsgVideoWall extends Message {
	
	public String video_name = "";
    public long remaining = 0;
	
	public MsgVideoWall()
    {
        head = new MessageHead();
        head.id = 0;
        head.nodeID = 0;
        head.type = MessageType.STATUS_Home;
    }
	
	public MsgVideoWall(long id, int nodeID)
    {
        head = new MessageHead();
        head.id = id;
        head.nodeID = 0;
        head.type = MessageType.STATUS_Home;
    }

	public int serialize(byte[] data_)
    {
    	int idx = super.serialize(data_);
        try {
            insertString(video_name, data, data.length, idx);
            idx += Strings.INT_LENGTH + video_name.getBytes("UnicodeLittleUnmarked").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        insertLong(remaining, data, data.length, idx);
        idx += Strings.LONG_LENGTH;
        return idx;
    }

	public int deserialize(byte[] data_, int size_)
    {
    	int idx = super.deserialize(data_, size_);
        try {
            video_name = getString(data, size, idx);
            idx += Strings.INT_LENGTH + video_name.getBytes("UnicodeLittleUnmarked").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        remaining = getLong(data, size, idx);
    	idx += Strings.LONG_LENGTH;
        return idx;
    }
}
