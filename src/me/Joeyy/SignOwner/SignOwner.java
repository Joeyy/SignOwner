package me.Joeyy.SignOwner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import org.bukkit.plugin.Plugin;


import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SignOwner extends JavaPlugin {
	public static final Logger log = Logger.getLogger("Minecraft");
	SignOwnerListener SignListener = new SignOwnerListener(this);
	HashMap<Location, String> signOwners = new HashMap<Location, String>();
	
	public static PermissionHandler Permissions;


	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.BLOCK_PLACE, SignListener,
				Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, SignListener,
				Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_DAMAGE, SignListener,
				Event.Priority.Normal, this);
		log.info("SignOwner enabled.");


		File file = new File(this.getDataFolder(), "signowners.txt");
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out
					.println("New file \"SignOwner.txt\" has been created to the current directory");
		}
		try {
			file.getParentFile().mkdirs();
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				String[] pieces = line.split(":");
				if (pieces.length != 5)
					continue;
				World locworld = getServer().getWorld(pieces[0]);
				if (locworld == null)
					continue;
				int x, y, z;
				try {
					x = Integer.valueOf(pieces[1]);
					y = Integer.valueOf(pieces[2]);
					z = Integer.valueOf(pieces[3]);
				} catch (Exception e) {
					continue;
				}
				String pname = pieces[4];
				Location l = new Location(locworld, x, y, z);
				signOwners.put(l, pname);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setupPermissions();

	}

	public void onDisable() {
		
		File file = new File(this.getDataFolder(), "signowners.txt");
		log.info("SignOwner disabled.");
		try {
			file.getParentFile().mkdirs();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			String newLineCharacter = System.getProperty("line.separator");
			for (Location loc : signOwners.keySet()) {
				bw.write(loc.getWorld().getName() + ":" + loc.getBlockX() + ":"
						+ loc.getBlockY() + ":" + loc.getBlockZ() + ":"
						+ signOwners.get(loc));
				bw.flush();
				bw.write(newLineCharacter);
				bw.flush();
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	  @SuppressWarnings("static-access")
	private void setupPermissions() {
	      Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");

	      if (this.Permissions == null) {
	          if (test != null) {
	              this.Permissions = ((Permissions)test).getHandler();
	          } else {
	              log.info("Permission system not detected, defaulting to OP");
	          }
	      }
	  }

}