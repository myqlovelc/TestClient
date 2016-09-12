package com.example.message;

import java.io.UnsupportedEncodingException;

import com.example.utils.*;

public class MsgCmdRegister extends Message {
	
	private ControllerConfig mController;
	
    public int PlayerID;
    
    public String DefaultVideo = "";

    public ControllerConfig getController() {
		return mController;
	}

	public void setController(ControllerConfig mController) {
		this.mController = mController;
	}

	public MsgCmdRegister(long id, ControllerConfig controller, int playerID)
    {
        // send register
        head = new MessageHead();
        head.id = id;
        head.nodeID = 0;
        head.type = MessageType.CMD_Register;

        if (controller == null) {
        	throw new java.lang.NullPointerException();
        }

        mController = controller;
        PlayerID = playerID;
    }

    public MsgCmdRegister(String controllerIP, int controllerID)
    {
        // receive cmd register
        mController = new ControllerConfig();
        mController.ip = controllerIP;
        mController.id = controllerID;
    }

    public int serialize(byte[] _data)
    {
        int idx = super.serialize(_data);
        insertInt(PlayerID, data, size, idx);//ignore
        idx += Strings.INT_LENGTH;
        insertInt(mController.alive_port, data, size, idx);
        idx += Strings.INT_LENGTH;
        insertInt(mController.status_port, data, size, idx);
        idx += Strings.INT_LENGTH;
        insertInt(mController.cmd_port, data, size, idx);
        idx += Strings.INT_LENGTH;
        try {
            insertString(DefaultVideo, data, size, idx);//ignore
            idx += Strings.INT_LENGTH + DefaultVideo.getBytes("UnicodeLittleUnmarked").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return idx;
    }

    public int deserialize(byte[] _data, int _size)
    {
        int idx = super.deserialize(_data, _size);
        PlayerID = getInt(data, size, idx);
        idx += Strings.INT_LENGTH;
        mController.alive_port = getInt(data, size, idx);
        idx += Strings.INT_LENGTH;
        mController.status_port = getInt(data, size, idx);
        idx += Strings.INT_LENGTH;
        mController.cmd_port = getInt(data, size, idx);
        idx += Strings.INT_LENGTH;
        try {
            DefaultVideo = getString(data, size, idx);
            idx += Strings.INT_LENGTH + DefaultVideo.getBytes("UnicodeLittleUnmarked").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return idx;
    }

    public String toString()
    {
        return "<head:{" + head + "}, controller:<id:{" + mController.id + "}, ip:{" + mController.ip + "}, " +
        		"alive_port:{" + mController.alive_port + "}, " + "status_port:{" + mController.status_port + "}, " +
        		"cmd_port:{" + mController.cmd_port + "}>, playerID:{" + PlayerID + "}>";
    }
}
