package edu.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * @author Subson Bigod
 * @email  subsontding@gmail.com
**/
public class FileUtil {

	public static List<String> fileToList(String file) {
		List<String> lines = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(file)));
			String line = null;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
			return lines;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return lines;
	}
	
	public static Map<String, String> fileToMap(String file) {
		Map<String, String> map = new HashMap<String, String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(file)));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] arr = line.split("\\s+");
				map.put(arr[0], arr[1]);
			}
			return map;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	public static void mapToFile(Map<?, ?> map, String file) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(new File(file)));
			Set<?> s = map.entrySet();
			Iterator<?> it = s.iterator();
			while (it.hasNext()) {
				Map.Entry m = (Map.Entry) it.next();
				writer.write(m.getKey() + "\t" + m.getValue() + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void listToFile(List<?> counts, String file) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(new File(file)));
			for (int i = 0; i < counts.size(); i++) {
				writer.write(counts.get(i) + "\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void listToFile(List<String> title, 
			List<Integer> context, String file) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(new File(file)));
			for (int i = 0; i < title.size() || i < context.size(); i++) {
				writer.write(title.get(i) + "\t" + context.get(i) + "\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static boolean mkdir(String mkdirName) {
		try {
			File dirFile = new File(mkdirName);
			boolean bFile = dirFile.exists();
			if (!bFile) {
				bFile = dirFile.mkdir();
				return bFile;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean mkdir(File file) {
		try {
			boolean bFile = file.exists();
			if (!bFile) {
				bFile = file.mkdir();
				return bFile;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	static public boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	static public List<String> getFilesPathByDir(String inputdir) {
		File dir = new File(inputdir);
		String absolutePath = dir.getAbsolutePath();
		String[] children = dir.list();
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < children.length; i++) {
			File file = new File(absolutePath+"\\"+children[i]);
			if (file.isFile()) list.add(file.getAbsolutePath());
		}
		return list;
	}

	static public List<String> getAllFilesPathByDir(String inputdir) {
		File dir = new File(inputdir);
		String absolutePath = dir.getAbsolutePath();
		String[] children = dir.list();
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < children.length; i++) {
			File file = new File(absolutePath+"\\"+children[i]);
			if (file.isFile()) list.add(file.getAbsolutePath());
			if (file.isDirectory()) {
				Collection<String> collection = getAllFilesPathByDir(file.getAbsolutePath());
				list.addAll(collection);
			}
		}
		return list;
	}

}
