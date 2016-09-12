package com.example.message;

import com.example.utils.Strings;

public class MsgAlive extends Message {

    public int battery_status = 0;
    public int battery_percent = 0;
    public int battery_temperature = 0;
	
	public MsgAlive()
    {
        head = new MessageHead();
        head.id = 0;
        head.nodeID = 0;
        head.type = MessageType.Alive;
    }
	
	public MsgAlive(long id, int nodeID, int _battery_status, int _battery_percent, int _battery_temperature)
    {
        head = new MessageHead();
        head.id = id;
        head.nodeID = nodeID;
        head.type = MessageType.Alive;

        battery_status = _battery_status;
        battery_percent = _battery_percent;
        battery_temperature = _battery_temperature;
    }

	public int serialize(byte[] _data)
    {
        int idx = super.serialize(_data);
        insertInt(battery_status, data, size, idx);
        idx += Strings.INT_LENGTH;
        insertInt(battery_percent, data, size, idx);
        idx += Strings.INT_LENGTH;
        insertInt(battery_temperature, data, size, idx);
        idx += Strings.INT_LENGTH;
        return idx;
    }

	public int deserialize(byte[] _data, int _size)
    {
        int idx = super.deserialize(_data, _size);
        battery_status = getInt(data, size, idx);
        idx += Strings.INT_LENGTH;
        battery_percent = getInt(data, size, idx);
        idx += Strings.INT_LENGTH;
        battery_temperature = getInt(data, size, idx);
        idx += Strings.INT_LENGTH;
        return idx;
    }
}
