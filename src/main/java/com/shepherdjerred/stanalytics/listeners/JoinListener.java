package com.shepherdjerred.stanalytics.listeners;

import com.shepherdjerred.stanalytics.mysql.stats.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerManager.hasJoinedBefore(player);
    }

}