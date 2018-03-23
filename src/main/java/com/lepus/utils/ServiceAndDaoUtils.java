package com.lepus.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author wangchenyu@cit
 * @date   2017-9-21
 */
public class ServiceAndDaoUtils {
	
	public static final String PROJECT_NAME = "security geermu2";
	public static final String MODULE_NAME = "mobile\\homework";
	public static final String MODULE_PKG_NAME = "mobile.homework";
	public static final String IGENERIC_DAO_NAME = "IGenericDAO";
	public static final String GENERIC_DAO_NAME = "GenericDAO";
	public static final String[] names = {"Homework"};
	
	private static String getAuthor(){
		return "wangchenyu@cit";
	}
	
	private static String getDate(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}

	public static void main(String[] args){
		gen();
	}
	
	public static void gen(){
		String beanPath = "D:\\MyEclipse\\Workspaces\\" + PROJECT_NAME + "\\src\\com\\dc\\module\\" + MODULE_NAME + "\\bean\\";
		String daoPath = "D:\\MyEclipse\\Workspaces\\" + PROJECT_NAME + "\\src\\com\\dc\\module\\" + MODULE_NAME + "\\dao\\";
		String daoImplPath = "D:\\MyEclipse\\Workspaces\\" + PROJECT_NAME + "\\src\\com\\dc\\module\\" + MODULE_NAME + "\\dao\\impl\\";
		String servicePath = "D:\\MyEclipse\\Workspaces\\" + PROJECT_NAME + "\\src\\com\\dc\\module\\" + MODULE_NAME + "\\service\\";
		String serviceImplPath = "D:\\MyEclipse\\Workspaces\\" + PROJECT_NAME + "\\src\\com\\dc\\module\\" + MODULE_NAME + "\\service\\impl\\";
		try {
			for(String name : names){
				genBean(name, beanPath);
				genDao(name, daoPath);
				genDaoImpl(name, daoImplPath);
				genService(name, servicePath);
				genServiceImpl(name, serviceImplPath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void mkdirs(File file){
		String absPath = file.getAbsolutePath();
		String[] paths = absPath.split("\\\\");
		String folderPath = absPath.replace(paths[paths.length - 1], "");
		File folder = new File(folderPath);
		if(!folder.exists())
			folder.mkdirs();
	}
	
	private static void genBean(String name, String beanPath) throws Exception{
		String beanFilename = name + ".java";
		File beanFile = new File(beanPath + beanFilename);
		if(!beanFile.exists()){
			mkdirs(beanFile);
			OutputStream os = new FileOutputStream(beanFile);
			os.write(genBeanFile(name).getBytes());
			os.flush();
			os.close();
		}
	}
	
	private static void genDao(String name, String daoPath) throws Exception{
		String daoFilename = "I" + name + "DAO.java";
		File daoFile = new File(daoPath + daoFilename);
		if(!daoFile.exists()){
			mkdirs(daoFile);
			OutputStream os = new FileOutputStream(daoFile);
			os.write(genDaoFile(name).getBytes());
			os.flush();
			os.close();
		}
	}
	
	private static void genDaoImpl(String name, String daoImplPath) throws Exception{
		String daoImplFilename = name + "DAO.java";
		File daoImplFile = new File(daoImplPath + daoImplFilename);
		if(!daoImplFile.exists()){
			mkdirs(daoImplFile);
			OutputStream os = new FileOutputStream(daoImplFile);
			os.write(genDaoImplFile(name).getBytes());
			os.flush();
			os.close();
		}
	}
	
	private static void genService(String name, String servicePath) throws Exception{
		String serviceFilename = "I" + name + "Service.java";
		File serviceFile = new File(servicePath + serviceFilename);
		if(!serviceFile.exists()){
			mkdirs(serviceFile);
			OutputStream os = new FileOutputStream(serviceFile);
			os.write(genServiceFile(name).getBytes());
			os.flush();
			os.close();
		}
	}
	
	private static void genServiceImpl(String name, String serviceImplPath) throws Exception{
		String serviceImplFilename = name + "Service.java";
		File serviceImplFile = new File(serviceImplPath + serviceImplFilename);
		if(!serviceImplFile.exists()){
			mkdirs(serviceImplFile);
			OutputStream os = new FileOutputStream(serviceImplFile);
			os.write(genServiceImplFile(name).getBytes());
			os.flush();
			os.close();
		}
	}
	
	private static String genBeanFile(String name){
		StringBuilder sb = new StringBuilder();
		sb.append("package com.dc.module." + MODULE_PKG_NAME + ".bean;\n\n");
		sb.append("import java.io.Serializable;\n\n");
		sb.append("import javax.persistence.Column;\n");
		sb.append("import javax.persistence.Entity;\n");
		sb.append("import javax.persistence.GeneratedValue;\n");
		sb.append("import javax.persistence.Id;\n\n");
		sb.append("import org.hibernate.annotations.GenericGenerator;\n\n");
		sb.append("/**\n");
		sb.append(" * \n");
		sb.append(" * @author " + getAuthor() + "\n");
		sb.append(" * @date   " + getDate() + "\n");
		sb.append(" */\n");
		sb.append("@Entity\n");
		sb.append("public class " + name + " implements Serializable{\n\n");
		sb.append("\t@Id\n");
		sb.append("\t@Column(length = 32)\n");
		sb.append("\t@GeneratedValue(generator = \"system-uuid\")\n");
		sb.append("\t@GenericGenerator(strategy = \"uuid\", name = \"system-uuid\")\n");
		sb.append("\tprivate String id;\n\n");
		sb.append("\tpublic String getId() {\n");
		sb.append("\t\treturn id;\n");
		sb.append("\t}\n\n");
		sb.append("\tpublic void setId(String id) {\n");
		sb.append("\t\tthis.id = id;\n");
		sb.append("\t}\n\n");
		sb.append("}");
		return sb.toString();
	}
	
	private static String genDaoFile(String name){
		StringBuilder sb = new StringBuilder();
		sb.append("package com.dc.module." + MODULE_PKG_NAME + ".dao;\n\n");
		sb.append("import org.rock.dao." + IGENERIC_DAO_NAME + ";\n\n");
		sb.append("import com.dc.module." + MODULE_PKG_NAME + ".bean." + name + ";\n\n");
		sb.append("/**\n");
		sb.append(" * \n");
		sb.append(" * @author " + getAuthor() + "\n");
		sb.append(" * @date   " + getDate() + "\n");
		sb.append(" */\n");
		sb.append("public interface I" + name + "DAO extends " + IGENERIC_DAO_NAME + "<" + name + ", String> {\n\n");
		sb.append("}");
		return sb.toString();
	}
	
	private static String genDaoImplFile(String name){
		StringBuilder sb = new StringBuilder();
		sb.append("package com.dc.module." + MODULE_PKG_NAME + ".dao.impl;\n\n");
		sb.append("import org.rock.dao." + GENERIC_DAO_NAME + ";\n");
		sb.append("import org.springframework.stereotype.Repository;\n\n");
		sb.append("import com.dc.module." + MODULE_PKG_NAME + ".bean." + name + ";\n");
		sb.append("import com.dc.module." + MODULE_PKG_NAME + ".dao.I" + name + "DAO;\n\n");
		sb.append("/**\n");
		sb.append(" * \n");
		sb.append(" * @author " + getAuthor() + "\n");
		sb.append(" * @date   " + getDate() + "\n");
		sb.append(" */\n");
		sb.append("@Repository\n");
		sb.append("public class " + name + "DAO extends " + GENERIC_DAO_NAME + "<" + name + ", String> implements I" + name + "DAO {\n\n");
		sb.append("}");
		return sb.toString();
	}
	
	private static String genServiceFile(String name){
		StringBuilder sb = new StringBuilder();
		sb.append("package com.dc.module." + MODULE_PKG_NAME + ".service;\n\n");
		sb.append("import org.rock.bean.PageBean;\n");
		sb.append("import com.dc.module." + MODULE_PKG_NAME + ".bean." + name + ";\n\n");
		sb.append("/**\n");
		sb.append(" * \n");
		sb.append(" * @author " + getAuthor() + "\n");
		sb.append(" * @date   " + getDate() + "\n");
		sb.append(" */\n");
		sb.append("public interface I" + name + "Service {\n\n");
		sb.append("\tpublic PageBean<" + name + "> findPage(PageBean<" + name + "> pageBean);\n\n");
		sb.append("\tpublic " + name + " save(" + name + " e);\n\n");
		sb.append("\tpublic " + name + " update(" + name + " e);\n\n");
		sb.append("\tpublic " + name + " fetch(String id);\n\n");
		sb.append("}");
		return sb.toString();
	}
	
	private static String genServiceImplFile(String name){
		StringBuilder sb = new StringBuilder();
		sb.append("package com.dc.module." + MODULE_PKG_NAME + ".service.impl;\n\n");
		sb.append("import javax.annotation.Resource;\n\n");
		sb.append("import org.rock.bean.HQL;\n");
		sb.append("import org.rock.bean.PageBean;\n");
		sb.append("import org.springframework.stereotype.Service;\n\n");
		sb.append("import com.dc.module." + MODULE_PKG_NAME + ".bean." + name + ";\n");
		sb.append("import com.dc.module." + MODULE_PKG_NAME + ".dao.I" + name + "DAO;\n");
		sb.append("import com.dc.module." + MODULE_PKG_NAME + ".service.I" + name + "Service;\n\n");
		sb.append("/**\n");
		sb.append(" * \n");
		sb.append(" * @author " + getAuthor() + "\n");
		sb.append(" * @date   " + getDate() + "\n");
		sb.append(" */\n");
		sb.append("@Service\n");
		sb.append("public class " + name + "Service implements I" + name + "Service{\n\n");
		sb.append("\t@Resource\n");
		sb.append("\tprivate I" + name + "DAO dao;\n\n");
		sb.append("\tpublic PageBean<" + name + "> findPage(PageBean<" + name + "> pageBean){\n");
		sb.append("\t\tHQL hql = new HQL(\"from " + name + "\");\n");
		sb.append("\t\treturn dao.find(pageBean, hql);\n");
		sb.append("\t}\n\n");
		sb.append("\tpublic " + name + " save(" + name + " e) {\n");
		sb.append("\t\treturn dao.save(e);\n");
		sb.append("\t}\n\n");
		sb.append("\tpublic " + name + " update(" + name + " e){\n");
		sb.append("\t\treturn dao.update(e);\n");
		sb.append("\t}\n\n");
		sb.append("\tpublic " + name + " fetch(String id){\n");
		sb.append("\t\treturn dao.findById(id);\n");
		sb.append("\t}\n\n");
		sb.append("}");
		return sb.toString();
	}
	
}
