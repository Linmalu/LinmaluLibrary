package com.linmalu.library.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class LinmaluBlacklist
{
	private static HashMap<String, BlackData> map;

	public static void initialize()
	{
		final SimpleDateFormat format = new SimpleDateFormat("yyyy년MM월dd일_HH시mm분ss초");
		map = new HashMap<String, BlackData>();

		new Thread(() ->
		{
			for(String type : new String[]{"http://list.nickname.mc-blacklist.kr/", "http://list.uuid.mc-blacklist.kr/",
					"http://list.ip.mc-blacklist.kr/"})
			{
				try
				{
					URL url = new URL(type);
					HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
					urlConn.setDoOutput(true);

					BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
					br.readLine();

					String line;
					while((line = br.readLine()) != null)
					{
						for(String value : line.split("</br>"))
						{
							if(value.equals("") || value.equals(" ") || value.startsWith("ban_date | "))
								continue;

							String[] data = value.split(" \\| ");
							String date = data[0];
							String ban = data[1];
							String reason = data[2].replaceAll("_", " ");
							String punisher = data[3];

							map.put(ban, new BlackData(format.parse(date), reason, punisher));
						}
					}

				}
				catch(Exception e)
				{
				}
			}

			try
			{
				URL url = new URL("http://ip.mc-blacklist.kr/");
				HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
				urlConn.setDoOutput(true);

				BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
				String ip = br.readLine().substring(1);

				if(map.containsKey(ip))
				{
					Bukkit.getConsoleSender().sendMessage("§c#============================#");
					Bukkit.getConsoleSender().sendMessage("귀하의 서버는 블랙리스트에 등록되어");
					Bukkit.getConsoleSender().sendMessage("플러그인에 의해 구동이 제한됩니다.");
					Bukkit.getConsoleSender().sendMessage("");
					Bukkit.getConsoleSender().sendMessage("§4사유: ");
					Bukkit.getConsoleSender().sendMessage("  \"" + map.get(ip).getReason() + "\"");
					Bukkit.getConsoleSender().sendMessage("§c#============================#");
					Bukkit.shutdown();
					return;
				}
			}
			catch(IOException e)
			{
			}

		}).start();
	}

	public static boolean contains(OfflinePlayer player)
	{
		if(Material.getMaterial("DOUBLE_PLANT") != null && map.containsKey(player.getUniqueId().toString()))
			return true;
		if(map.containsKey(player.getName()))
			return true;

		if(player.isOnline())
		{
			Player p = player.getPlayer();

			if(map.containsKey(p.getAddress().getAddress().getHostAddress()))
			{
				return true;
			}
		}

		return false;
	}

	public static BlackData getBlackData(OfflinePlayer player)
	{
		if(Material.getMaterial("DOUBLE_PLANT") != null && map.containsKey(player.getUniqueId().toString()))
			return map.get(player.getUniqueId().toString());
		if(map.containsKey(player.getName()))
			return map.get(player.getName());

		if(player.isOnline())
		{
			String ip = player.getPlayer().getAddress().getAddress().getHostAddress();

			if(map.containsKey(ip))
			{
				return map.get(ip);
			}
		}

		return null;
	}

	public static void kick(Player player)
	{
		if(Material.getMaterial("DOUBLE_PLANT") != null)
			log(player.getName(), player.getUniqueId().toString(), player.getAddress().getHostName(),
					getBlackData(player).getReason(), getBlackData(player).getPunisher());
		else
			log(player.getName(), null, player.getAddress().getHostName(), getBlackData(player).getReason(),
					getBlackData(player).getPunisher());
		player.kickPlayer("§7§l[Blacklist]§r\n§8블랙 리스트에 등록된 사용자입니다!§r\n\n§c§l\"§4" + getBlackData(player).getReason()
				+ "§c§l\"§r");
	}

	public static class BlackData
	{
		private final Date date;
		private final String reason;
		private final String punisher;

		public BlackData(Date date, String reason, String punisher)
		{
			this.date = date;
			this.reason = reason;
			this.punisher = punisher;
		}

		public Date getDate()
		{
			return this.date;
		}

		public String getReason()
		{
			return this.reason;
		}

		public String getPunisher()
		{
			return this.punisher;
		}
	}

	public static void log(String nickname, String uuid, String ip, String reason, String by)
	{
		new Thread(() ->
		{
			try
			{
				URL url = new URL("http://horyu.cafe24.com/Minecraft/Plugin/Blacklist/log.php");
				Map<String, Object> params = new LinkedHashMap<String, Object>();
				params.put("nickname", nickname);
				params.put("ip", ip);
				params.put("reason", reason);
				params.put("uuid", uuid != null ? uuid : "none");
				params.put("by", by);

				StringBuilder postData = new StringBuilder();
				for(Map.Entry<String, Object> param : params.entrySet())
				{
					if(postData.length() != 0)
						postData.append('&');
					postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
					postData.append('=');
					postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
				}
				byte[] postDataBytes = postData.toString().getBytes("UTF-8");

				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
				conn.setDoOutput(true);
				conn.getOutputStream().write(postDataBytes);
				new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			}
			catch(Exception e)
			{
			}
		}).start();
	}
}
