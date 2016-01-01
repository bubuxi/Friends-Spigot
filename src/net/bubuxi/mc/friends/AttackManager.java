package net.bubuxi.mc.friends;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by zekunshen on 12/30/15.
 */
public class AttackManager implements Listener{

    private HashSet<Player> canAttackFriends = new HashSet<>();

    protected boolean canAttackFriends(Player p) {
        return !canAttackFriends.contains(p);
    }

    protected void toggleAttackMode(Player p) {
        if(canAttackFriends.contains(p)) {
            canAttackFriends.remove(p);
        }
        else {
            canAttackFriends.add(p);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onAttack(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player&&e.getEntity() instanceof Player) {
            Player damager, entity;
            damager = (Player)e.getDamager();
            entity = (Player)e.getEntity();
            if(Friends.getInstance().fm.areFriends(damager.getName(),entity.getName())&&!canAttackFriends(damager)) {
                e.setCancelled(true);
            }
        }
        if(e.getDamager() instanceof Arrow &&e.getEntity() instanceof  Player) {
            if(((Arrow)e.getDamager()).getShooter() instanceof Player) {
                Player damager, entity;
                damager = (Player)(((Arrow)e.getDamager()).getShooter());
                entity = (Player)e.getEntity();
                if(Friends.getInstance().fm.areFriends(damager.getName(),entity.getName())&&!canAttackFriends(damager)) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
