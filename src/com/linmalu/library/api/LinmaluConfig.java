package com.linmalu.library.api;

import com.linmalu.library.LinmaluLibrary;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class LinmaluConfig extends YamlConfiguration
{
	private static boolean _run = false;
	private static LinmaluConfig _linmaluConfig = null;
	private static Thread _linmaluConfigThread = null;
	private static BlockingQueue<LinmaluConfig> _linmaluConfigQueue = null;

	/**
	 * 초기화
	 */
	public static void initialize()
	{
		if(_run)
		{
			return;
		}
		_run = true;
		ConfigurationSerialization.registerClass(LinmaluLocation.class);
		ConfigurationSerialization.registerClass(LinmaluSquareLocation.class);
		_linmaluConfig = new LinmaluConfig();
		_linmaluConfigQueue = new ArrayBlockingQueue<>(10000);
		_linmaluConfigThread = new Thread(() ->
		{
			while(true)
			{
				try
				{
					LinmaluConfig config = _linmaluConfigQueue.take();
					if(config == _linmaluConfig)
					{
						break;
					}
					config.save(config._file);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		_linmaluConfigThread.start();
	}

	/**
	 * 종료
	 */
	public static void close()
	{
		if(!_run)
		{
			return;
		}
		_run = false;
		ConfigurationSerialization.unregisterClass(LinmaluLocation.class);
		ConfigurationSerialization.unregisterClass(LinmaluSquareLocation.class);
		try
		{
			_linmaluConfigQueue.put(_linmaluConfig);
			_linmaluConfigThread.join();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		_linmaluConfigThread = null;
		_linmaluConfigQueue.clear();
		_linmaluConfigQueue = null;
		_linmaluConfig = null;
	}

	private final File _file;
	private boolean _autoSave;

	private LinmaluConfig()
	{
		_file = null;
	}

	public LinmaluConfig(File file)
	{
		this(file, true);
	}

	public LinmaluConfig(File file, boolean autoSave)
	{
		_file = file;
		_autoSave = autoSave;
		reload();
	}

	/**
	 * 자동저장 유무 확인
	 */
	public boolean isAutoSave()
	{
		return _autoSave;
	}

	/**
	 * 자동저장 설정
	 */
	public void setAutoSave(boolean autoSave)
	{
		_autoSave = autoSave;
	}

	/**
	 * 리스트 형태의 값 가져오기
	 *
	 * @return Null Or Value
	 */
	public <T> List<T> getListData(String path)
	{
		return getListData(path, null);
	}

	/**
	 * 리스트 형태의 값 가져오기(실패시 기본값)
	 *
	 * @param def Null Or Value
	 * @return Null Or Value
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getListData(String path, List<T> def)
	{
		try
		{
			def = (List<T>)getList(path, def);
		}
		catch(Exception ignored)
		{
		}
		return def;
	}

	/**
	 * 값 설정
	 *
	 * @param value Null Or Value
	 */
	@Override
	public void set(String path, Object value)
	{
		super.set(path, value);
		if(_autoSave)
		{
			save();
		}
	}

	/**
	 * 값 삭제
	 */
	public void remove(String key)
	{
		set(key, null);
	}

	/**
	 * 초기화
	 */
	public void clear()
	{
		getKeys(false).iterator().forEachRemaining(this::remove);
	}

	/**
	 * 파일 다시 불러오기
	 */
	public void reload()
	{
		try
		{
			load(_file);
		}
		catch(FileNotFoundException ignored)
		{
		}
		catch(IOException | InvalidConfigurationException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 파일 저장
	 */
	public void save()
	{
		if(!_linmaluConfigQueue.offer(this))
		{
			Bukkit.getConsoleSender().sendMessage(LinmaluLibrary.getInstance().getTitle() + ChatColor.RED + "LinmaluConfigQueue Full!");
		}
	}

	/**
	 * LinmaluLocation 확인
	 */
	public boolean isLinmaluLocation(String path)
	{
		return isSet(path) && get(path) instanceof LinmaluLocation;
	}

	/**
	 * LinmaluLocation 가져오기
	 *
	 * @return Null Or Value
	 */
	public LinmaluLocation getLinmaluLocation(String path)
	{
		return getLinmaluLocation(path, null);
	}

	/**
	 * LinmaluLocation 가져오기(실패시 기본값)
	 *
	 * @param def Null Or Value
	 * @return Null Or Value
	 */
	public LinmaluLocation getLinmaluLocation(String path, LinmaluLocation def)
	{
		if(isLinmaluLocation(path))
		{
			return (LinmaluLocation)get(path);
		}
		return def;
	}

	/**
	 * LinmaluSquareLocation 확인
	 */
	public boolean isLinmaluSquareLocation(String path)
	{
		return isSet(path) && get(path) instanceof LinmaluSquareLocation;
	}

	/**
	 * LinmaluSquareLocation 가져오기
	 *
	 * @return Null Or Value
	 */
	public LinmaluSquareLocation getLinmaluSquareLocation(String path)
	{
		return getLinmaluSquareLocation(path, null);
	}

	/**
	 * LinmaluSquareLocation 가져오기(실패시 기본값)
	 *
	 * @param def Null Or Value
	 * @return Null Or Value
	 */
	public LinmaluSquareLocation getLinmaluSquareLocation(String path, LinmaluSquareLocation def)
	{
		if(isLinmaluSquareLocation(path))
		{
			return (LinmaluSquareLocation)get(path);
		}
		return def;
	}

}
