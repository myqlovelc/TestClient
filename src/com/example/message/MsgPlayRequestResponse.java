package com.example.message;


import java.io.UnsupportedEncodingException;

import com.example.utils.Strings;

public class MsgPlayRequestResponse extends Message {

    public static int OK = 0;
    public static int FileNotFound = 1;
    public static int DeviceNotAuthorized = 2;
    public static int VideoNotAuthorized = 4;
    public static int BothNotAuthorized = 5;
    public static int HeadsetUnmounted = 3;
    public static int InvalidVideoSuffix = 6;
    public static int CommandBuffered = 7;
    public static int InvalidVideoData = 8;
    public static int VideoLoading = 9;

	public long requestId = 0;
	public String video_name = "";
	public int status = MsgPlayRequestResponse.OK;
	
	public MsgPlayRequestResponse()
    {
        head = new MessageHead();
        head.type = MessageType.ACK_Play_OK;
        head.id = 0;
        head.nodeID = 0;
    }
	
    public MsgPlayRequestResponse(long _id, int _nodeID, long _requestId, String _video_name, int _status)
    {
        head = new MessageHead();
        head.type = MessageType.ACK_Play_OK;
        head.id = _id;
        head.nodeID = _nodeID;
        requestId = _requestId;
        video_name = _video_name;
        status = _status;
    }
    
    public int serialize(byte[] _data)
    {
        int idx = super.serialize(_data);
        insertLong(requestId, data, size, idx);
        idx += Strings.LONG_LENGTH;
        try {
            insertString(video_name, data, size, idx);
            idx += Strings.INT_LENGTH + video_name.getBytes("UnicodeLittleUnmarked").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        insertInt(status, data, size, idx);
        idx += Strings.INT_LENGTH;
        return idx;
    }

    public int deserialize(byte[] _data, int _size)
    {
        int idx = super.deserialize(_data, _size);
        requestId = getLong(data, size, idx);
        idx += Strings.LONG_LENGTH;
        try {
            video_name = getString(data, size, idx);
            idx += Strings.INT_LENGTH + video_name.getBytes("UnicodeLittleUnmarked").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        status = getInt(data, size, idx);
        idx += Strings.INT_LENGTH;
        return idx;
    }
}
