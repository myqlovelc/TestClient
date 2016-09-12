package com.example.message;

import com.example.utils.*;

public class MsgCmdStop extends Message {
	
	public long video;
	
    public MsgCmdStop(long _id, int _nodeID, int _video)
    {
        head = new MessageHead();
        head.type = MessageType.CMD_Stop;
        head.id = _id;
        head.nodeID = _nodeID;
        video = _video;
    }
    
    public int serialize(byte[] _data)
    {
        int idx = super.serialize(_data);
        insertLong(video, data, size, 0);
        idx += Strings.LONG_LENGTH;
        return idx;
    }

    public int deserialize(byte[] _data, int _size)
    {
        int idx = super.deserialize(_data, _size);
        video = getLong(data, size, idx);
        idx += Strings.LONG_LENGTH;
        return idx;
    }
}
