package com.example.message;

public class MsgCmdPause extends Message {
	
	public MsgCmdPause(long _id, int _nodeID)
    {
        head = new MessageHead();
        head.type = MessageType.CMD_Pause;
        head.id = _id;
        head.nodeID = _nodeID;
    }
    
	public int serialize(byte[] _data)
    {
        int idx = super.serialize(_data);
        return idx;
    }

	public int deserialize(byte[] _data, int _size)
    {
        int idx = super.deserialize(_data, _size);
        return idx;
    }
}
