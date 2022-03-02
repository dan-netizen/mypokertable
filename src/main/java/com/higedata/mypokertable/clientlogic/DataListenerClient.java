package com.higedata.mypokertable.clientlogic;


import com.higedata.mypokertable.clientlogic.packets.AddPlayerPacket;
import com.higedata.mypokertable.clientlogic.packets.RemovePlayerPacket;

public class DataListenerClient {
    public void received(Object p) {
        if(p instanceof AddPlayerPacket) {
            AddPlayerPacket packet = (AddPlayerPacket) p;
            PlayerHandler.players.put(packet.id, new NetPlayer(packet.id, packet.name));
            System.out.println(packet.name + " has joined the game");
        } else if(p instanceof RemovePlayerPacket) {
            RemovePlayerPacket packet = (RemovePlayerPacket) p;
            System.out.println(PlayerHandler.players.get(packet.id).name + " has left the game");
            PlayerHandler.players.remove(packet.id);
        }
    }
}
