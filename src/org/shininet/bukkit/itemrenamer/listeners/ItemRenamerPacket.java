package org.shininet.bukkit.itemrenamer.listeners;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.server.ItemStack;

import org.shininet.bukkit.itemrenamer.ItemRenamer;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;

public class ItemRenamerPacket {

	private ItemRenamer myPlugin;
	private ProtocolManager protocolManager;
	private final Logger logger;
	private PacketAdapter packetAdapter;

	public ItemRenamerPacket(ItemRenamer myPlugin, ProtocolManager protocolManager, Logger logger) {
		this.myPlugin = myPlugin;
		this.protocolManager = protocolManager;
		this.logger = logger;
		addListener();
	}
	
	public void addListener() {

		protocolManager.addPacketListener(packetAdapter = new PacketAdapter(myPlugin, ConnectionSide.SERVER_SIDE, 0x67, 0x68) {
			@Override
			public void onPacketSending(PacketEvent event) {
				PacketContainer packet = event.getPacket();
				
				try {
					switch (event.getPacketID()) {
					case 0x67:
						myPlugin.process(packet.getSpecificModifier(ItemStack.class).read(0));
						break;

					case 0x68:
						myPlugin.process(packet.getSpecificModifier(ItemStack[].class).read(0));
						break;
				
					}
				} catch (FieldAccessException e) {
					logger.log(Level.WARNING, "Couldn't access field.", e);
				}
			}
		});
	}
	
	public void unregister() {
		protocolManager.removePacketListener(packetAdapter);
	}

}
