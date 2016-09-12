package com.example.message;

import java.io.UnsupportedEncodingException;

import com.example.utils.*;

public class MsgSimple extends Message {
	
	public String info = "";// set to ""
    public MsgSimple(long id, int nodeID, String _info)
    {
        head = new MessageHead();
        head.id = id;
        head.nodeID = nodeID;
        head.type = MessageType.STATUS_LoadingVideo;
        info = _info;
    }

    public int serialize(byte[] _data)
    {
        int idx = super.serialize(_data);
        try {
            insertString(info, data, size, idx);
			idx += Strings.INT_LENGTH + info.getBytes("UnicodeLittleUnmarked").length;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return idx;
    }

    public int deserialize(byte[] _data, int _size)
    {
        int idx = super.deserialize(_data, _size);
        try {
            info = getString(data, size, idx);
			idx += Strings.INT_LENGTH + info.getBytes("UnicodeLittleUnmarked").length;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return idx;
    }
}
