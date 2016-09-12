package com.example.message;

import java.io.UnsupportedEncodingException;

import com.example.utils.*;

public class MsgStatusPaused extends Message{
	
	public long video = 0;
	public String video_name = "";
	public long remaining = 0;
	public long total_frame = 0;
	public long current_frame = 0;
	
	public MsgStatusPaused()
    {
        head = new MessageHead();
        head.id = 0;
        head.nodeID = 0;
        head.type = MessageType.STATUS_Paused;
    }
	
	public MsgStatusPaused(long id, int nodeID, String _video_name, long _total_frame, long _current_frame)
    {
        head = new MessageHead();
        head.id = id;
        head.nodeID = nodeID;
        head.type = MessageType.STATUS_Paused;
        video_name = _video_name;
        total_frame = _total_frame;
        current_frame = _current_frame;
    }

	public int serialize(byte[] _data)
    {
        int idx = super.serialize(_data);
        try {
            insertString(video_name, data, data.length, idx);
            idx += Strings.INT_LENGTH + video_name.getBytes("UnicodeLittleUnmarked").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        insertLong(video, data, data.length, idx);
        idx += Strings.LONG_LENGTH;
        insertLong(remaining, data, data.length, idx);
        idx += Strings.LONG_LENGTH;
        insertLong(total_frame, data, data.length, idx);
        idx += Strings.LONG_LENGTH;
        insertLong(current_frame, data, data.length, idx);
        idx += Strings.LONG_LENGTH;
        return idx;
    }

	public int deserialize(byte[] _data, int _size)
    {
        int idx = super.deserialize(_data, _size);
        try {
            video_name = getString(data, size, idx);
            idx += Strings.INT_LENGTH + video_name.getBytes("UnicodeLittleUnmarked").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        video = getLong(data, size, idx);
        idx += Strings.LONG_LENGTH;
        remaining = getLong(data, size, idx);
        idx += Strings.LONG_LENGTH;
        total_frame = getLong(data, size, idx);
        idx += Strings.LONG_LENGTH;
        current_frame = getLong(data, size, idx);
        idx += Strings.LONG_LENGTH;
        return idx;
    }

    public String toString()
    {
        return "<head:{" + head + "}, videoname:{" + video_name + "}, video:{" + video + "}, " +
        		"remaining:{" + remaining + "}, totalframe:{" + total_frame + "}, currentframe:{" + current_frame + "}>";
    }
}
