package top.bingk.jtable.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.alibaba.druid.filter.stat.StatFilter;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;

/**
 * JFinal的Model测试用例
 * 
 * @author ilgqh
 * @version 2.0
 */
public class TestCase {

    protected static DruidPlugin dp;
    protected static ActiveRecordPlugin activeRecord;
    private static boolean commit = true;

    private static Connection conn;

    private static Connection connection;
    private static String dataSql;
    private static String createTableSql;
    private static String truncateSql;
    private static Statement st;

    @Before
    public void createTable() throws Exception {
        st.execute(truncateSql);
        st.execute(dataSql);
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        PropKit.use("config.properties");
        dp = new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("userName"), PropKit.get("password").trim());
        dp.setDriverClass("org.h2.Driver");
        dp.addFilter(new StatFilter());
        dp.getDataSource();
        if (StrKit.notBlank(readTxtFileAll("../../../schema.sql"))) {
            createTableSql = readTxtFileAll("../../../schema.sql");
        } else {
            createTableSql = readTxtFileAll("schema.sql");
        }
        if (StrKit.notBlank(readTxtFileAll("../../../truncate.sql"))) {
            truncateSql = readTxtFileAll("../../../truncate.sql");
        } else {
            truncateSql = readTxtFileAll("schema.sql");
        }
        dp.setConnectionInitSql(createTableSql);
        dp.start();

        DataSource dataSource = dp.getDataSource();
        connection = dataSource.getConnection();
        st = connection.createStatement();
        if (StrKit.notBlank(readTxtFileAll("../../../data.sql"))) {
            dataSql = readTxtFileAll("../../../data.sql");
        } else {
            dataSql = readTxtFileAll("data.sql");
        }
        st.execute(dataSql);

        activeRecord = new ActiveRecordPlugin(dp);
        activeRecord.setDialect(new MysqlDialect());
        // 映射数据库的表和继承与model的实体
        // 只有做完该映射后，才能进行junit测试
        activeRecord.addMapping("basic_dictionary", "dictionaryId", BasicDictionary.class);
        activeRecord.getEngine().setDevMode(PropKit.use("config.properties").getBoolean("devMode"));

        activeRecord.start();

        conn = DbKit.getConfig().getDataSource().getConnection();
        DbKit.getConfig().setThreadLocalConnection(conn);
        // 自动提交变成false
        conn.setAutoCommit(false);

    }

    @SuppressWarnings("static-access")
    protected void setCommit(boolean commit) {
        this.commit = commit;
    }

    @AfterClass
    public static void closeStream() throws Exception {
        st.close();
        connection.close();
        activeRecord.stop();
        dp.stop();
    }

    @After
    public void tearDown() throws Exception {
        if (commit) {
            conn.rollback();
        } else {
            conn.commit();
        }
        commit = true;
    }

    protected static String readTxtFileAll(String fileP) {
        try {

            String filePath = (PathKit.getPath(TestCase.class)).split("target")[0] + "src/main/resources/";
            filePath = filePath.replaceAll("file:/", "");
            filePath = filePath.replaceAll("%20", " ");
            filePath = filePath.trim() + fileP.trim();
            if (filePath.indexOf(":") != 1) {
                filePath = File.separator + filePath;
            }
            String encoding = "utf-8";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                StringBuffer sb = new StringBuffer();
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    sb.append(lineTxt);
                }
                read.close();
                return sb.toString();
            } else {
                System.out.println("找不到指定的文件,查看此路径是否正确:" + filePath);
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
        }
        return "";
    }

}