package com.example.message;

import com.example.utils.Strings;

public class MsgRegisterRequestResponse extends Message {
	
	public long requestId = 0;
	
	public MsgRegisterRequestResponse()
    {
        head = new MessageHead();
        head.type = MessageType.ACK_Register_OK;
        head.id = 0;
        head.nodeID = 0;
    }
	
    public MsgRegisterRequestResponse(long _id, int _nodeID, long _requestId)
    {
        head = new MessageHead();
        head.type = MessageType.ACK_Register_OK;
        head.id = _id;
        head.nodeID = _nodeID;
        requestId = _requestId;
    }
    
    public int serialize(byte[] _data)
    {
        int idx = super.serialize(_data);
        insertLong(requestId, data, size, idx);
        idx += Strings.LONG_LENGTH;
        return idx;
    }

    public int deserialize(byte[] _data, int _size)
    {
        int idx = super.deserialize(_data, _size);
        requestId = getLong(data, size, idx);
        idx += Strings.LONG_LENGTH;
        return idx;
    }
}
