package com.linmalu.library.api;

import com.linmalu.library.LinmaluLibrary;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
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
	private static Thread _linmaluConfigThread = null;
	private static BlockingQueue<LinmaluConfig> _linmaluConfigQueue = new ArrayBlockingQueue<>(10000);
	private static LinmaluConfig _config = new LinmaluConfig();

	/**
	 * 초기화
	 */
	public static void Initialize()
	{
		if(_run)
		{
			return;
		}
		_run = true;
		ConfigurationSerialization.registerClass(LinmaluLocation.class);
		ConfigurationSerialization.registerClass(LinmaluSquareLocation.class);
		_linmaluConfigQueue.clear();
		_linmaluConfigThread = new Thread(() ->
		{
			while(true)
			{
				try
				{
					LinmaluConfig config = _linmaluConfigQueue.take();
					if(config == _config)
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
	public static void Close()
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
			_linmaluConfigQueue.put(_config);
			_linmaluConfigThread.join();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		_linmaluConfigQueue.clear();
		_linmaluConfigThread = null;
	}

	private final File _file;
	private boolean _autoSave;

	private LinmaluConfig()
	{
		_file = null;
	}

	public LinmaluConfig(@NotNull File file)
	{
		this(file, true);
	}

	public LinmaluConfig(@NotNull File file, boolean autoSave)
	{
		_file = file;
		_autoSave = autoSave;
		reload();
	}

	/**
	 * 자동저장 유무 확인
	 *
	 * @return
	 */
	public boolean isAutoSave()
	{
		return _autoSave;
	}

	/**
	 * 자동저장 설정
	 *
	 * @param autoSave
	 */
	public void setAutoSave(boolean autoSave)
	{
		_autoSave = autoSave;
	}

	/**
	 * 리스트 형태의 값 가져오기
	 *
	 * @param path
	 * @param <T>
	 * @return Null Or Value
	 */
	public <T> List<T> getListData(@NotNull String path)
	{
		return getListData(path, null);
	}

	/**
	 * 리스트 형태의 값 가져오기(실패시 기본값)
	 *
	 * @param path
	 * @param def
	 * @param <T>
	 * @return Null Or Value
	 */
	public <T> List<T> getListData(@NotNull String path, @Nullable List<T> def)
	{
		try
		{
			def = (List<T>)getList(path, def);
		}
		catch(Exception e)
		{
		}
		return def;
	}

	/**
	 * 값 설정
	 *
	 * @param path
	 * @param value
	 */
	@Override
	public void set(@NotNull String path, @Nullable Object value)
	{
		super.set(path, value);
		if(_autoSave)
		{
			save();
		}
	}

	/**
	 * 값 삭제
	 *
	 * @param key
	 */
	public void remove(@NotNull String key)
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
		catch(FileNotFoundException e)
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
	 *
	 * @param path
	 * @return
	 */
	public boolean isLinmaluLocation(@NotNull String path)
	{
		return isSet(path) && get(path) instanceof LinmaluLocation;
	}

	/**
	 * LinmaluLocation 가져오기
	 *
	 * @param path
	 * @return Null Or Value
	 */
	public LinmaluLocation getLinmaluLocation(@NotNull String path)
	{
		return getLinmaluLocation(path, null);
	}

	/**
	 * LinmaluLocation 가져오기(실패시 기본값)
	 *
	 * @param path
	 * @param def
	 * @return Null Or Value
	 */
	public LinmaluLocation getLinmaluLocation(@NotNull String path, @Nullable LinmaluLocation def)
	{
		if(isLinmaluLocation(path))
		{
			return (LinmaluLocation)get(path);
		}
		return def;
	}

	/**
	 * LinmaluSquareLocation 확인
	 *
	 * @param path
	 * @return
	 */
	public boolean isLinmaluSquareLocation(@NotNull String path)
	{
		return isSet(path) && get(path) instanceof LinmaluSquareLocation;
	}

	/**
	 * LinmaluSquareLocation 가져오기
	 *
	 * @param path
	 * @return Null Or Value
	 */
	public LinmaluSquareLocation getLinmaluSquareLocation(@NotNull String path)
	{
		return getLinmaluSquareLocation(path, null);
	}

	/**
	 * LinmaluSquareLocation 가져오기(실패시 기본값)
	 *
	 * @param path
	 * @param def
	 * @return Null Or Value
	 */
	public LinmaluSquareLocation getLinmaluSquareLocation(@NotNull String path, @Nullable LinmaluSquareLocation def)
	{
		if(isLinmaluSquareLocation(path))
		{
			return (LinmaluSquareLocation)get(path);
		}
		return def;
	}

}
