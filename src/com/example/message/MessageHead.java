package com.example.message;

public class MessageHead
{
    public long id = -1;
    public int nodeID = -1;
    public int type = MessageType.Invalid;
    public final static int LENGTH = 32;
    public String toString()
    {
        return "<id:{" + id + "}, nodeID:{" + nodeID + "}, type:{" + type + "}>";
    }
}