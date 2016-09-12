package com.example.message;

import com.example.utils.Strings;

public class MsgCmdSeek extends Message{
	
	public long seeked_location;
	
	public MsgCmdSeek()
    {
        head = new MessageHead();
        head.type = MessageType.CMD_Seek;
        head.id = 0;
        head.nodeID = 0;
        seeked_location = (long)0;
    }
	
    public MsgCmdSeek(long _id, int _nodeID, long _seeked_location)
    {
        head = new MessageHead();
        head.type = MessageType.CMD_Seek;
        head.id = _id;
        head.nodeID = _nodeID;
        seeked_location = _seeked_location;
    }
    
    public int serialize(byte[] _data)
    {
        int idx = super.serialize(_data);
        insertLong(seeked_location, data, size, idx);
        idx += Strings.LONG_LENGTH;
        return idx;
    }

    public int deserialize(byte[] _data, int _size)
    {
        int idx = super.deserialize(_data, _size);
        seeked_location = getLong(data, size, idx);
        idx += Strings.LONG_LENGTH;
        return idx;
    }
}
