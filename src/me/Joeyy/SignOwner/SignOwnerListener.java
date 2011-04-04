package me.Joeyy.SignOwner;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class SignOwnerListener extends BlockListener {
	public static SignOwner plugin;

	public SignOwnerListener(SignOwner instance) {
		plugin = instance;
	}

	public void onBlockPlace(BlockPlaceEvent event) {
		Block block = event.getBlock();
		Location location = block.getLocation();
		Player player = event.getPlayer();

		if (block.getType() == Material.SIGN_POST
				|| block.getType() == Material.WALL_SIGN) {
			plugin.signOwners.put(location, player.getName());
		}
	}

	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		if (block.getType() == Material.SIGN_POST
				|| block.getType() == Material.WALL_SIGN) {
			plugin.signOwners.remove(event.getBlock().getLocation());
		}
	}

	@SuppressWarnings("static-access")
	public void onBlockDamage(BlockDamageEvent event) {
		Block block = event.getBlock();
		Player player = event.getPlayer();

		if (!(plugin.Permissions.has(player, "SignOwner.getMessage") || (player.isOp()))) {
			return;
		}

		if (block.getType() != Material.SIGN_POST
				&& block.getType() != Material.WALL_SIGN) {
			return;
		}

		if (plugin.signOwners.containsKey(event.getBlock().getLocation()) == false
				&& (plugin.Permissions.has(player, "SignOwner.getMessage") || player.isOp())) {
			player.sendMessage("This sign was placed before enabling the plugin.");
		}
		
		if (plugin.signOwners.containsKey(event.getBlock().getLocation()) == true
				&& (plugin.Permissions.has(player, "SignOwner.getMessage") || player.isOp())) {
			player.sendMessage("This sign is placed by: "
					+ plugin.signOwners.get(event.getBlock().getLocation()));
		}
	}
}