package com.example.message;

import com.example.utils.Strings;

import java.io.UnsupportedEncodingException;

public class MsgCmdPlayByName extends Message {
	
	public String video = "";
	public String video_hash_code = "";
	public String deviceId = "";
    public int hash_code_flag = 0; //0 for not validate hash code, 1 for validate
    public int device_flag = 0; //0 for not validate device, 1 for validate
	
	public MsgCmdPlayByName()
    {
        head = new MessageHead();
        head.type = MessageType.CMD_Play;
        head.id = 0;
        head.nodeID = 0;
    }
	
    public MsgCmdPlayByName(long _id, int _nodeID, String _video, String _video_hash_code, String _deviceId, int _hash_code_flag, int _device_flag)
    {
        head = new MessageHead();
        head.type = MessageType.CMD_Play;
        head.id = _id;
        head.nodeID = _nodeID;
        video = _video;
        video_hash_code = _video_hash_code;
        deviceId = _deviceId;
        hash_code_flag = _hash_code_flag;
        device_flag = _device_flag;
    }
    
    public int serialize(byte[] _data)
    {
        int idx = super.serialize(_data);
        try {
            insertString(video, data, size, idx);
            idx += Strings.INT_LENGTH + video.getBytes("UnicodeLittleUnmarked").length;
            insertString(video_hash_code, data, size, idx);
            idx += Strings.INT_LENGTH + video_hash_code.getBytes("UnicodeLittleUnmarked").length;
            insertString(deviceId, data, size, idx);
            idx += Strings.INT_LENGTH + deviceId.getBytes("UnicodeLittleUnmarked").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        insertInt(hash_code_flag, data, size, idx);
        idx += Strings.INT_LENGTH;
        insertInt(device_flag, data, size, idx);
        idx += Strings.INT_LENGTH;
        return idx;
    }

    public int deserialize(byte[] _data, int _size)
    {
        int idx = super.deserialize(_data, _size);
        try {
            video = getString(data, size, idx);
            idx += Strings.INT_LENGTH + video.getBytes("UnicodeLittleUnmarked").length;
            video_hash_code = getString(data, size, idx);
            idx += Strings.INT_LENGTH + video_hash_code.getBytes("UnicodeLittleUnmarked").length;
            deviceId = getString(data, size, idx);
            idx += Strings.INT_LENGTH + deviceId.getBytes("UnicodeLittleUnmarked").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        hash_code_flag = getInt(data, size, idx);
        idx += Strings.INT_LENGTH;
        device_flag = getInt(data, size, idx);
        idx += Strings.INT_LENGTH;
        return idx;
    }
}
